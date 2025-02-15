package com.slickpath.mobile.android.simple.vm.machine

import java.util.EmptyStackException

class MemoryStack {

    /**
     * Internal stack to store integers.
     * Using ArrayDeque for better performance compared to Stack in most cases.
     */
    private val stack: ArrayDeque<Int> = ArrayDeque()

    /**
     * Returns if Stack is empty
     *
     * @return boolean
     */
    val isEmpty: Boolean
        get() = stack.isEmpty()

    /**
     * Empty the stack
     */
    fun reset() {
        stack.clear()
    }

    /**
     * Return stack memory as a List<Integer>
     *
     * @return value at every line of stack memory
    </Integer> */
    fun dump(): List<Int> {
        return stack.toList()
    }

    /**
     * pop the top value from the stack and return
     * clear out the old top
     *
     * @return The value at the top of the stack.
     * @throws EmptyStackException if the stack is empty.
     */
    fun popValue(): Int {
        if (stack.isEmpty()) {
            throw EmptyStackException()
        }
        return stack.removeLast()
    }

    /**
     * pop the value onto the stack
     *
     * @param value value to push
     * @return value pushed on the stack
     */
    fun pushValue(value: Int): Int {
        stack.addLast(value)
        return value
    }

    /**
     * Returns the top element from the stack without removing it.
     *
     * @return The value at the top of the stack.
     * @throws EmptyStackException if the stack is empty.
     */
    @Suppress("unused")
    fun peek(): Int {
        if(stack.isEmpty()){
            throw EmptyStackException()
        }
        return stack.last()
    }
}