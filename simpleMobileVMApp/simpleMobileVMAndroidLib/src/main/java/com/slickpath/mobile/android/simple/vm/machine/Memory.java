package com.slickpath.mobile.android.simple.vm.machine;

import android.support.annotation.NonNull;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.util.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pete Procopio
 *         This class is the VM Basic Memory manager
 */
public class Memory {

    /**
     * 1st location in memory
     */
    public static final int START_LOC = ProgramManager.START_LOC;

    /**
     * Value of memory if it is empty (unused)
     */
    public static final int EMPTY_MEMORY_VALUE = 999999;

    /**
     * Memory consists of N number of sequential Integers
     */
    public static final int MAX_MEMORY = 500;

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
    public Memory() {
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
    public boolean isStackEmpty() {
        return _stack.isEmpty();
    }

    /**
     * Empty the stack
     */
    public void resetStack() {
        _stack.reset();
    }

    /**
     * Return memory as a List<Integer>
     * NOTE: Creates a copy of memory
     *
     * @return
     */
    @NonNull
    public List<Integer> memoryDump() {
        return new ArrayList<>(_memoryStore);
    }

    /**
     * Return memory as a List<Integer>
     *
     * @return
     */
    @NonNull
    public List<Integer> stackDump() {
        return _stack.dump();
    }

    /**
     * Return program memory as a List<Integer>
     * NOTE: Returns a Copy of Program Memory
     *
     * @return
     */
    @NonNull
    public List<Command> programMemoryDump() {
        return _programManager.dumpProgramStore();
    }


    /**
     * Return value at location
     *
     * @param location
     * @return
     */
    public int get(final int location) {
        return _memoryStore.get(location);
    }

    /**
     * Returns the instruction at the specified location
     *
     * @param location
     * @return
     */
    public Command getCommand(final int location) {
        return _programManager.getCommandAt(location);
    }

    /**
     * Set value at location
     *
     * @param location
     * @param value
     * @return
     */
    public Integer set(final int location, final int value) {
        return _memoryStore.set(location, value);
    }

    /**
     * Set instruction value at the specified location
     *
     * @param location
     * @param command
     */
    public void setCommand(final int location, final Command command) {
        _programManager.setCommandAt(location, command);
    }

    /**
     * pop the top value from the stack and return
     * clear out the old top
     *
     * @return
     * @throws VMError
     */
    public Integer pop_mem() {
        return _stack.pop();
    }

    /**
     * pop the value onto the stack
     *
     * @param value
     * @return
     */
    public Integer push_mem(final int value) {
        return _stack.push(value);
    }

    /**
     * Get current line in program that will execute next
     *
     * @return int
     */
    public int getProgramCounter() {
        return _programManager.getProgramCounter();
    }

    /**
     * Set program counter location to indication location
     * Used primarily for JUMP and BRNACH
     *
     * @param location
     */
    public void setProgramCounter(final int location) {
        _programManager.setProgramCounter(location);
    }

    /**
     * Increment program counter location to next line of instruction
     */
    public void incProgramCounter() {
        _programManager.incProgramCounter();
    }

    /**
     * Decrement program counter location to previous line of instruction
     */
    public void decProgramCounter() {
        _programManager.decProgramCounter();
    }

    /**
     * program counter location to start of program memory
     */
    public void resetProgramCounter() {
        _programManager.resetProgramCounter();
    }

    /**
     * Get the current location i program memory where the next instruction will be added
     *
     * @return
     */
    public int getProgramWriterPtr() {
        return _programManager.getProgramWriterPtr();
    }

    /**
     * Increment program writer location to next memory location
     */
    public void incProgramWriter() {
        _programManager.incProgramWriter();
    }

    /**
     * Decrement program writer location to previous memory location
     */
    public void resetProgramWriter() {
        _programManager.resetProgramWriter();
    }
}