package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.extensions.dumpAsList
import com.slickpath.mobile.android.simple.vm.util.Command
import java.util.*

/**
 * This class is the VM Basic Memory manager
 *
 * @author Pete Procopio
 */
internal class MemoryStore {

    companion object {
        /**
         * Value of memory if it is empty (unused)
         */
        const val EMPTY_MEMORY_VALUE = 999999

        /**
         * Memory consists of N number of sequential Integers
         */
        const val MAX_MEMORY = 500
    }

    /**
     * Memory to store values
     */
    private val _memoryStore: MutableList<Int> = ArrayList(MAX_MEMORY)

    /**
     * Manager and store for Program Memory
     */
    private val _programManager = ProgramManager()


    /**
     * Constructor
     */
    init {
        // initialize every piece of memory to EMPTY
        for (i in 0 until MAX_MEMORY) {
            _memoryStore.add(EMPTY_MEMORY_VALUE)
        }
    }

    /**
     * Return memory as a List<Integer>
     *
     * NOTE: Creates a copy of memory
     *
     * @return value at every line of memory
    </Integer> */
    fun memoryDump(): List<Int> {
        return ArrayList(_memoryStore)
    }

    /**
     * Return program memory as a List<Integer>
     *
     * NOTE: Returns a copy of Program Memory
     *
     * @return Copy of every line of program Memory
    </Integer> */
    fun programMemoryDump(): List<Command> {
        return _programManager.dumpProgramStore()
    }

    /**
     * Return value at location
     *
     * @param location location in memory
     * @return value at location
     */
    operator fun get(location: Int): Int {
        return _memoryStore[location]
    }

    /**
     * Returns the instruction at the specified location
     *
     * @param location location of command in program memory
     * @return command at location
     */
    fun getCommand(location: Int): Command {
        return _programManager.getCommandAt(location)
    }

    /**
     * Set value at location
     *
     * @param location location in memory
     * @param value value to set
     * @return value that was set
     */
    operator fun set(location: Int, value: Int): Int {
        return _memoryStore.set(location, value)
    }

    /**
     * Set instruction value at the specified location
     *
     * @param location location in program memory
     * @param command command to set
     */
    fun setCommand(location: Int, command: Command) {
        _programManager.setCommandAt(location, command)
    }

    /**
     * Get current line in program that will execute next
     *
     * @return int
     */
    /**
     * Set program counter location to indicated location
     * Used primarily for JUMP and BRANCH
     *
     * @param location location the program counter points to
     */
    @Suppress("KDocUnresolvedReference")
    var programCounter: Int
        get() = _programManager.programCounter
        set(location) {
            _programManager.programCounter = location
        }

    /**
     * Increment program counter location to next line of instruction
     */
    fun incProgramCounter() {
        _programManager.incProgramCounter()
    }

    /**
     * Decrement program counter location to previous line of instruction
     */
    fun decProgramCounter() {
        _programManager.decProgramCounter()
    }

    /**
     * program counter location to start of program memory
     */
    fun resetProgramCounter() {
        _programManager.resetProgramCounter()
    }

    /**
     * Get the current location in program memory where the next instruction will be added
     *
     * @return current location in program memory where the next instruction will be added
     */
    val programWriterPtr: Int
        get() = _programManager.programWriterPtr

    /**
     * Increment program writer location to next memory location
     */
    fun incrementProgramWriter() {
        _programManager.incProgramWriter()
    }

    /**
     * Decrement program writer location to previous memory location
     */
    fun resetProgramWriter() {
        _programManager.resetProgramWriter()
    }
}