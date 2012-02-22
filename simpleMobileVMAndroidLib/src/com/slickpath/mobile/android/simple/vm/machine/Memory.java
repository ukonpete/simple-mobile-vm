package com.slickpath.mobile.android.simple.vm.machine;

import java.util.ArrayList;
import java.util.List;

import com.slickpath.mobile.android.simple.vm.VMError;

/**
 * @author Pete Procopio
 * This class is the VM Basic Memory manager
 * 
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
		_memoryStore = new ArrayList<Integer>(MAX_MEMORY);
		// initialize every piece of memory to EMPTY
		for (int i = 0; i < MAX_MEMORY; i++)
		{
			_memoryStore.add(EMPTY_MEMORY_VALUE);
		}
	}

	/**
	 * Returns if Stack is empty
	 * 
	 * @return boolean
	 */
	public boolean isStackEmpty()
	{
		return _stack.isEmpty();
	}

	/**
	 * Empty the stack
	 */
	public void resetStack()
	{
		_stack.reset();
	}

	/**
	 * Return memory as a List<Integer>
	 * NOTE: Creates a copy of memory
	 * 
	 * @return
	 */
	public List<Integer> memoryDump()
	{
		return  new ArrayList<Integer>(_memoryStore);
	}

	/**
	 * Return memory as a List<Integer>
	 * 
	 * @return
	 */
	public List<Integer> stackDump()
	{
		return _stack.dump();
	}

	/**
	 * Return program memory as a List<Integer>
	 * NOTE: Returns a Copy of Program Memory
	 * 
	 * @return
	 */
	public List<Integer> programMemoryDump()
	{
		return _programManager.dumpProgramStore();
	}

	/**
	 * Return parameter memory as a List<Integer>
	 * NOTE: Returns a Copy of parameter Memory
	 * 
	 * @return
	 */
	public List<Integer> parameterMemoryDump()
	{
		return _programManager.dumpParameterStore();
	}

	/**
	 * Return value at location
	 * 
	 * @see Java.util.List
	 * 
	 * @param location
	 * @return
	 */
	public int get(final int location)
	{
		return _memoryStore.get(location);
	}

	/**
	 * Returns the instruction at the specified location
	 * 
	 * @param location
	 * @return
	 */
	public int getInstruction(final int location)
	{
		return _programManager.getInstructionAt(location);
	}

	/**
	 * Returns the parameters at the specified location
	 * 
	 * @param location
	 * @return
	 */
	public int getParameters(final int location)
	{
		return _programManager.getParametersAt(location);
	}

	/**
	 * Set value at location
	 * 
	 * @see Java.util.List
	 * 
	 * @param location
	 * @param value
	 * @return
	 */
	public Integer set(final int location, final int value)
	{
		return _memoryStore.set(location, value);
	}

	/**
	 * Set instruction value at the specified location
	 *
	 * @param location
	 * @param value
	 */
	public void setInstruction(final int location, final int value)
	{
		_programManager.setInstructionAt(location, value);
	}

	/**
	 * Set parameter values at the specified location
	 *
	 * @param location
	 * @param value
	 */
	public void setParameters(final int location, final int value)
	{
		_programManager.setParametersAt(location, value);
	}

	/**
	 * pop the top value from the stack and return
	 * clear out the old top
	 * 
	 * @return
	 * @throws VMError
	 */
	public Integer pop_mem()
	{
		return _stack.pop();
	}

	/**
	 * pop the value onto the stack
	 * 
	 * @see Java.util.List
	 * 
	 * @param location
	 * @param value
	 * @return
	 */
	public Integer push_mem(final int value)
	{
		return _stack.push(value);
	}

	/**
	 * Get current line in program that will execute next
	 * @return int
	 */
	public int getProgramCounter()
	{
		return _programManager.getProgramCounter();
	}

	/**
	 * Increment program counter location to next line of instruction
	 */
	public void incProgramCounter()
	{
		_programManager.incProgramCounter();
	}

	/**
	 * Decrement program counter location to previous line of instruction
	 */
	public void decProgramCounter()
	{
		_programManager.decProgramCounter();
	}

	/**
	 * Set program counter location to indication location
	 * Used primarily for JUMP and BRNACH
	 * @param location
	 */
	public void setProgramCounter(final int location)
	{
		_programManager.setProgramCounter(location);
	}

	/**
	 * program counter location to start of program memory
	 */
	public void resetProgramCounter()
	{
		_programManager.resetProgramCounter();
	}

	/**
	 * Get the current location i program memory where the next instruction will be added
	 * @return
	 */
	public int getProgramWriterPtr()
	{
		return _programManager.getProgramWriterPtr();
	}

	/**
	 * Increment program writer location to next memory location
	 */
	public void incProgramWriter()
	{
		_programManager.incProgramWriter();
	}

	/**
	 * Decrement program writer location to previous memory location
	 */
	public void resetProgramWriter()
	{
		_programManager.resetProgramWriter();
	}
}