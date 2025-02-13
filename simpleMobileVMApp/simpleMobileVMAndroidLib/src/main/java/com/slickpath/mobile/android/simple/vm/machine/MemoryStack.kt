package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.extensions.dumpAsList
import java.util.Stack

class MemoryStack {

    /**
     * Memory Stack
     */
    private val _stack = Stack<Int>()

    /**
     * Returns if Stack is empty
     *
     * @return boolean
     */
    val isEmpty: Boolean
        get() = _stack.isEmpty()

    /**
     * Empty the stack
     */
    fun reset() {
        _stack.clear()
    }

    /**
     * Return stack memory as a List<Integer>
     *
     * @return value at every line of stack memory
    </Integer> */
    fun stackDump(): List<Int> {
        return _stack.dumpAsList()
    }

    /**
     * pop the top value from the stack and return
     * clear out the old top
     *
     * @return value at top of stack
     */
    fun popValue(): Int {
        return _stack.pop()
    }

    /**
     * pop the value onto the stack
     *
     * @param value value to push
     * @return value pushed on the stack
     */
    fun pushValue(value: Int): Int {
        return _stack.push(value)
    }
}