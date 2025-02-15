@file:Suppress("KDocUnresolvedReference")

package com.slickpath.mobile.android.simple.vm.machine

import android.content.Context
import android.util.Log
import com.slickpath.mobile.android.simple.vm.*
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet
import com.slickpath.mobile.android.simple.vm.instructions.Instructions
import com.slickpath.mobile.android.simple.vm.parser.ParseResult
import com.slickpath.mobile.android.simple.vm.parser.Parser
import com.slickpath.mobile.android.simple.vm.parser.ParserListener
import com.slickpath.mobile.android.simple.vm.util.Command
import com.slickpath.mobile.android.simple.vm.util.CommandList
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

/**
 * Virtual Machine class responsible for executing commands and managing program memory.
 *
 * This class simulates a virtual machine environment, handling instructions, program execution,
 * and interactions with input/output.
 *
 * - Allows caller to pass in streams for both input and output
 * - If outputListener is null output will be added to log is debugVerbose is set
 * - If inputListener is null input will be attempted to be retrieved from the console System.in
 *
 * @property context Android context.
 * @property outputListener Listener for output events. Defaults to null.
 * @property inputListener Listener for input events. Defaults to null.
 *
 * @author Pete Procopio
 */
class VirtualMachine(
    private val context: Context,
    private val outputListener: OutputListener? = null,
    private val inputListener: InputListener? = null
) : VirtualMachineInterface, Machine(outputListener, inputListener), Instructions {

    companion object {
        /**
         * Constant to indicate that a command this value or larger requires a single parameter.
         */
        const val SINGLE_PARAM_COMMAND_START = 1000
        private val LOG_TAG = VirtualMachine::class.java.name
        private val executorPool = Executors.newCachedThreadPool() as ThreadPoolExecutor
    }

    /**
     * Counter for the number of instructions executed.
     */
    private var instructionsRunCount = 0

    /**
     * Listener for VM events.
     */
    var vmListener: VMListener? = null

    var parser: Parser? = null

    init {
        try {
            Class.forName("BaseInstructionSet")
        } catch (e: ClassNotFoundException) {
            debug(LOG_TAG, "BaseInstructionSet class not found " + (e.message ?: "<No Message>"))
        }
    }

    //   EXECUTION
    /**
     * Adds a single command to the program memory.
     *
     * @param command The command to add.
     */
    override fun addCommand(command: Command) {
        setCommandAt(command, programWriterPtr)
    }

    /**
     * Adds a list of commands to the program memory asynchronously.
     *
     * @param commands The list of commands to add.
     */
    override fun addCommands(commands: CommandList?) {
        resetProgramWriter()
        executorPool.execute { doAddInstructions(commands) }
    }

    /**
     * Adds commands from a parser asynchronously.
     *
     * @param parser The parser containing the commands.
     */
    override suspend fun addCommands(parser: Parser) {
        parser.addParserListener(vmParserListener)
        parser.parse()
        this.parser = parser
    }

    /**
     * Listener for parser events.
     */
    private var vmParserListener: ParserListener = object : ParserListener {
        override fun completedParse(parseResult: ParseResult) {
            if (parseResult.vmError == null) {
                addCommands(parseResult.commands)
            }
            vmListener?.completedAddingInstructions(parseResult.vmError, parseResult.commands.size)
            parser?.removeParserListener(this)
        }
    }

    /**
     * Adds a list of commands to the program memory synchronously.
     *
     * @param commands The list of commands to add.
     */
    private fun doAddInstructions(commands: CommandList?) {
        Log.d(LOG_TAG, "^^^^^^^^^^ ADD INSTRUCTIONS START ^^^^^^^^^^")
        var vmError: VMError? = null
        var addedInstructionCount = 0
        if (commands == null) {
            vmError = VMError("Null command list provided to doAddInstructions", VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
        } else {
            addedInstructionCount = commands.size
            commands.forEach { command ->
                addCommand(command)
            }
        }
        vmListener?.completedAddingInstructions(vmError, addedInstructionCount)
        Log.d(LOG_TAG, "^^^^^^^^^^ ADD INSTRUCTIONS END ^^^^^^^^^^")
    }

    /**
     * Executes the next instruction in the program memory synchronously.
     *
     * @return Results containing information about the executed instruction.
     * @throws VMError if a virtual machine error occurs.
     */
    @Throws(VMError::class)
    override fun runNextInstruction(): Results {
        val results = doRunNextInstruction()
        results.vmError?.let { throw it }
        return results
    }

    /**
     * Executes the next instruction in the program memory.
     *
     * @return Results containing information about the executed instruction.
     */
    private fun doRunNextInstruction(): Results {
        Log.d(LOG_TAG, "+doRunNextInstruction $programCounter")
        var hasHalted = false
        var vmError: VMError? = null
        var instructionValue = -1
        try {
            instructionValue = currentInstruction
            runCommand(instructionValue)
        } catch (e: VMError) {
            vmError = e
        }
        if (instructionValue == Instructions.HALT) {
            instructionsRunCount = 0
            resetProgramWriter()
            resetStack()
            hasHalted = true
        }
        return Results(hasHalted, programCounter, vmError)
    }

    /**
     * Data class to hold the results of running an instruction.
     *
     * @property hasHalted Indicates if the HALT instruction was executed.
     * @property lastLineExecuted The program counter value after execution.
     * @property vmError Any VMError that occurred during execution.
     */
    data class Results(
        val halt: Boolean,
        val lastLineExecuted: Int,
        val vmError: VMError?
    )

    /**
     * Executes all remaining instructions in the program memory asynchronously.
     */
    override fun runInstructions() {
        executorPool.execute { doRunInstructions() }
    }

    /**
     * Executes a specified number of instructions in the program memory asynchronously.
     *
     * @param numInstructionsToRun The number of instructions to execute.
     */
    override fun runInstructions(numInstructionsToRun: Int) {
        executorPool.execute { doRunInstructions(numInstructionsToRun) }
    }

    /**
     * Executes a specified number of instructions in the program memory.
     *
     * @param numInstructionsToRun The number of instructions to execute. -1 to run all remaining
     */
    private fun doRunInstructions(numInstructionsToRun: Int = -1) {
        Log.d(LOG_TAG, "++++++++++ RUN INSTRUCTIONS START ++++++++++")
        var vmError: VMError? = null
        dumpMemory("1")
        var numInstructionsRun = 0
        var lastProgramCounter = -1
        var instructionValue: Int = Instructions.BEGIN
        try {
            while (instructionValue != Instructions.HALT &&
                programCounter < MemoryStore.MAX_MEMORY &&
                (numInstructionsRun < numInstructionsToRun || numInstructionsToRun == -1)
            ) {
                lastProgramCounter = programCounter
                numInstructionsRun++
                instructionValue = currentInstruction
                runCommand(instructionValue)
            }
            debug(LOG_TAG, "=========================")
            logExecutionSummary(numInstructionsRun, lastProgramCounter, instructionValue)
            debug(LOG_TAG, "=========================")
        } catch (vme: VMError) {
            debug(LOG_TAG, "=========================")
            debug(LOG_TAG, "VMError=(" + vme.type + ") " + vme.message)
            logExecutionSummary(numInstructionsRun, lastProgramCounter, instructionValue)
            dumpMemory("2")
            vmError = vme
            debug(LOG_TAG, "=========================")
        }
        dumpMemory("3")
        Log.d(LOG_TAG, "+DONE PROCESSING+++++++++")

        vmListener?.completedRunningInstructions(
            instructionValue == Instructions.HALT,
            programCounter,
            vmError
        ) ?: run {
            debug(LOG_TAG, "NO VMListener")
        }
        Log.d(LOG_TAG, "++++++++++ RUN INSTRUCTIONS END ++++++++++")
    }

    /**
     * Logs additional information about the last instruction executed and the program's state.
     *
     * @param numInstructionsRun The number of instructions that were executed.
     * @param lastProgramCounter The last program counter location before exiting.
     * @param lastInstructionValue The value of the last instruction executed.
     */
    private fun logExecutionSummary(
        numInstructionsRun: Int,
        lastProgramCounter: Int,
        lastInstructionValue: Int
    ) {
        val lastInstructionString = getInstructionString(lastInstructionValue)
        debug(LOG_TAG, "LAST INSTRUCTION: ($lastInstructionString) - Value: $lastInstructionValue")
        debug(LOG_TAG, "NUM INSTRUCTIONS RUN=$numInstructionsRun")
        debug(LOG_TAG, "CURRENT PROGRAM CTR=$programCounter")
        debug(LOG_TAG, "LAST PROGRAM CTR=$lastProgramCounter")
    }

    /**
     * Retrieves the string representation of an instruction based on its integer value.
     *
     * @param instructionValue The integer value representing the instruction.
     * @return The string representation of the instruction, or null if the instruction value is not found.
     */
    private fun getInstructionString(instructionValue: Int): String? {
        return BaseInstructionSet.INSTRUCTION_SET_CONV[instructionValue]
    }

    /**
     * Dumps the memory contents to a file for debugging purposes.
     * File name: "memDump_<append>.txt"
     */
    private fun dumpMemory(append: String) {
        if (!debugDump) return

        val filename = "memDump$append.txt"
        val data = StringBuilder()

        var i = 1
        while (i < MemoryStore.MAX_MEMORY) {
            try {
                val parameter = getValueAt(i)
                val command = getValueAt(i - 1)
                data.append("$command $parameter\n")
            } catch (e: VMError) {
                Log.e(LOG_TAG, "Error accessing memory at index: ${i - 1} or $i", e)
            }
            i += 2
        }
        try {
            context.openFileOutput(filename, Context.MODE_PRIVATE).use { fos ->
                fos.write(data.toString().toByteArray())
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Error writing memory dump to file: $filename", e)
        }
    }

    /**
     * Run the selected commandId
     *
     * @param commandId id of command to run
     * @throws VMError VM error on running a command
     */
    @Throws(VMError::class)
    private fun runCommand(commandId: Int) {
        doRunCommandDebug(commandId)
        instructionsRunCount++
        var shouldIncrementProgramCounter = true
        when (commandId) {
            Instructions.ADD -> {
                ADD()
            }

            Instructions.SUB -> {
                SUB()
            }

            Instructions.MUL -> {
                MUL()
            }

            Instructions.DIV -> {
                DIV()
            }

            Instructions.NEG -> {
                NEG()
            }

            Instructions.EQUAL -> {
                EQUAL()
            }

            Instructions.NOTEQL -> {
                NOTEQL()
            }

            Instructions.GREATER -> {
                GREATER()
            }

            Instructions.LESS -> {
                LESS()
            }

            Instructions.GTREQL -> {
                GTREQL()
            }

            Instructions.LSSEQL -> {
                LSSEQL()
            }

            Instructions.NOT -> {
                NOT()
            }

            Instructions.POP -> {
                POP()
            }

            Instructions.JUMP -> {
                JUMP()
                shouldIncrementProgramCounter = false
            }

            Instructions.RDCHAR -> {
                RDCHAR()
            }

            Instructions.RDINT -> {
                RDINT()
            }

            Instructions.WRCHAR -> {
                WRCHAR()
            }

            Instructions.WRINT -> {
                WRINT()
            }

            Instructions.CONTENTS -> {
                CONTENTS()
            }

            Instructions.HALT -> {
                HALT()
            }

            Instructions.PUSHC -> {
                PUSHC(currentParameter)
            }

            Instructions.PUSH -> {
                PUSH(currentParameter)
            }

            Instructions.POPC -> {
                POPC(currentParameter)
            }

            Instructions.BRANCH -> {
                BRANCH(currentParameter)
                shouldIncrementProgramCounter = false
            }

            Instructions.BREQL -> {
                val didBranch = BREQL(currentParameter)
                shouldIncrementProgramCounter = didBranch.not()
            }

            Instructions.BRLSS -> {
                val didBranch = BRLSS(currentParameter)
                shouldIncrementProgramCounter = didBranch.not()
            }

            Instructions.BRGTR -> {
                val didBranch = BRGTR(currentParameter)
                shouldIncrementProgramCounter = didBranch.not()
            }

            else -> throw VMError(
                "BAD runCommand :$commandId",
                VMErrorType.VM_ERROR_TYPE_BAD_UNKNOWN_COMMAND
            )
        }

        if(shouldIncrementProgramCounter) {
            incProgramCounter()
        }
    }

    /**
     * Gets the current instruction ID.
     *
     * @throws VMError if an error occurs while retrieving the instruction.
     */
    private val currentInstruction: Int
        get() {
            val command = getCommandAt(programCounter)
            return command.commandId
        }


    /**
     * Gets the current parameter.
     *
     * @throws VMError if an error occurs while retrieving the parameter.
     */
    private val currentParameter: Int
        get() {
            val command = getCommandAt(programCounter)
            if (command.parameters.isEmpty() && command.commandId >= SINGLE_PARAM_COMMAND_START) {
                throw VMError("No parameters provided for command requiring a parameter.", VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
            } else if (command.parameters.isNotEmpty() && command.commandId < SINGLE_PARAM_COMMAND_START) {
                throw VMError("Too many parameters provided for command.", VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
            }
            return command.parameters[0]
        }

    /**
     * Debug output of runCommand
     *
     * @param commandId The ID of the command to execute.
     * @throws VMError if a VM error occurs during execution.
     */
    @Throws(VMError::class)
    private fun doRunCommandDebug(commandId: Int) {
        debug(LOG_TAG) {
            buildString {
                append("[")
                append(instructionsRunCount)
                append("]")
                append(" Line=")
                append(programCounter)
                append(" CMD=")
                append(getInstructionString(commandId))
                append(" (")
                append(commandId)
                append(")")
                if (commandId >= 1000) {
                    append(" PARAM=")
                    append(currentParameter)
                }
            }
        }
    }
}