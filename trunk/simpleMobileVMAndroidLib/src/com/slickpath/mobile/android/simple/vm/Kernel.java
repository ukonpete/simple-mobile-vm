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
package com.slickpath.mobile.android.simple.vm;


/**
 * @author PJ
 *
 */
public class Kernel extends Memory {

	public static final int PUSHC_YES = 1;
	public static final int PUSHC_NO = 0;

	/**
	 * 
	 */
	public Kernel() {
		super();
	}

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
			throw new VMError("setValAt", VMError.VM_ERROR_TYPE_LOC_MAX_MEMORY);
		}
	}

	protected int pop() throws VMError
	{
		if (!isStackEmpty())
		{
			final int returnVal = _memory.get(getStackPointer());
			decStackPtr();
			// Reset every memory position we pop to 99999
			_memory.set((getStackPointer() + 1), EMPTY_MEMORY_LOC);
			return returnVal;
		}
		else
		{
			throw new VMError("_pop", VMError.VM_ERROR_TYPE_STACK_PTR);
		}
	}

	protected void push(final int value) throws VMError
	{
		incStackPtr();
		_memory.set(getStackPointer(), value);
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
}
