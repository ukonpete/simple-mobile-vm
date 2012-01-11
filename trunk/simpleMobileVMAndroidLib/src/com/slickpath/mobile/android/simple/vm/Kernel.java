/**
 * 
 */
package com.slickpath.mobile.android.simple.vm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PJ
 *
 */
public class Kernel {

	public static final int EMPTY_MEMORY_LOC = 999999;
	public static final int MAX_MEMORY = 500;
	public static final int STACK_LIMIT = MAX_MEMORY / 2;
	public static final int PUSHC_YES = 1;
	public static final int PUSHC_NO = 0;

	private int _stackPtr = -1;
	private int _programCtr = STACK_LIMIT;
	private int _programWriter = STACK_LIMIT;

	private List<Integer> _memory = null;

	/**
	 * 
	 */
	public Kernel() {
		initMemory();
	}

	/**
	 * 
	 */
	private void initMemory() {
		_memory = new ArrayList<Integer>(MAX_MEMORY);
		// initialize every piece of memory to EMPTY
		for (int i = 0; i < MAX_MEMORY; i++)
		{
			_memory.add(EMPTY_MEMORY_LOC);
		}
	}

	// PRIVATE_METHODS
	protected int getValueAt(final int location) throws VMError
	{
		int returnVal = 0;
		if (location < MAX_MEMORY)
		{
			returnVal = _memory.get(location);
		}
		else
		{
			throw new VMError("getValueAt", VMError.VM_ERROR_TYPE_MAX_MEMORY);
		}
		return returnVal;
	}

	protected void setValueAt(final int value, final int location) throws VMError
	{
		if (location < MAX_MEMORY)
		{
			_memory.set(location,value);
		}
		else
		{
			throw new VMError("setValAtLocation", VMError.VM_ERROR_TYPE_LOC_MAX_MEMORY);
		}
	}

	protected int pop() throws VMError
	{
		if (_stackPtr >= 0)
		{
			final int returnVal = _memory.get(_stackPtr--);
			// Reset every memory position we pop to 99999
			_memory.set((_stackPtr + 1), EMPTY_MEMORY_LOC);
			return returnVal;
		}
		else
		{
			throw new VMError("_pop", VMError.VM_ERROR_TYPE_STACK_PTR);
		}
	}

	public List<Integer> memoryDump()
	{
		return _memory;
	}

	public int getStackPointer()
	{
		return _stackPtr;
	}

	protected void incStackPtr()
	{
		_stackPtr++;
	}

	protected void decStackPtr()
	{
		_stackPtr--;
	}

	protected void resetStackPointer()
	{
		_stackPtr = 0;
	}

	public boolean isStackPointerValid()
	{
		return _stackPtr < STACK_LIMIT;
	}

	protected void branch(final int location) throws VMError
	{
		final int tempLocation = (location * 2) + STACK_LIMIT;
		if (tempLocation >= STACK_LIMIT)
		{
			_programCtr = tempLocation;
		}
		else
		{
			throw new VMError("BRANCH : loc=" + location + " temp="+ tempLocation, VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
	}

	protected void jump() throws VMError
	{
		final int location = pop();
		_programCtr = (location * 2) + STACK_LIMIT;
	}

	protected int getProgramCounter()
	{
		return _programCtr;
	}

	protected void incProgramCounter()
	{
		_programCtr++;
	}

	protected void decProgramCounter()
	{
		_programCtr--;
	}

	protected void resetProgramCounter()
	{
		_programCtr = STACK_LIMIT;
	}

	protected int getProgramWriter()
	{
		return _programWriter;
	}

	protected void incProgramWriter()
	{
		_programWriter++;
	}

	protected void decProgramWriter()
	{
		_programWriter--;
	}

	protected void resetProgramWriter()
	{
		_programWriter = STACK_LIMIT;
	}
}
