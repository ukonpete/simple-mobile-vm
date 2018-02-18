package com.slickpath.mobile.android.simple.vm.machine;

import android.support.annotation.NonNull;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.VMErrorType;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.util.Command;

import java.util.List;

/**
 * @author Pete Procopio
 *         Kernel level call for the VM
 */
class Kernel {
    private static final String LOG_TAG = Machine.class.getName();

    static final int PUSHC_YES = 1;
    static final int PUSHC_NO = 0;

    private boolean debugDump = false;
    private boolean debugVerbose = true;
    private final Memory memory = new Memory();
    private boolean debug = true;

    /**
     * Constructor
     */
    Kernel() {
        super();
    }

    /**
     * Returns the value at a specified memory location
     *
     * @param location - in memory to return
     * @return int - value at specified memory location
     * @throws VMError error in VM
     */
    int getValueAt(final int location) throws VMError {
        int returnVal;
        if (location < Memory.MAX_MEMORY) {
            returnVal = memory.get(location);
        } else {
            throw new VMError("getValueAt : " + location, VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT);
        }
        return returnVal;
    }

    /**
     * Set value at location specified
     *
     * @param value value to be set
     * @param location location in memory
     * @return Integer (previous value) - @see Java.util.List.set()
     * @throws VMError error in VM
     */
    @SuppressWarnings("UnusedReturnValue")
    Integer setValueAt(final int value, final int location) throws VMError {
        if (location < Memory.MAX_MEMORY) {
            return memory.set(location, value);
        } else {
            throw new VMError("setValAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT);
        }
    }

    /**
     * Return the value at the current stack pointer, remove item from stack (decrement stack pointer)
     * <p>
     * The value at the location of the stack pointer at the time of the call will be cleaned
     *
     * @return int - value at top of stack
     * @throws VMError error in VM
     */
    int pop() throws VMError {
        if (!memory.isStackEmpty()) {
            return memory.pop_mem();
        } else {
            throw new VMError("_pop", VMErrorType.VM_ERROR_TYPE_STACK_LIMIT);
        }
    }

    /**
     * Push the indicated value to the top of the stack (increment stack pointer then add item at the new stack pointer location)
     *
     * @param value value to push
     * @throws VMError error in VM
     */
    void push(final int value) throws VMError {
        try {
            memory.push_mem(value);
        } catch (@NonNull final Exception e) {
            throw new VMError("PUSHC", e, VMErrorType.VM_ERROR_TYPE_STACK_LIMIT);
        }
    }

    @SuppressWarnings("unused")
    void setDebugDump(boolean debugDump) {
        this.debugDump = debugDump;
    }

    @SuppressWarnings("unused")
    boolean getDebugDump() {
        return debugDump;
    }
    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that is passed in
     *
     * @param location location in memory
     * @throws VMError error in VM
     */
    void branch(final int location) throws VMError {
        if (location <= Memory.MAX_MEMORY) {
            memory.setProgramCounter(location);
        } else {
            throw new VMError("BRANCH : loc=" + location, VMErrorType.VM_ERROR_TYPE_STACK_LIMIT);
        }
        debugVerbose(LOG_TAG, "--BR=" + memory.getProgramCounter());
    }

    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that the top of the stack indicates.
     * The value used is popped from the stack
     *
     * @throws VMError error in VM
     */
    void jump() throws VMError {
        final int location = pop();
        memory.setProgramCounter(location);
    }

    /**
     * If debugVerbose is enabled write the output to a log
     * <p>
     * This allows sub-classes of this class to do logging without having to implement it.
     *
     * @param tag  - the Log.d LOG_TAG to use
     * @param text test to log
     */
    void debugVerbose(final String tag, final String text) {
        if (debugVerbose) {
            debug(tag, text);
        }
    }

    @SuppressWarnings("unused")
    void setDebugVerbose(boolean debugVerbose) {
        this.debugVerbose = debugVerbose;
    }

    /**
     * If debug is enabled write the output to a log
     * <p>
     * This allows sub-classes of this class to do logging without having to implement it.
     *
     * @param tag  the Log.d LOG_TAG to use
     * @param text string to log if debug is enabled
     */
    void debug(final String tag, final String text) {
        if (debug) {
            Log.d(tag, text);
        }
    }

    /**
     * @return is debug verbose enabled
     */
    boolean getDebugVerbose() {
        return debugVerbose;
    }

    /**
     * Toggle if debug will be enabled
     * <p>
     * If enabled certain methods will log to Android Log.d
     * <p>
     * Also any output commands (example WRCHR and WRINT) will Log.d the output
     *
     * @param bDebug set this to be debug
     */
    @SuppressWarnings("unused")
    void setDebug(final boolean bDebug) {
        debug = bDebug;
    }

    /**
     * Get current line in program that will execute next
     *
     * @return int
     */
    int getProgramCounter() {
        return memory.getProgramCounter();
    }

    /**
     * Increment program counter location to next line of instruction
     */
    void incProgramCounter() {
        memory.incProgramCounter();
    }

    /**
     * Decrement program counter location to previous line of instruction
     */
    void decProgramCounter() {
        memory.decProgramCounter();
    }

    /**
     * program counter location to start of program memory
     */
    void resetProgramCounter() {
        memory.resetProgramCounter();
    }

    /**
     * Get the current location in program memory where the next instruction will be added
     *
     * @return current location in program memory where the next instruction will be added
     */
    int getProgramWriterPtr() {
        return memory.getProgramWriterPtr();
    }

    /**
     * Increment program writer location to next memory location
     */
    void incProgramWriter() {
        memory.incProgramWriter();
    }

    /**
     * Reset program writer location to starting memory location
     */
    void resetProgramWriter() {
        memory.resetProgramWriter();
    }

    /**
     * Empty the stack
     */
    void resetStack() {
        memory.resetStack();
    }

    /**
     * Gets the instruction and parameters at the requested location and puts it in a list
     *
     * @param location location in memory
     * @return Command at location
     * @throws VMError error in VM
     */
    Command getCommandAt(final int location) throws VMError {
        if (location < Memory.MAX_MEMORY) {
            final Integer instruction = memory.getCommand(location).getCommandId();
            final Integer parameters = memory.getCommand(location).getParameters().get(0);
            if (debug) {
                Log.d(LOG_TAG, "Get Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(instruction) + ") " + instruction + " param " + parameters + " at " + location);
            }

            return memory.getCommand(location);
        } else {
            throw new VMError("getValueAt", VMErrorType.VM_ERROR_TYPE_MEMORY_LIMIT);
        }
    }

    /**
     * Takes an instruction and parameters and stores it in the requested location
     *
     * @param command  the command to set
     * @param location location in memory
     */
    void setCommandAt(@NonNull final Command command, final int location) {
        if (location < Memory.MAX_MEMORY) {
            if (debug) {
                String paramInfo = "<null>";
                if (command.getParameters().get(0) != null) {
                    paramInfo = command.getParameters().get(0).toString();
                }
                Log.d(LOG_TAG, "Set Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(command.getCommandId()) + ") " + command.getCommandId() + " param " + paramInfo + " at " + location);

            }

            memory.setCommand(location, command);
            incProgramWriter();
        }
    }

    /**
     * Dump the memory as a list
     *
     * @return List<Integer> list of each each value of memory
     */
    @NonNull
    List<Integer> dumpMemory() {
        return memory.memoryDump();
    }

    /**
     * Returns the stack as a List, where the 1st element in the list is at the bottom of the stack
     *
     * @return List<Integer> list of each each value of memory in the stack
     */
    @NonNull
    List<Integer> dumpStack() {
        return memory.stackDump();
    }

    /**
     * Returns all the Instruction/Parameter lists as one list
     *
     * @return List<Integer> list of each each value of memory for instructions
     */
    @NonNull
    List<Command> dumpInstructionMemory() {
        return memory.programMemoryDump();
    }
}
