package com.slickpath.mobile.android.simple.vm.machine

/**
 * This class is the VM Basic Memory Store
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
     * Return value at location
     *
     * @param location location in memory
     * @return value at location
     */
    operator fun get(location: Int): Int {
        return _memoryStore[location]
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
}