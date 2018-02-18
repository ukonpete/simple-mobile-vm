package com.slickpath.mobile.android.simple.vm.machine;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Pete Procopio
 *         This class wraps java.util.Stack to simplify the interface to what we really need.
 * @see java.util.Stack
 */
class SimpleStack {

    /**
     * The stack container class
     */
    private final Stack<Integer> _stack = new Stack<>();

    /**
     * Is the stack empty
     *
     * @return boolean - is the stack empty
     */
    boolean isEmpty() {
        return _stack.empty();
    }

    /**
     * Return the value at the top of the stack, do NOT pop it!
     *
     * @return Integer - value at top of stack
     */
    Integer peek() {
        return _stack.peek();
    }

    /**
     * Return the value at the top of the stack, pop it.
     *
     * @return Integer - value at top of stack before pop
     */
    Integer pop() {
        return _stack.pop();
    }

    /**
     * Push value to top of stack
     *
     * @param value value to push onto top of stack
     * @return Integer - value pushed onto top of stack
     */
    Integer push(final Integer value) {
        return _stack.push(value);
    }

    /**
     * Number of elements pushed onto the stack
     *
     * @return int - number of elements pushed onto the stack
     */
    int size() {
        return _stack.size();
    }

    /**
     * Clear all items from the stack - The size should also be 0
     */
    void reset() {
        _stack.clear();
    }

    /**
     * Dump the Stack as a List for debugging
     * The list should be ordered where the 1st element is at the bottom of the stack and the last
     * element is at the top of the stack.
     * <p>
     * NOTE: This makes a copy of the stack
     *
     * @return List<Integer> list of data
     */
    @NonNull
    List<Integer> dump() {
        return new ArrayList<>(_stack);
    }
}
