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

	private final List<Integer> _memoryStore;

	private final SimpleStack _stack = new SimpleStack();

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

	public void resetStack()
	{
		_stack.reset();
	}
	/**
	 * Return memory as a List<Integer>
	 * 
	 * @return
	 */
	public List<Integer> memoryDump()
	{
		return _memoryStore;
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

	public int getProgramCounter()
	{
		return _programManager.getProgramCounter();
	}

	public void incProgramCounter()
	{
		_programManager.incProgramCounter();
	}

	public void decProgramCounter()
	{
		_programManager.decProgramCounter();
	}

	public void setProgramCounter(final int location)
	{
		_programManager.setProgramCounter(location);
	}

	public void resetProgramCounter()
	{
		_programManager.resetProgramCounter();
	}

	public int getProgramWriterPtr()
	{
		return _programManager.getProgramWriterPtr();
	}

	public void incProgramWriter()
	{
		_programManager.incProgramWriter();
	}

	public void resetProgramWriter()
	{
		_programManager.resetProgramWriter();
	}
}