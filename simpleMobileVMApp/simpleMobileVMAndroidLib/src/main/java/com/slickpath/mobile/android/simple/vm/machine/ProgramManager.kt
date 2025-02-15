package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.machine.MemoryStore.Companion.EMPTY_MEMORY_VALUE
import com.slickpath.mobile.android.simple.vm.machine.MemoryStore.Companion.MAX_MEMORY
import com.slickpath.mobile.android.simple.vm.util.Command

/**
 * Manages the program memory, storing instructions and providing access to them.
 *
 * This class handles the memory used to store program instructions and parameters. It manages
 * the program counter, program writer pointer, and provides methods to access and modify
 * the program store.
 *
 * @author Pete Procopio
 */
internal class ProgramManager {

    companion object {
        /**
         * The starting location of the program in memory (when the stack is considered empty).
         */
        const val START_LOCATION = 0
    }

    /**
     * The actual memory store for the program instructions.
     */
    private val programStore = MutableList(MAX_MEMORY) { Command(EMPTY_MEMORY_VALUE, null) }

    /**
     * The current instruction being pointed to.
     */
    private var programCounter = START_LOCATION

    /**
     * The next available location for writing an instruction.
     */
    var programWriterPointer = START_LOCATION
        private set

    /**
     * Sets the program counter to the specified location.
     *
     * @param location The new location for the program counter.
     * @throws IllegalArgumentException if the location is out of bounds.
     */
    fun setProgramCounter(location: Int) {
        require(location in 0 until MAX_MEMORY) { "Program counter location out of bounds: $location" }
        programCounter = location
    }

    /**
     * Gets the current location of the program counter.
     *
     * @return The current location of the program counter.
     */
    fun getProgramCounter(): Int {
        return programCounter
    }

    /**
     * Increments the program counter to the next instruction.
     *
     * @throws IllegalStateException if the program counter is at the end of memory.
     */
    fun incProgramCounter() {
        check(programCounter < MAX_MEMORY) { "Program counter cannot be incremented past the end of memory" }
        programCounter++
    }

    /**
     * Decrements the program counter to the previous instruction.
     *
     * @throws IllegalStateException if the program counter is at the beginning of memory.
     */
    fun decProgramCounter() {
        check(programCounter > 0) { "Program counter cannot be decremented past the beginning of memory" }
        programCounter--
    }

    /**
     * Resets the program counter to the start of the program.
     */
    fun resetProgramCounter() {
        programCounter = START_LOCATION
    }

    /**
     * Increments the program writer pointer to the next available location.
     *
     * @throws IllegalStateException if the program writer pointer is at the end of memory.
     */
    fun incrementProgramWriter() {
        programWriterPointer++
    }

    /**
     * Reset program writer location to starting memory location
     */
    fun resetProgramWriter() {
        check(programWriterPointer < MAX_MEMORY - 1) { "Program writer pointer cannot be incremented past the end of memory" }
        programWriterPointer = START_LOCATION
    }

    /**
     * Returns a copy of the program store.
     *
     * @return A copy of the program memory.
     */
    fun dumpProgramStore(): List<Command> {
        return programStore.toList()
    }

    /**
     * Returns the instruction at the specified location.
     *
     * @param location The location in the program store.
     * @return The command at the specified location.
     * @throws IllegalArgumentException if the location is out of bounds.
     */
    fun getCommandAt(location: Int): Command {
        return programStore[location]
    }

    /**
     * Set instruction value at the specified location
     *
     * @param location location in program store
     * @param command command that will be set
     */
    fun setCommandAt(location: Int, command: Command) {
        require(location in 0 until MAX_MEMORY) { "Set Location out of bounds: $location" }
        programStore[location] = command
    }

}