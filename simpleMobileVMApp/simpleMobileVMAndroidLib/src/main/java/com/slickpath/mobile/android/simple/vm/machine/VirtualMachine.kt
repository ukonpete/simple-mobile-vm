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
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

/**
 * @author Pete Procopio
 *
 *
 * Constructor:
 * - Allows caller to pass in streams for both input and output
 * - If outputListener is null output will be added to log is debugVerbose is set
 * - If inputListener is null input will be attempted to be retrieved from the console System.in
 *
 * @param context context object
 * @param outputListener listener for output events
 * @param inputListener listener to return input on input events
 */
class VirtualMachine constructor(
    private val context: Context,
    private val outputListener: OutputListener? = null,
    private val inputListener: InputListener? = null
) : VirtualMachineInterface, Machine(outputListener, inputListener), Instructions {

    companion object {
        const val SINGLE_PARAM_COMMAND_START = 1000
        private val LOG_TAG = VirtualMachine::class.java.name
        private val executorPool = Executors.newCachedThreadPool() as ThreadPoolExecutor
    }

    private var numInstructionsRun = 0

    /**
     * listener to listen to events thrown by VM
     *
     * @param listener listener on vm events
     */
    var vmListener: VMListener? = null

    var parser: Parser? = null

    init {
        try {
            Class.forName("BaseInstructionSet")
        } catch (e: ClassNotFoundException) {
            debug(LOG_TAG, e.message ?: "<No Message>")
        }
    }

    //   EXECUTION
    /**
     * Add the contents of a Command object to the VM
     *
     *
     * Basically write the command id and its parameter(s) into the program memory space
     *
     * @param command command to add
     */
    override fun addCommand(command: Command) {
        setCommandAt(command, programWriterPtr)
    }

    /**
     * Launch thread that will add all the Commands in the CommandList to the VM
     * will call completedAddingInstructions on VMListener after completion
     *
     * @param commands command container
     */
    override fun addCommands(commands: CommandList?) {
        resetProgramWriter()
        executorPool.execute { doAddInstructions(commands) }
    }

    override fun addCommands(parser: Parser) {
        parser.addParserListener(vmParserListener)
        parser.parse()
        this.parser = parser
    }

    private var vmParserListener: ParserListener = object : ParserListener {
        override fun completedParse(parseResult: ParseResult) {
            if(parseResult.vmError == null) {
                addCommands(parseResult.commands)
            }
            vmListener?.completedAddingInstructions(parseResult.vmError, parseResult.commands.size )
            parser?.removeParserListener(this)
        }
    }

    /**
     * Add all the Commands in the CommandList to the VM
     * will call completedAddingInstructions on VMListener after completion
     *
     * @param commands commands to add
     */
    private fun doAddInstructions(commands: CommandList?) {
        Log.d(LOG_TAG, "^^^^^^^^^^ ADD INSTRUCTIONS START ^^^^^^^^^^")
        var vmError: VMError? = null
        var numInstructionsAdded = 0
        if (commands != null) {
            val numCommands = commands.size
            for (i in 0 until numCommands) {
                addCommand(commands[i])
                numInstructionsAdded++
            }
        } else {
            vmError = VMError("addInstructions instructions", VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
        }
        vmListener?.completedAddingInstructions(vmError, numInstructionsAdded)
        Log.d(LOG_TAG, "^^^^^^^^^^ ADD INSTRUCTIONS END ^^^^^^^^^^")
    }

    /**
     * Will run the instruction the program pointer is pointing at.
     * This is a synchronous call and will not call into the completedAddingInstructions
     *
     * @return instruction was a halt
     * @throws VMError on a VM error
     */
    @Throws(VMError::class)
    override fun runNextInstruction(): Results {
        val results = doRunNextInstruction()
        if (results.vmError != null) {
            throw results.vmError
        }
        return results
    }

    /**
     * Run the instruction the program pointer is pointing at
     * will call completedRunningInstruction on VMListener after completion
     */
    private fun doRunNextInstruction(): Results {
        Log.d(LOG_TAG, "+doRunNextInstruction $programCounter")
        var bHalt = false
        var vmError: VMError? = null
        var instructionVal = -1
        try {
            instructionVal = instruction
            runCommand(instructionVal)
        } catch (e: VMError) {
            vmError = e
        }
        if (instructionVal == Instructions.HALT) {
            numInstructionsRun = 0
            resetProgramWriter()
            resetStack()
            bHalt = true
        }
        return Results(bHalt, programCounter, vmError)
    }

    data class Results constructor(val halt: Boolean, val lastLineExecuted: Int, val vmError: VMError?)

    /**
     * Launches thread that does - Run all remaining instructions - starting from current program ptr location
     * will call completedRunningInstructions on VMListener after completion
     */
    override fun runInstructions() {
        executorPool.execute { doRunInstructions() }
    }

    /**
     * Launch thread that will Run N number of instructions - starting from current program ptr location
     * will call completedRunningInstructions on VMListener after completion
     *
     * @param numInstructionsToRun number of instructions to run until running stops
     */
    override fun runInstructions(numInstructionsToRun: Int) {
        executorPool.execute { doRunInstructions(numInstructionsToRun) }
    }
    /**
     * Run N number of instructions - starting from current program ptr location
     * will call completedRunningInstructions on VMListener after completion
     *
     * @param numInstructionsToRun number of instructions to run until running stops
     */
    private fun doRunInstructions(numInstructionsToRun: Int = -1) {
        Log.d(LOG_TAG, "++++++++++ RUN INSTRUCTIONS START ++++++++++")
        var vmError: VMError? = null
        dumpMem("1")
        var numInstructionsRun = 0
        var lastProgramCounter = -1
        var instructionVal: Int = Instructions.BEGIN
        try {
            while (instructionVal != Instructions.HALT &&
                programCounter < MemoryStore.MAX_MEMORY &&
                (numInstructionsRun < numInstructionsToRun || numInstructionsToRun == -1)
            ) {
                lastProgramCounter = programCounter
                numInstructionsRun++
                instructionVal = instruction
                runCommand(instructionVal)
            }
            debug(LOG_TAG, "=========================")
            logAdditionalInfo(numInstructionsRun, lastProgramCounter, instructionVal)
            debug(LOG_TAG, "=========================")
        } catch (vme: VMError) {
            debug(LOG_TAG, "=========================")
            debug(LOG_TAG, "VMError=(" + vme.type + ") " + vme.message)
            logAdditionalInfo(numInstructionsRun, lastProgramCounter, instructionVal)
            dumpMem("2")
            vmError = vme
            debug(LOG_TAG, "=========================")
        }
        dumpMem("3")
        Log.d(LOG_TAG, "+DONE PROCESSING+++++++++")

        vmListener?.completedRunningInstructions(
            instructionVal == Instructions.HALT,
            programCounter,
            vmError
        ) ?: run {
            debug(LOG_TAG, "NO VMListener")
        }
        Log.d(LOG_TAG, "++++++++++ RUN INSTRUCTIONS END ++++++++++")
    }

    /**
     * @param numInstructionsRun   number of instructions that were run
     * @param lastProgramCounter    last program counter location
     * @param instructionVal last instruction value
     */
    private fun logAdditionalInfo(
        numInstructionsRun: Int, lastProgramCounter: Int,
        instructionVal: Int
    ) {
        debug(
            LOG_TAG,
            "LAST_INSTRUCTION=(" + getInstructionString(instructionVal) + ") " + instructionVal
        )
        debug(LOG_TAG, "NUM INSTRUCTIONS RUN=$numInstructionsRun")
        debug(LOG_TAG, "PROG_CTR=$programCounter")
        debug(LOG_TAG, "LAST_PROG_CTR=$lastProgramCounter")
    }

    /**
     * @param instructionVal instruction val
     * @return instruction string representation
     */
    private fun getInstructionString(instructionVal: Int): String? {
        return BaseInstructionSet.INSTRUCTION_SET_CONV[instructionVal]
    }

    /**
     * Dump the memory into a file for debugging purposes
     * file name : "memDump<sAppend>.text
    </sAppend> */
    private fun dumpMem(append: String) {
        if (debugDump) {
            val filename = "memDump$append.txt"
            val data = StringBuilder()
            var i = 1
            while (i < MemoryStore.MAX_MEMORY) {
                try {
                    val parameter = getValueAt(i)
                    val command = getValueAt(i - 1)
                    data.append(command)
                        .append(' ')
                        .append(parameter)
                        .append("\r\n")
                } catch (e: VMError) {
                    e.printStackTrace()
                }
                i += 2
            }
            var fos: FileOutputStream? = null
            try {
                fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
                fos.write(data.toString().toByteArray())
            } catch (e: IOException) {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e1: IOException) {
                        Log.w(LOG_TAG, "unable to fo final close", e1)
                    }
                }
            }
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
        if (debugVerbose) {
            doRunCommandDebug(commandId)
        }
        numInstructionsRun++
        val bBranched: Boolean
        when (commandId) {
            Instructions.ADD -> {
                ADD()
                incProgramCounter()
            }
            Instructions.SUB -> {
                SUB()
                incProgramCounter()
            }
            Instructions.MUL -> {
                MUL()
                incProgramCounter()
            }
            Instructions.DIV -> {
                DIV()
                incProgramCounter()
            }
            Instructions.NEG -> {
                NEG()
                incProgramCounter()
            }
            Instructions.EQUAL -> {
                EQUAL()
                incProgramCounter()
            }
            Instructions.NOTEQL -> {
                NOTEQL()
                incProgramCounter()
            }
            Instructions.GREATER -> {
                GREATER()
                incProgramCounter()
            }
            Instructions.LESS -> {
                LESS()
                incProgramCounter()
            }
            Instructions.GTREQL -> {
                GTREQL()
                incProgramCounter()
            }
            Instructions.LSSEQL -> {
                LSSEQL()
                incProgramCounter()
            }
            Instructions.NOT -> {
                NOT()
                incProgramCounter()
            }
            Instructions.POP -> {
                POP()
                incProgramCounter()
            }
            Instructions.JUMP -> JUMP()
            Instructions.RDCHAR -> {
                RDCHAR()
                incProgramCounter()
            }
            Instructions.RDINT -> {
                RDINT()
                incProgramCounter()
            }
            Instructions.WRCHAR -> {
                WRCHAR()
                incProgramCounter()
            }
            Instructions.WRINT -> {
                WRINT()
                incProgramCounter()
            }
            Instructions.CONTENTS -> {
                CONTENTS()
                incProgramCounter()
            }
            Instructions.HALT -> {
                HALT()
                incProgramCounter()
            }
            Instructions.PUSHC -> {
                PUSHC(parameter)
                incProgramCounter()
            }
            Instructions.PUSH -> {
                PUSH(parameter)
                incProgramCounter()
            }
            Instructions.POPC -> {
                POPC(parameter)
                incProgramCounter()
            }
            Instructions.BRANCH -> BRANCH(parameter)
            Instructions.BREQL -> {
                bBranched = BREQL(parameter)
                if (!bBranched) {
                    incProgramCounter()
                }
            }
            Instructions.BRLSS -> {
                bBranched = BRLSS(parameter)
                if (!bBranched) {
                    incProgramCounter()
                }
            }
            Instructions.BRGTR -> {
                bBranched = BRGTR(parameter)
                if (!bBranched) {
                    incProgramCounter()
                }
            }
            else -> throw VMError(
                "BAD runCommand :$commandId",
                VMErrorType.VM_ERROR_TYPE_BAD_UNKNOWN_COMMAND
            )
        }
    }

    @get:Throws(VMError::class)
    private val instruction: Int
        get() {
            val command = getCommandAt(programCounter)
            return command.commandId
        }

    @get:Throws(VMError::class)
    private val parameter: Int
        get() {
            val command = getCommandAt(programCounter)
            if (command.parameters.isEmpty() && command.commandId >= SINGLE_PARAM_COMMAND_START) {
                throw VMError("No Parameters", VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
            } else if (command.parameters.isNotEmpty() && command.commandId < SINGLE_PARAM_COMMAND_START) {
                throw VMError("Too many Parameters", VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
            }
            return command.parameters[0]
        }

    /**
     * Debug output of runCommand
     *
     * @param commandId id of command to run in debug
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    private fun doRunCommandDebug(commandId: Int) {
        val lineCount = StringBuilder("[")
        lineCount.append(numInstructionsRun)
            .append("]")
            .append(" Line=")
            .append(programCounter - 1)
            .append(" CMD=")
            .append(getInstructionString(commandId))
            .append(" (")
            .append(commandId)
            .append(")")
        if (commandId >= 1000) {
            lineCount.append(" PARAM=")
                .append(parameter)
        }
        debug(LOG_TAG, lineCount.toString())
    }
}