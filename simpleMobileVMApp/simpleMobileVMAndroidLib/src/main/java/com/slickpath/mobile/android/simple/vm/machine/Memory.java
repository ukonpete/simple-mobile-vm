package com.slickpath.mobile.android.simple.vm.machine;

import androidx.annotation.NonNull;

import com.slickpath.mobile.android.simple.vm.util.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the VM Basic Memory manager
 *
 * @author Pete Procopio
 */
class Memory {

    /**
     * 1st location in memory
     */
    static final int START_LOC = ProgramManager.START_LOC;

    /**
     * Value of memory if it is empty (unused)
     */
    static final int EMPTY_MEMORY_VALUE = 999999;

    /**
     * Memory consists of N number of sequential Integers
     */
    static final int MAX_MEMORY = 500;

    /**
     * Memory to store values
     */
    @NonNull
    private final List<Integer> _memoryStore;

    /**
     * Memory Stack
     */
    private final SimpleStack _stack = new SimpleStack();

    /**
     * Manager and store for Program Memory
     */
    private final ProgramManager _programManager = new ProgramManager();

    /**
     * Constructor
     */
    Memory() {
        super();
        _memoryStore = new ArrayList<>(MAX_MEMORY);
        // initialize every piece of memory to EMPTY
        for (int i = 0; i < MAX_MEMORY; i++) {
            _memoryStore.add(EMPTY_MEMORY_VALUE);
        }
    }

    /**
     * Returns if Stack is empty
     *
     * @return boolean
     */
    boolean isStackEmpty() {
        return _stack.isEmpty();
    }

    /**
     * Empty the stack
     */
    void resetStack() {
        _stack.reset();
    }

    /**
     * Return memory as a List<Integer>
     * <p>NOTE: Creates a copy of memory
     *
     * @return value at every line of memory
     */
    @NonNull
    List<Integer> memoryDump() {
        return new ArrayList<>(_memoryStore);
    }

    /**
     * Return stack memory as a List<Integer>
     *
     * @return value at every line of stack memory
     */
    @NonNull
    List<Integer> stackDump() {
        return _stack.dump();
    }

    /**
     * Return program memory as a List<Integer>
     * <p>NOTE: Returns a copy of Program Memory
     *
     * @return Copy of every line of program Memory
     */
    @NonNull
    List<Command> programMemoryDump() {
        return _programManager.dumpProgramStore();
    }


    /**
     * Return value at location
     *
     * @param location location in memory
     * @return value at location
     */
    int get(final int location) {
        return _memoryStore.get(location);
    }

    /**
     * Returns the instruction at the specified location
     *
     * @param location location of command in program memory
     * @return command at location
     */
    Command getCommand(final int location) {
        return _programManager.getCommandAt(location);
    }

    /**
     * Set value at location
     *
     * @param location location in memory
     * @param value value to set
     * @return value that was set
     */
    Integer set(final int location, final int value) {
        return _memoryStore.set(location, value);
    }

    /**
     * Set instruction value at the specified location
     *
     * @param location location in program memory
     * @param command command to set
     */
    void setCommand(final int location, final Command command) {
        _programManager.setCommandAt(location, command);
    }

    /**
     * pop the top value from the stack and return
     * clear out the old top
     *
     * @return value at top of stack
     */
    Integer pop_mem() {
        return _stack.pop();
    }

    /**
     * pop the value onto the stack
     *
     * @param value value to push
     * @return value pushed on the stack
     */
    @SuppressWarnings("UnusedReturnValue")
    Integer push_mem(final int value) {
        return _stack.push(value);
    }

    /**
     * Get current line in program that will execute next
     *
     * @return int
     */
    int getProgramCounter() {
        return _programManager.getProgramCounter();
    }

    /**
     * Set program counter location to indicated location
     * Used primarily for JUMP and BRANCH
     *
     * @param location location the program counter points to
     */
    void setProgramCounter(final int location) {
        _programManager.setProgramCounter(location);
    }

    /**
     * Increment program counter location to next line of instruction
     */
    void incProgramCounter() {
        _programManager.incProgramCounter();
    }

    /**
     * Decrement program counter location to previous line of instruction
     */
    void decProgramCounter() {
        _programManager.decProgramCounter();
    }

    /**
     * program counter location to start of program memory
     */
    void resetProgramCounter() {
        _programManager.resetProgramCounter();
    }

    /**
     * Get the current location in program memory where the next instruction will be added
     *
     * @return current location in program memory where the next instruction will be added
     */
    int getProgramWriterPtr() {
        return _programManager.getProgramWriterPtr();
    }

    /**
     * Increment program writer location to next memory location
     */
    void incProgramWriter() {
        _programManager.incProgramWriter();
    }

    /**
     * Decrement program writer location to previous memory location
     */
    void resetProgramWriter() {
        _programManager.resetProgramWriter();
    }
}