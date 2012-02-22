/**
 * This class represents the basic functionality of the machine itself
 * for example
 *   -  push/pop stack data
 *   -  stack tracking
 *   -  read/write memory data
 *   -  memory tracking
 *   -  read/write program data
 *
 *   NOTE:  Program memory uses the same space as accessible memory
 */
package com.slickpath.mobile.android.simple.vm.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;

/**
 * @author Pete Procopio
 * Kernel level call for the VM
 * 
 */
public class Kernel {

	private static final String TAG = Machine.class.getName();

	public static final int INSTRUCTION_LOC = 0;

	public static final int PARAMETERS_LOC = 1;

	/**
	 * Convenience Const.
	 */
	public static final int PUSHC_YES = 1;
	/**
	 * Convenience Const.
	 */
	public static final int PUSHC_NO = 0;

	protected boolean _bDebug = false;

	protected boolean _bDebugDump = false;

	protected boolean _bDebugVerbose = false;

	private final  Memory _memory = new Memory();

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
	 * @throws VMError
	 */
	public int getValueAt(final int location) throws VMError
	{
		int returnVal = 0;
		if (location < Memory.MAX_MEMORY)
		{
			returnVal = _memory.get(location);
		}
		else
		{
			throw new VMError("getValueAt : " + location, VMError.VM_ERROR_TYPE_MEMORY_LIMIT);
		}
		return returnVal;
	}

	/**
	 * Set value at location specified
	 * 
	 * @param value
	 * @param location
	 * @return Integer (previous value) - @see Java.util.List.set()
	 * @throws VMError
	 */
	public Integer setValueAt(final int value, final int location) throws VMError
	{
		if (location < Memory.MAX_MEMORY)
		{
			return _memory.set(location,value);
		}
		else
		{
			throw new VMError("setValAt", VMError.VM_ERROR_TYPE_MEMORY_LIMIT);
		}
	}

