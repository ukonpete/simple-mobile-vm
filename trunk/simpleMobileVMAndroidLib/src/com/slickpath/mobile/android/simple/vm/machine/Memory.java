package com.slickpath.mobile.android.simple.vm.machine;

import java.util.ArrayList;
import java.util.List;

import com.slickpath.mobile.android.simple.vm.VMError;

/**
 * @author Pete Procopio
 * This class is the VM Basic Memory manager
 * 
 */
public class Memory implements IStack {

	/**
	 * Location in memory of stack pointer when stack is empty
	 */
	public static final int STACK_EMPTY_LOC = -1;

	/**
	 * Value of memory if it is empty (unused)
	 */
	public static final int EMPTY_MEMORY_VALUE = 999999;

	/**
	 * Memory consists of N number of sequential Integers
	 */
	public static final int MAX_MEMORY = 500;
	/**
	 * The stack uses up the lower half of memory
	 */
	public static final int STACK_LIMIT = MAX_MEMORY / 2;

	/**
	 * Keeps track of currently stack location
	 */
	private int _stackPtr = STACK_EMPTY_LOC;

	/**
	 * Keeps track of location of next instruction to run
	 */
	private int _programCtr = STACK_LIMIT;

	/**
	 * Keeps track of next location where an instruction to run can be written to- part of program setup
	 */
	private int _programWriter = STACK_LIMIT;

	private final List<Integer> _memoryStore;

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

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#getStackPointer()
	 */
	@Override
	public int getStackPointer()
	{
		return _stackPtr;
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#incStackPtr()
	 */
	@Override
	public void incStackPtr() throws VMError
	{
		if ( !isStackFull() )
		{
			_stackPtr++;
		}
		else
		{
			throw new VMError("incStackPtr - Stack is full", VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#decStackPtr()
	 */
	@Override
	public void decStackPtr() throws VMError
	{
		if ( !isStackEmpty() )
		{
			_stackPtr--;
		}
		else
		{
			throw new VMError("decStackPtr - Stack is empty", VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
	}

	/**
	 * Returns if Stack is empty
	 * 
	 * @return boolean
	 */
	public boolean isStackEmpty()
	{
		return _stackPtr <= STACK_EMPTY_LOC;
	}

	/**
	 * Returns if Stack is full
	 * 
	 * @return boolean
	 */
	public boolean isStackFull()
	{
		return _stackPtr == STACK_LIMIT;
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
	 * Sets value of program counter (set it to a position in meory where the next command will be run from)
	 * 
	 * @param location
	 */
	protected void setProgramCounter(final int location)
	{
		_programCtr = location;
	}

	/**
	 * Return value at location
	 * 
	 * @see Java.util.List
	 * 
	 * @param location
	 * @return
	 */
	protected int get(final int location)
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
	protected Integer set(final int location, final int value)
	{
		return _memoryStore.set(location, value);
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#resetStackPointer()
	 */
	@Override
	public void resetStackPointer()
	{
		_stackPtr = STACK_EMPTY_LOC;
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#getProgramCounter()
	 */
	@Override
	public int getProgramCounter()
	{
		return _programCtr;
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#incProgramCounter()
	 */
	@Override
	public void incProgramCounter()
	{
		_programCtr++;
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#decProgramCounter()
	 */
	@Override
	public void decProgramCounter()
	{
		_programCtr--;
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#resetProgramCounter()
	 */
	@Override
	public void resetProgramCounter()
	{
		_programCtr = STACK_LIMIT;
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#getProgramWriterPtr()
	 */
	@Override
	public int getProgramWriterPtr()
	{
		return _programWriter;
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#incProgramWriter()
	 */
	public void incProgramWriter()
	{
		_programWriter++;
	}

	/* (non-Javadoc)
	 * @see com.slickpath.mobile.android.simple.vm.machine.IStack#resetProgramWriter()
	 */
	@Override
	public void resetProgramWriter()
	{
		_programWriter = STACK_LIMIT;
	}
}