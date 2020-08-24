package com.slickpath.mobile.android.simple.vm.machine

import android.util.Log
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.VMErrorType
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet
import com.slickpath.mobile.android.simple.vm.util.Command

/**
 * Kernel level call for the VM
 *
 * @author Pete Procopio
 */
open class Kernel {

    companion object {
        private val LOG_TAG = Machine::class.java.name
        const val PUSHC_YES = 1
        const val PUSHC_NO = 0
    }

    var debugDump = false

    /**
     * @return is debug verbose enabled
     */
    var debugVerbose = true

    /**
     * Toggle if debug will be enabled
     * If enabled certain methods will log to Android Log.d
     * Also any output commands (example WRCHR and WRINT) will Log.d the output
     */
    var debug = true

    private val memory = Memory()

    /**
     * Get current line in program that will execute next
     *
     * @return int
     */
    val programCounter: Int
        get() = memory.programCounter

    /**
     * Returns the value at a specified memory location
     *
     * @param location - in memory to return
     * @return int - value at specified memory location
     * @throws VMError error in VM
     */
    @Throws(VMError::class)
    fun getValueAt(location: Int): Int {
        return if (location < Memory.MAX_MEMORY) {
            memory[location]
        } else {
            throw VMError("getValueAt : $location", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT)
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
        return if (location < Memory.MAX_MEMORY) {
            memory.set(location, value)
        } else {
            throw VMError("setValAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT)
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
        return if (!memory.isStackEmpty) {
            memory.popMEM()
        } else {
            throw VMError("_pop", VMErrorType.VM_ERROR_TYPE_STACK_LIMIT)
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
            memory.pushMEM(value)
        } catch (e: Exception) {
            throw VMError("PUSHC", e, VMErrorType.VM_ERROR_TYPE_STACK_LIMIT)
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
        if (location <= Memory.MAX_MEMORY) {
            memory.programCounter = location
        } else {
            throw VMError("BRANCH : loc=$location", VMErrorType.VM_ERROR_TYPE_STACK_LIMIT)
        }
        debugVerbose(LOG_TAG, "--BR=" + memory.programCounter)
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
        memory.programCounter = location
    }

    /**
     * If debugVerbose is enabled write the output to a log
     *
     *
     * This allows sub-classes of this class to do logging without having to implement it.
     *
     * @param tag  - the Log.d LOG_TAG to use
     * @param text test to log
     */
    fun debugVerbose(tag: String?, text: String) {
        if (debugVerbose) {
            debug(tag, text)
        }
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
    fun debug(tag: String?, text: String) {
        if (debug) {
            Log.d(tag, text)
        }
    }

    /**
     * Increment program counter location to next line of instruction
     */
    fun incProgramCounter() {
        memory.incProgramCounter()
    }

    /**
     * Decrement program counter location to previous line of instruction
     */
    fun decProgramCounter() {
        memory.decProgramCounter()
    }

    /**
     * program counter location to start of program memory
     */
    fun resetProgramCounter() {
        memory.resetProgramCounter()
    }

    /**
     * Get the current location in program memory where the next instruction will be added
     *
     * @return current location in program memory where the next instruction will be added
     */
    val programWriterPtr: Int
        get() = memory.programWriterPtr

    /**
     * Increment program writer location to next memory location
     */
    fun incrementProgramWriter() {
        memory.incrementProgramWriter()
    }

    /**
     * Reset program writer location to starting memory location
     */
    fun resetProgramWriter() {
        memory.resetProgramWriter()
    }

    /**
     * Empty the stack
     */
    fun resetStack() {
        memory.resetStack()
    }

    fun reset() {
        memory.incrementProgramWriter()
        memory.resetProgramWriter()
        memory.resetStack()
    }

    /**
     * Gets the instruction and parameters at the requested location and puts it in a list
     *
     * @param location location in memory
     * @return Command at location
     * @throws VMError error in VM
     */
    @Throws(VMError::class)
    fun getCommandAt(location: Int): Command {
        return if (location < Memory.MAX_MEMORY) {
            val command = memory.getCommand(location)
            val instruction = command.commandId
            val parameterCount = command.parameters.size
            if (debug) {
                if (parameterCount > 0) {
                    Log.d(LOG_TAG, "Get Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT[instruction] + ") " + instruction + " param(0) " + command.parameters[0] + " at " + location)
                } else {
                    Log.d(LOG_TAG, "Get Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT[instruction] + ") " + instruction + " NO param at " + location)
                }
            }
            command
        } else {
            throw VMError("getCommandAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT)
        }
    }

    /**
     * Takes an instruction and parameters and stores it in the requested location
     *
     * @param command  the command to set
     * @param location location in memory
     */
    fun setCommandAt(command: Command, location: Int) {
        if (location < Memory.MAX_MEMORY) {
            if (debug) {
                var paramInfo = "<null>"
                if (command.parameters.isNotEmpty()) {
                    paramInfo = command.parameters[0].toString()
                }
                Log.d(LOG_TAG, "Set Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT[command.commandId] + ") " + command.commandId + " param " + paramInfo + " at " + location)
            }
            memory.setCommand(location, command)
            incrementProgramWriter()
        }
    }

    /**
     * Dump the memory as a list
     *
     * @return List<Integer> list of each each value of memory
    </Integer> */
    fun dumpMemory(): List<Int> {
        return memory.memoryDump()
    }

    /**
     * Returns the stack as a List, where the 1st element in the list is at the bottom of the stack
     *
     * @return List<Integer> list of each each value of memory in the stack
    </Integer> */
    fun dumpStack(): List<Int> {
        return memory.stackDump()
    }

    /**
     * Returns all the Instruction/Parameter lists as one list
     *
     * @return List<Integer> list of each each value of memory for instructions
    </Integer> */
    fun dumpInstructionMemory(): List<Command> {
        return memory.programMemoryDump()
    }
}