package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.VMErrorType
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet
import com.slickpath.mobile.android.simple.vm.util.Command
import java.util.EmptyStackException

/**

 *
 * @author Pete Procopio
 */
open class Kernel : IKernel, IDebugVerboseLogger {

    companion object {
        private val LOG_TAG = Machine::class.java.simpleName
    }

    override var debugDump: Boolean = false

    private val debugVerboseLogger = DebugVerboseLogger(
        /**
         * Toggle if debug will be enabled
         * If enabled certain methods will log to Android Log.d
         * Also any output commands (example WRCHR and WRINT) will Log.d the output
         */
        debug = true, // is debug verbose enabled
        /**
         * @return is debug verbose enabled
         */
        debugVerbose = false
    )

    private val memoryStore = MemoryStore()

    private val memoryStack = MemoryStack()

    /**
     * Manager and store for Program Memory
     */
    private val programManager = ProgramManager()

    /**
     * Get current line in program that will execute next
     *
     * @return int
     */
    override val programCounter: Int
        get() = programManager.getProgramCounter()

    /**
     * Returns the value at a specified memory location
     *
     * @param location - in memory to return
     * @return int - value at specified memory location
     * @throws VMError error in VM
     */
    @Throws(VMError::class)
    override fun getValueAt(location: Int): Int {
        return if (location in 0 until MemoryStore.MAX_MEMORY) {
            memoryStore[location]
        } else {
            if (location < 0) {
                throw VMError("getCommandAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT_MIN)
            } else {
                throw VMError("getCommandAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT_MAX)
            }
        }
    }

    /**
     * Set value at location specified
     *
     * @param value value to be set
     * @param location location in memory
     * @return Integer (previous value) - @see Java.util.List.set()
     * @throws VMError error in VM
     */
    @Throws(VMError::class)
    fun setValueAt(value: Int, location: Int): Int {
        return if (location in 0 until MemoryStore.MAX_MEMORY) {
            memoryStore.set(location, value)
        } else {
            if (location < 0) {
                throw VMError("getCommandAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT_MIN)
            } else {
                throw VMError("getCommandAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT_MAX)
            }
        }
    }

    /**
     * Return the value at the current stack pointer, remove item from stack (decrement stack pointer)
     *
     *
     * The value at the location of the stack pointer at the time of the call will be cleaned
     *
     * @return int - value at top of stack
     * @throws VMError error in VM
     */
    @Throws(VMError::class)
    fun pop(): Int {
        return try {
            memoryStack.popValue()
        } catch (e: EmptyStackException) {
            throw VMError("_pop", VMErrorType.VM_ERROR_TYPE_STACK_EMPTY)
        }
    }

    /**
     * Push the indicated value to the top of the stack (increment stack pointer then add item at the new stack pointer location)
     *
     * @param value value to push
     * @throws VMError error in VM
     */
    @Throws(VMError::class)
    fun push(value: Int) {
        try {
            memoryStack.pushValue(value)
        } catch (e: Exception) {
            throw VMError("PUSHC", e, VMErrorType.VM_ERROR_TYPE_STACK_GENERAL)
        }
    }

    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that is passed in
     *
     * @param location location in memory
     * @throws VMError error in VM
     */
    @Throws(VMError::class)
    fun branch(location: Int) {
        if (location in 0 until MemoryStore.MAX_MEMORY) {
            programManager.setProgramCounter(location)
        } else {
            if (location < 0) {
                throw VMError("BRANCH : loc=$location", VMErrorType.VM_ERROR_TYPE_STACK_LIMIT_MIN)
            } else {
                throw VMError("BRANCH : loc=$location", VMErrorType.VM_ERROR_TYPE_STACK_LIMIT_MAX)
            }
        }
        debugVerbose(LOG_TAG, "--BR=" + programManager.getProgramCounter())
    }

    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that the top of the stack indicates.
     * The value used is popped from the stack
     *
     * @throws VMError error in VM
     */
    @Throws(VMError::class)
    fun jump() {
        val location = pop()
        programManager.setProgramCounter(location)
    }

    /**
     * If debugVerbose is enabled write the output to a log
     *
     *
     * This allows sub-classes of this class to do logging without having to implement it.
     *
     * @param tag  - the Log.d LOG_TAG to use
     * @param text text to log is debugVerbose is enabled
     */
    override fun debugVerbose(tag: String, text: String) {
        debugVerboseLogger.debugVerbose(tag, text)
    }

    /**
     * If debugVerbose is enabled write the output to a log
     *
     *
     * This allows sub-classes of this class to do logging without having to implement it.
     *
     * @param tag  - the Log.d LOG_TAG to use
     * @param text callback for text to log is debugVerbose is enabled
     */
    override fun debugVerbose(tag: String, text: () -> String) {
        debugVerboseLogger.debugVerbose(tag, text())
    }

    /**
     * If debug is enabled write the output to a log
     *
     *
     * This allows sub-classes of this class to do logging without having to implement it.
     *
     * @param tag  the Log.d LOG_TAG to use
     * @param text string to log if debug is enabled
     */
    override fun debug(tag: String, text: String) {
        debugVerboseLogger.debug(tag, text)
    }

    /**
     * If debug is enabled write the output to a log
     *
     *
     * This allows sub-classes of this class to do logging without having to implement it.
     *
     * @param tag  the Log.d LOG_TAG to use
     * @param text callback for string to log if debug is enabled
     */
    override fun debug(tag: String, text: () -> String) {
        debugVerboseLogger.debug(tag, text)
    }

    /**
     * Increment program counter location to next line of instruction
     */
    override fun incrementProgramCounter() {
        programManager.incProgramCounter()
    }

    /**
     * Decrement program counter location to previous line of instruction
     */
    override fun decrementProgramCounter() {
        programManager.decProgramCounter()
    }

    /**
     * program counter location to start of program memory
     */
    override fun resetProgramCounter() {
        programManager.resetProgramCounter()
    }

    /**
     * Get the current location in program memory where the next instruction will be added
     *
     * @return current location in program memory where the next instruction will be added
     */
    val programWriterPtr: Int
        get() = programManager.programWriterPointer

    /**
     * Increment program writer location to next memory location
     */
    fun incrementProgramWriter() {
        programManager.incrementProgramWriter()
    }

    /**
     * Reset program writer location to starting memory location
     */
    override fun resetProgramWriter() {
        programManager.resetProgramWriter()
    }

    /**
     * Empty the stack
     */
    override fun resetStack() {
        memoryStack.reset()
    }

    override fun reset() {
        programManager.incrementProgramWriter()
        programManager.resetProgramWriter()
        resetStack()
    }

    /**
     * Gets the instruction and parameters at the requested location and puts it in a list
     *
     * @param location location in memory
     * @return Command at location
     * @throws VMError error in VM
     */
    @Throws(VMError::class)
    override fun getCommandAt(location: Int): Command {
        return if (location in 0 until MemoryStore.MAX_MEMORY) {
            val command = programManager.getCommandAt(location)
            val instruction = command.commandId
            val parameterCount = command.parameters.size
            debug(LOG_TAG) {
                if (parameterCount > 0) {
                    "Get Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV[instruction] + ") " + instruction + " param(0) " + command.parameters[0] + " at " + location
                } else {
                    "Get Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV[instruction] + ") " + instruction + " NO param at " + location
                }
            }
            command
        } else {
            if (location < 0) {
                throw VMError("getCommandAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT_MIN)
            } else {
                throw VMError("getCommandAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT_MAX)
            }
        }
    }

    /**
     * Takes an instruction and parameters and stores it in the requested location
     *
     * @param command  the command to set
     * @param location location in memory
     */
    fun setCommandAt(command: Command, location: Int) {
        if (location in 0 until MemoryStore.MAX_MEMORY) {
            debug(LOG_TAG) {
                var paramInfo = "<null>"
                if (command.parameters.isNotEmpty()) {
                    paramInfo = command.parameters[0].toString()
                }
                "  Set CMD : ${BaseInstructionSet.INSTRUCTION_SET_CONV[command.commandId]} (${command.commandId}) PARAM $paramInfo at loc $location"
            }
            programManager.setCommandAt(location, command)
            incrementProgramWriter()
        } else {
            if (location < 0) {
                throw VMError("setCommandAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT_MIN)
            } else {
                throw VMError("setCommandAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT_MAX)
            }
        }
    }

    /**
     * Dump the memory as a list
     *
     * @return List<Integer> list of each each value of memory
    </Integer> */
    override fun dumpMemory(): List<Int> {
        return memoryStore.memoryDump()
    }

    /**
     * Returns the stack as a List, where the 1st element in the list is at the bottom of the stack
     *
     * @return List<Integer> list of each each value of memory in the stack
    </Integer> */
    override fun dumpStack(): List<Int> {
        return memoryStack.dump()
    }

    /**
     * Returns all the Instruction/Parameter lists as one list
     *
     * @return List<Integer> list of each each value of memory for instructions
    </Integer> */
    fun dumpInstructionMemory(): List<Command> {
        return programManager.dumpProgramStore()
    }

    override fun addCommand(command: Command) {
        setCommandAt(command, programWriterPtr)
    }
}