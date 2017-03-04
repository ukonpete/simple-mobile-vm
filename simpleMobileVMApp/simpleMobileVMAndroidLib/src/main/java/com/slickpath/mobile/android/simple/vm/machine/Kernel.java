package com.slickpath.mobile.android.simple.vm.machine;

import android.support.annotation.NonNull;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.util.Command;

import java.util.List;

/**
 * @author Pete Procopio
 *         Kernel level call for the VM
 */
public class Kernel {

    public static final int PUSHC_YES = 1;
    public static final int PUSHC_NO = 0;
    private static final String TAG = Machine.class.getName();
    protected final boolean _bDebugDump = false;
    protected final boolean _bDebugVerbose = true;
    private final Memory _memory = new Memory();
    private boolean _bDebug = true;

    /**
     * Constructor
     */
    public Kernel() {
        super();
    }

    /**
     * Returns the value at a specified memory location
     *
     * @param location - in memory to return
     * @return int - value at specified memory location
     * @throws VMError error in VM
     */
    public int getValueAt(final int location) throws VMError {
        int returnVal;
        if (location < Memory.MAX_MEMORY) {
            returnVal = _memory.get(location);
        } else {
            throw new VMError("getValueAt : " + location, VMError.VM_ERROR_TYPE_MEMORY_LIMIT);
        }
        return returnVal;
    }

    /**
     * Set value at location specified
     *
     * @param value    value to be set
     * @param location location in memory
     * @return Integer (previous value) - @see Java.util.List.set()
     * @throws VMError error in VM
     */
    public Integer setValueAt(final int value, final int location) throws VMError {
        if (location < Memory.MAX_MEMORY) {
            return _memory.set(location, value);
        } else {
            throw new VMError("setValAt", VMError.VM_ERROR_TYPE_MEMORY_LIMIT);
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
    public int pop() throws VMError {
        if (!_memory.isStackEmpty()) {
            return _memory.pop_mem();
        } else {
            throw new VMError("_pop", VMError.VM_ERROR_TYPE_STACK_LIMIT);
        }
    }

    /**
     * Push the indicated value to the top of the stack (increment stack pointer then add item at the new stack pointer location)
     *
     * @param value value to push
     * @throws VMError error in VM
     */
    public void push(final int value) throws VMError {
        try {
            _memory.push_mem(value);
        } catch (@NonNull final Exception e) {
            throw new VMError("PUSHC", e, VMError.VM_ERROR_TYPE_STACK_LIMIT);
        }
    }

    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that is passed in
     *
     * @param location location in memory
     * @throws VMError error in VM
     */
    public void branch(final int location) throws VMError {
        if (location <= Memory.MAX_MEMORY) {
            _memory.setProgramCounter(location);
        } else {
            throw new VMError("BRANCH : loc=" + location, VMError.VM_ERROR_TYPE_STACK_LIMIT);
        }
        debugVerbose(TAG, "--BR=" + _memory.getProgramCounter());
    }

    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that the top of the stack indicates.
     * The value used is popped from the stack
     *
     * @throws VMError error in VM
     */
    public void jump() throws VMError {
        final int location = pop();
        _memory.setProgramCounter(location);
    }

    /**
     * If debugVerbose is enabled write the output to a log
     * <p>
     * This allows sub-classes of this class to do logging without having to implement it.
     *
     * @param sTag  - the Log.d TAG to use
     * @param sText test to log
     */
    protected void debugVerbose(final String sTag, final String sText) {
        if (_bDebugVerbose) {
            debug(sTag, sText);
        }
    }

    /**
     * If debug is enabled write the output to a log
     * <p>
     * This allows sub-classes of this class to do logging without having to implement it.
     *
     * @param sTag  - the Log.d TAG to use
     * @param sText
     */
    protected void debug(final String sTag, final String sText) {
        if (_bDebug) {
            Log.d(sTag, sText);
        }
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
    public void setDebug(final boolean bDebug) {
        _bDebug = bDebug;
    }

    /**
     * Get current line in program that will execute next
     *
     * @return int
     */
    public int getProgramCounter() {
        return _memory.getProgramCounter();
    }

    /**
     * Increment program counter location to next line of instruction
     */
    public void incProgramCounter() {
        _memory.incProgramCounter();
    }

    /**
     * Decrement program counter location to previous line of instruction
     */
    public void decProgramCounter() {
        _memory.decProgramCounter();
    }

    /**
     * program counter location to start of program memory
     */
    public void resetProgramCounter() {
        _memory.resetProgramCounter();
    }

    /**
     * Get the current location in program memory where the next instruction will be added
     *
     * @return current location in program memory where the next instruction will be added
     */
    public int getProgramWriterPtr() {
        return _memory.getProgramWriterPtr();
    }

    /**
     * Increment program writer location to next memory location
     */
    public void incProgramWriter() {
        _memory.incProgramWriter();
    }

    /**
     * Reset program writer location to starting memory location
     */
    public void resetProgramWriter() {
        _memory.resetProgramWriter();
    }

    /**
     * Empty the stack
     */
    public void resetStack() {
        _memory.resetStack();
    }

    /**
     * Gets the instruction and parameters at the requested location and puts it in a list
     *
     * @param location location in memory
     * @return Command at location
     * @throws VMError error in VM
     */
    public Command getCommandAt(final int location) throws VMError {
        if (location < Memory.MAX_MEMORY) {
            final Integer instruction = _memory.getCommand(location).getCommandId();
            final Integer parameters = _memory.getCommand(location).getParameters().get(0);
            if (_bDebug) {
                Log.d(TAG, "Get Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(instruction) + ") " + instruction + " param " + parameters + " at " + location);
            }

            return _memory.getCommand(location);
        } else {
            throw new VMError("getValueAt", VMError.VM_ERROR_TYPE_MEMORY_LIMIT);
        }
    }

    /**
     * Takes an instruction and parameters and stores it in the requested location
     *
     * @param command  the command to set
     * @param location location in memory
     * @throws VMError error in VM
     */
    public void setCommandAt(@NonNull final Command command, final int location) {
        if (location < Memory.MAX_MEMORY) {
            if (_bDebug) {
                String sParamInfo = "<null>";
                if (command.getParameters().get(0) != null) {
                    sParamInfo = command.getParameters().get(0).toString();
                }
                Log.d(TAG, "Set Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(command.getCommandId()) + ") " + command.getCommandId() + " param " + sParamInfo + " at " + location);

            }

            _memory.setCommand(location, command);
            incProgramWriter();
        }
    }

    /**
     * Dump the memory as a list
     *
     * @return List<Integer> list of each each value of memory
     */
    @NonNull
    public List<Integer> dumpMemory() {
        return _memory.memoryDump();
    }

    /**
     * Returns the stack as a List, where the 1st element in the list is at the bottom of the stack
     *
     * @return List<Integer> list of each each value of memory in the stack
     */
    @NonNull
    public List<Integer> dumpStack() {
        return _memory.stackDump();
    }

    /**
     * Returns all the Instruction/Parameter lists as one list
     *
     * @return List<Integer> list of each each value of memory for instructions
     */
    @NonNull
    public List<Command> dumpInstructionMemory() {
        return _memory.programMemoryDump();
    }
}
