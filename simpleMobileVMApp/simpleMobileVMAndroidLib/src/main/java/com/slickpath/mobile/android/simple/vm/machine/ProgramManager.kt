package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.util.Command

/**
 * This class abstracts the handling of the memory used to store program instructions and parameters
 *
 * @author Pete Procopio
 */
internal class ProgramManager {

    companion object {
        /**
         * Location in memory of stack pointer when stack is empty
         */
        const val START_LOC = 0

        /**
         * Value of memory if it is empty (unused)
         */
        private const val EMPTY_MEMORY_VALUE = 999999

        /**
         * Memory consists of N number of sequential Integers
         */
        private const val MAX_MEMORY = 500
    }

    /**
     * Actually store we use for memory
     */
    private val _programStore: MutableList<Command> = ArrayList(MAX_MEMORY)

    /**
     * Keeps track of location of next instruction to run
     */
    private var _programCounter = START_LOC

    /**
     * Keeps track of next location where an instruction to run can be written to- part of program setup
     */
    var programWriterPtr = START_LOC
        private set

    init {
        // initialize every piece of instruction memory to EMPTY
        for (i in 0 until MAX_MEMORY) {
            _programStore.add(Command(EMPTY_MEMORY_VALUE, null))
        }
    }

    fun setProgramCounter(location: Int) {
        _programCounter = location
    }

    fun getProgramCounter(): Int {
        return _programCounter
    }

    /**
     * Increment program counter location to next line of instruction
     */
    fun incProgramCounter() {
        _programCounter++
    }

    /**
     * Decrement program counter location to previous line of instruction
     */
    fun decProgramCounter() {
        _programCounter--
    }

    /**
     * program counter location to start of program memory
     */
    fun resetProgramCounter() {
        _programCounter = START_LOC
    }

    /**
     * Increment program writer location to previous memory location
     */
    fun incrementProgramWriter() {
        programWriterPtr++
    }

    /**
     * Reset program writer location to starting memory location
     */
    fun resetProgramWriter() {
        programWriterPtr = START_LOC
    }

    /**
     * Returns the instructions as a list
     *
     *
     * NOTE: This makes a copy of the program memory
     *
     * @return Copy of memory
     */
    fun dumpProgramStore(): List<Command> {
        return ArrayList(_programStore)
    }

    /**
     * Returns the instruction at the specified location
     *
     * @param location location in program store
     * @return command at location
     */
    fun getCommandAt(location: Int): Command {
        return _programStore[location]
    }

    /**
     * Set instruction value at the specified location
     *
     * @param location location in program store
     * @param command command that will be set
     */
    fun setCommandAt(location: Int, command: Command) {
        _programStore[location] = command
    }

}