	/**
	 * Return the value at the current stack pointer, remove item from stack (decrement stack pointer)
	 * 
	 * The value at the location of the stack pointer at the time of the call will be cleaned
	 * 
	 * @return int - value at top of stack
	 * @throws VMError
	 */
	public int pop() throws VMError
	{
		if (!_memory.isStackEmpty())
		{
			return _memory.pop_mem();
		}
		else
		{
			throw new VMError("_pop", VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
	}

	/**
	 * Push the indicated value to the top of the stack (increment stack pointer then add item at the new stack pointer location)
	 * 
	 * @param value
	 * @throws VMError
	 */
	public void push(final int value) throws VMError
	{
		try
		{
			_memory.push_mem(value);
		}
		catch(final Exception e)
		{
			throw new VMError("PUSHC", e, VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
	}

	/**
	 * Set the program counter (the pointer to the next instruction to be run) to the location that is passed in
	 * 
	 * @param location
	 * @throws VMError
	 */
	public void branch(final int location) throws VMError
	{
		if (location <= Memory.MAX_MEMORY)
		{
			_memory.setProgramCounter(location);
		}
		else
		{
			throw new VMError("BRANCH : loc=" + location, VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
		debugVerbose(TAG, "--BR=" + _memory.getProgramCounter());
	}

	/**
	 *  Set the program counter (the pointer to the next instruction to be run) to the location that the top of the stack indicates.
	 *  The value used is popped from the stack
	 * 
	 * @throws VMError
	 */
	public void jump() throws VMError
	{
		final int location = pop();
		_memory.setProgramCounter(location);
	}

	/**
	 * If debugVerbose is enabled write the output to a log
	 * 
	 * This allows sub-classes of this class to do logging without having to implement it.
	 * 
	 * @param sTag - the Log.d TAG to use
	 * @param sText
	 */
	protected void debugVerbose(final String sTag, final String sText)
	{
		if ( _bDebugVerbose )
		{
			debug(sTag, sText);
		}
	}

	/**
	 * If debug is enabled write the output to a log
	 * 
	 * This allows sub-classes of this class to do logging without having to implement it.
	 * 
	 * @param sTag - the Log.d TAG to use
	 * @param sText
	 */
	protected void debug(final String sTag, final String sText)
	{
		if ( _bDebug )
		{
			Log.d(sTag, sText);
		}
	}

	/**
	 * Toggle if debug will be enabled
	 * 
	 * If enabled certain methods will log to Android Log.d
	 * 
	 * Also any output commands (example WRCHR and WRINT) will Log.d the output
	 * 
	 * @param bDebug
	 */
	public void setDebug(final boolean bDebug)
	{
		_bDebug = bDebug;
	}

	/**************************************************
	 * Abstract memory from other sub-Classes of Kernel
	 ***************************************************/

	/**
	 * Get current line in program that will execute next
	 * @return int
	 */
	public int getProgramCounter()
	{
		return _memory.getProgramCounter();
	}

	/**
	 * Increment program counter location to next line of instruction
	 */
	public void incProgramCounter()
	{
		_memory.incProgramCounter();
	}

	/**
	 * Decrement program counter location to previous line of instruction
	 */
	public void decProgramCounter()
	{
		_memory.decProgramCounter();
	}

	/**
	 * program counter location to start of program memory
	 */
	public void resetProgramCounter()
	{
		_memory.resetProgramCounter();
	}

	/**
	 * Get the current location in program memory where the next instruction will be added
	 * @return
	 */
	public int getProgramWriterPtr()
	{
		return _memory.getProgramWriterPtr();
	}

	/**
	 * Increment program writer location to next memory location
	 */
	public void incProgramWriter()
	{
		_memory.incProgramWriter();
	}

	/**
	 * Reset program writer location to starting memory location
	 */
	public void resetProgramWriter()
	{
		_memory.resetProgramWriter();
	}

	/**
	 * Empty the stack
	 */
	public void resetStack()
	{
		_memory.resetStack();
	}

	/**
	 * Gets the instruction and parameters at the requested location and puts it in a list
	 * 
	 * @param location
	 * @return
	 * @throws VMError
	 */
	public List<Integer> getInstructionAt(final int location) throws VMError
	{
		if (location < Memory.MAX_MEMORY)
		{
			final Integer instruction = _memory.getInstruction(location);
			final Integer parameters = _memory.getParameters(location);
			if ( _bDebug )
			{
				Log.d(TAG, "Get Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(instruction) + ") " + instruction + " param " + parameters + " at " + location);
			}
			return new ArrayList<Integer>(Arrays.asList(instruction,parameters));
		}
		else
		{
			throw new VMError("getValueAt", VMError.VM_ERROR_TYPE_MEMORY_LIMIT);
		}
	}

	/**
	 * Takes an instruction and parameters and stores it in the requested location
	 * 
	 * @param instruction
	 * @param parameters
	 * @param location
	 * @throws VMError
	 */
	public void setInstructionAt(final int instruction, final int parameters, final int location) throws VMError
	{
		if (location < Memory.MAX_MEMORY)
		{
			if ( _bDebug )
			{
				Log.d(TAG, "Set Instruction (" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(instruction) + ") " + instruction + " param " + parameters + " at " + location);
			}
			_memory.setInstruction(location, instruction);
			_memory.setParameters(location, parameters);
			incProgramWriter();
		}
	}

	/**
	 * Dump the memory as a list
	 * 
	 * @return
	 * 
	 */
	public List<Integer> dumpMemory()
	{
		return _memory.memoryDump();
	}

	/**
	 * Returns the stack as a List, where the 1st element in the list is at the bottom of the stack
	 *
	 * @return
	 */
	public List<Integer> dumpStack()
	{
		return _memory.stackDump();
	}

	/**
	 * Returns all the Instruction/Parameter lists as one list
	 * 
	 * @return
	 */
	public List<List<Integer>> dumpInstructionMemory()
	{
		final List<List<Integer>> instructionMemory = new ArrayList<List<Integer>>();
		final List<Integer> programMemory =  _memory.programMemoryDump();
		final List<Integer> paramMemory =  _memory.parameterMemoryDump();

		for(int i = 0; i < programMemory.size(); i++)
		{
			final List<Integer> instruciton = new ArrayList<Integer>();
			instruciton.add(programMemory.get(i));
			instruciton.add(paramMemory.get(i));
			instructionMemory.add(instruciton);
		}
		return instructionMemory;
	}
}
