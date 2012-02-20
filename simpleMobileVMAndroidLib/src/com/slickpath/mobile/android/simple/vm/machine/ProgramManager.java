/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.machine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PJ
 *
 */
public class ProgramManager {

	/**
	 * Location in memory of stack pointer when stack is empty
	 */
	public static final int EMPTY_LOC = -1;

	/**
	 * Value of memory if it is empty (unused)
	 */
	public static final int EMPTY_MEMORY_VALUE = 999999;

	/**
	 * Memory consists of N number of sequential Integers
	 */
	public static final int MAX_MEMORY = 500;

	/**
	 * Keeps track of location of next instruction to run
	 */
	private int _programCtr = EMPTY_LOC;

	/**
	 * Keeps track of next location where an instruction to run can be written to- part of program setup
	 */
	private int _programWriter = EMPTY_LOC;

	private final List<Integer> _programStore = new ArrayList<Integer>(MAX_MEMORY);

	public ProgramManager()
	{
		// initialize every piece of memory to EMPTY
		for (int i = 0; i < MAX_MEMORY; i++)
		{
			_programStore.add(EMPTY_MEMORY_VALUE);
		}
	}

	// TODO Wrap in memory / interface
	/*
	public void branch(final int location) throws VMError
	{
		if (location < MAX_MEMORY)
		{
			setProgramCounter(location);
		}
		else
		{
			throw new VMError("BRANCH : loc=" + location, VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
	}
	 */


	/**
	 * Sets value of program counter (set it to a position in memory where the next command will be run from)
	 * 
	 * @param location
	 */
	public void setProgramCounter(final int location)
	{
		_programCtr = location;
	}

	public int getProgramCounter()
	{
		return _programCtr;
	}

	public void incProgramCounter()
	{
		_programCtr++;
	}

	public void decProgramCounter()
	{
		_programCtr--;
	}

	public void resetProgramCounter()
	{
		_programCtr = EMPTY_LOC;
	}

	public int getProgramWriterPtr()
	{
		return _programWriter;
	}

	public void incProgramWriter()
	{
		_programWriter++;
	}

	public void resetProgramWriter()
	{
		_programWriter = EMPTY_LOC;
	}
}
