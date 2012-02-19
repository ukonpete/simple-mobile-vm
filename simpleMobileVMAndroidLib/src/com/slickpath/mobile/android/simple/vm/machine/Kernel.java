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

import android.util.Log;

import com.slickpath.mobile.android.simple.vm.VMError;

/**
 * @author Pete Procopio
 * Kernel level call for the VM
 * 
 */
public class Kernel implements IStack{

	private static final String TAG = Machine.class.getName();

	/**
	 * Convenience Const.
	 */
	public static final int PUSHC_YES = 1;
	/**
	 * Convenience Const.
	 */
	public static final int PUSHC_NO = 0;

	protected boolean _bDebug = false;

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
	protected int getValueAt(final int location) throws VMError
	{
		int returnVal = 0;
		if (location < Memory.MAX_MEMORY)
		{
			returnVal = _memory.get(location);
		}
		else
		{
			throw new VMError("getValueAt", VMError.VM_ERROR_TYPE_MEMORY_LIMIT);
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
	protected Integer setValueAt(final int value, final int location) throws VMError
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
	protected int pop() throws VMError
	{
		if (!_memory.isStackEmpty())
		{
			final int returnVal = _memory.get(_memory.getStackPointer());
			_memory.decStackPtr();
			// Reset every memory position we pop to 99999
			_memory.set((_memory.getStackPointer() + 1), Memory.EMPTY_MEMORY_VALUE);
			return returnVal;
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
	protected void push(final int value) throws VMError
	{
		if (!_memory.isStackFull())
		{
			_memory.incStackPtr();
			_memory.set(_memory.getStackPointer(), value);
		}
		else
		{
			throw new VMError("PUSHC", VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
	}

	/**
	 * Set the program counter (the pointer to the next instruction to be run) to the location that is passed in
	 * 
	 * @param location
	 * @throws VMError
	 */
	protected void branch(final int location) throws VMError
	{
		debug("--BR=" + _memory.getProgramCounter());
		final int tempLocation = (location * 2) + Memory.STACK_LIMIT;
		if (tempLocation >= Memory.STACK_LIMIT)
		{
			_memory.setProgramCounter(tempLocation);
		}
		else
		{
			throw new VMError("BRANCH : loc=" + location + " temp="+ tempLocation, VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
		debug("--BR=" + _memory.getProgramCounter());
	}

	/**
	 *  Set the program counter (the pointer to the next instruction to be run) to the location that the top of the stack indicates.
	 *  The value used is popped from the stack
	 * 
	 * @throws VMError
	 */
	protected void jump() throws VMError
	{
		final int location = pop();
		_memory.setProgramCounter((location * 2) + Memory.STACK_LIMIT);
	}

	/**
	 * If debug is enabled write the output to a log
	 * 
	 * Uses this classes TAG
	 * 
	 * @param sText
	 */
	protected void debug(final String sText)
	{
		if ( _bDebug )
		{
			debug(TAG, sText);
		}
	}

	/**
	 * If debug is enabled write the output to a log
	 * 
	 * THis allows callers of this class to do logging without having to implement it.
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
	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#getProgramCounter()
	 */
	@Override
	public int getProgramCounter()
	{
		return _memory.getProgramCounter();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#incProgramCounter()
	 */
	@Override
	public void incProgramCounter()
	{
		_memory.incProgramCounter();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#decProgramCounter()
	 */
	@Override
	public void decProgramCounter()
	{
		_memory.decProgramCounter();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#resetProgramCounter()
	 */
	@Override
	public void resetProgramCounter()
	{
		_memory.resetProgramCounter();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#getProgramWriterPtr()
	 */
	@Override
	public int getProgramWriterPtr()
	{
		return _memory.getProgramWriterPtr();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#incProgramWriter()
	 */
	public void incProgramWriter()
	{
		_memory.incProgramWriter();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#resetProgramWriter()
	 */
	@Override
	public void resetProgramWriter()
	{
		_memory.resetProgramWriter();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#getStackPointer()
	 */
	@Override
	public int getStackPointer()
	{
		return _memory.getStackPointer();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#incStackPtr()
	 */
	@Override
	public void incStackPtr() throws VMError
	{
		_memory.incStackPtr();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#decStackPtr()
	 */
	@Override
	public void decStackPtr() throws VMError
	{
		_memory.decStackPtr();
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#resetStackPointer()
	 */
	@Override
	public void resetStackPointer()
	{
		_memory.resetStackPointer();
	}
}
