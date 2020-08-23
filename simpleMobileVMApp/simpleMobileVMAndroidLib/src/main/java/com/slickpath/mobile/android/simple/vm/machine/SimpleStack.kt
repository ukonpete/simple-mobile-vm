package com.slickpath.mobile.android.simple.vm.machine

import java.util.*

/**
 * This class wraps java.util.Stack to simplify the interface to what we really need.
 *
 * @author Pete Procopio
 * @see java.util.Stack
 */
internal class SimpleStack {
    /**
     * The stack container class
     */
    private val _stack = Stack<Int>()

    /**
     * Is the stack empty
     *
     * @return boolean - is the stack empty
     */
    val isEmpty: Boolean
        get() = _stack.empty()

    /**
     * Return the value at the top of the stack, do NOT pop it!
     *
     * @return Integer - value at top of stack
     */
    fun peek(): Int {
        return _stack.peek()
    }

    /**
     * Return the value at the top of the stack, pop it.
     *
     * @return Integer - value at top of stack before pop
     */
    fun pop(): Int {
        return _stack.pop()
    }

    /**
     * Push value to top of stack
     *
     * @param value value to push onto top of stack
     * @return Integer - value pushed onto top of stack
     */
    fun push(value: Int): Int {
        return _stack.push(value)
    }

    /**
     * Number of elements pushed onto the stack
     *
     * @return int - number of elements pushed onto the stack
     */
    fun size(): Int {
        return _stack.size
    }

    /**
     * Clear all items from the stack - The size should also be 0
     */
    fun reset() {
        _stack.clear()
    }

    /**
     * Dump the Stack as a List for debugging
     *
     *
     * The list should be ordered where the 1st element is at the bottom of the stack and the last
     * element is at the top of the stack.
     *
     *
     * NOTE: This makes a copy of the stack
     *
     * @return List<Integer> list of data
    </Integer> */
    fun dump(): List<Int> {
        return ArrayList(_stack)
    }
}