/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.machine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PJ
 * This class abstracts the handling of the memory used to store program instructions and parameters
 *
 */
public class ProgramManager {

	/**
	 * Location in memory of stack pointer when stack is empty
	 */
	public static final int START_LOC = 0;

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
	private int _programCtr = START_LOC;

	/**
	 * Keeps track of next location where an instruction to run can be written to- part of program setup
	 */
	private int _programWriter = START_LOC;

	/**
	 * Actually store we use for memory
	 */
	private final List<Integer> _programStore = new ArrayList<Integer>(MAX_MEMORY);

	/**
	 * Actually store we use for parameters
	 */
	private final List<Integer> _parameterStore = new ArrayList<Integer>(MAX_MEMORY);

	/**
	 * Constructor
	 */
	public ProgramManager()
	{
		// initialize every piece of instruction memory to EMPTY
		for (int i = 0; i < MAX_MEMORY; i++)
		{
			_programStore.add(EMPTY_MEMORY_VALUE);
		}

		// initialize every piece of parameter memory to EMPTY
		for (int i = 0; i < MAX_MEMORY; i++)
		{
			_parameterStore.add(EMPTY_MEMORY_VALUE);
		}
	}

	/**
	 * Sets value of program counter (set it to a position in memory where the next command will be run from)
	 * 
	 * @param location
	 */
	public void setProgramCounter(final int location)
	{
		_programCtr = location;
	}

	/**
	 * Get current line in program that will execute next
	 * @return int
	 */
	public int getProgramCounter()
	{
		return _programCtr;
	}

	/**
	 * Increment program counter location to next line of instruction
	 */
	public void incProgramCounter()
	{
		_programCtr++;
	}

	/**
	 * Decrement program counter location to previous line of instruction
	 */
	public void decProgramCounter()
	{
		_programCtr--;
	}

	/**
	 * program counter location to start of program memory
	 */
	public void resetProgramCounter()
	{
		_programCtr = START_LOC;
	}

	/**
	 * Get the current location in program memory where the next instruction will be added
	 * @return
	 */
	public int getProgramWriterPtr()
	{
		return _programWriter;
	}

	/**
	 * Increment program writer location to previous memory location
	 */
	public void incProgramWriter()
	{
		_programWriter++;
	}

	/**
	 * Reset program writer location to starting memory location
	 */
	public void resetProgramWriter()
	{
		_programWriter = START_LOC;
	}

	/**
	 * Returns the instructions as a list
	 * 
	 * NOTE: This makes a copy of the program memory
	 * @return Copy of memory
	 */
	public  List<Integer> dumpProgramStore()
	{
		return  new ArrayList<Integer>(_programStore);
	}

	/**
	 * Returns the parameters as a list
	 * 
	 * @return Copy of memory
	 */
	public  List<Integer> dumpParameterStore()
	{
		return  new ArrayList<Integer>(_parameterStore);
	}

	/**
	 * Returns the instruction at the specified location
	 * 
	 * @param location
	 * @return
	 */
	public Integer getInstructionAt(final int location)
	{
		return _programStore.get(location);
	}

	/**
	 * Returns the parameters at the specified location
	 * 
	 * @param location
	 * @return
	 */
	public Integer getParametersAt(final int location)
	{
		return _parameterStore.get(location);
	}

	/**
	 * Set instruction value at the specified location
	 *
	 * @param location
	 * @param value
	 */
	public void setInstructionAt(final int location, final int value)
	{
		_programStore.set(location, value);
	}

	/**
	 * Set parameter values at the specified location
	 *
	 * @param location
	 * @param value
	 */
	public void setParametersAt(final int location, final int value)
	{
		_parameterStore.set(location, value);
	}
}
