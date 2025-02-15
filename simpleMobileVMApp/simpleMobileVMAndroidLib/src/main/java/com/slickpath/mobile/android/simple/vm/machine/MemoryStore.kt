package com.slickpath.mobile.android.simple.vm.machine

/**
 * This class represents a basic memory store for a virtual machine.
 *
 * It manages a fixed-size block of memory, providing read and write access to individual memory locations.
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
    private val memoryStore: MutableList<Int> = MutableList(MAX_MEMORY) { EMPTY_MEMORY_VALUE }

    /**
     * Return memory as a List<Integer>
     *
     * NOTE: Creates a copy of memory
     *
     * @return value at every line of memory
    </Integer> */
    fun memoryDump(): List<Int> {
        return memoryStore.toList()
    }

    /**
     * Retrieves the value stored at the specified memory location.
     *
     * @param location The index of the memory location to read from
     * @return value at location
     * @throws IndexOutOfBoundsException if the location is outside the valid memory range.
     */
    operator fun get(location: Int): Int {
        require(location in 0 until MAX_MEMORY) { "Invalid get memory location: $location. Must be between 0 and ${MAX_MEMORY - 1}." }
        return memoryStore[location]
    }

    /**
     * Stores a value at the specified memory location.
     *
     * @param location The index of the memory location to write to.
     * @param value value to set
     * @return value that was set
     * @throws IndexOutOfBoundsException if the location is outside the valid memory range.
     */
    operator fun set(location: Int, value: Int): Int {
        require(location in 0 until MAX_MEMORY) { "Invalid set memory location: $location. Must be between 0 and ${MAX_MEMORY - 1}." }
        return memoryStore.set(location, value)
    }
}