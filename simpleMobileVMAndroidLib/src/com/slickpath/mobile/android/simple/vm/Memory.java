package com.slickpath.mobile.android.simple.vm;

import java.util.ArrayList;
import java.util.List;

public class Memory {

	private final int STACK_EMPTY_LOC = -1;
	public static final int EMPTY_MEMORY_LOC = 999999;

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
	// TODO - make private
	protected int _programCtr = STACK_LIMIT;
	// TODO - make private
	protected int _programWriter = STACK_LIMIT;
	// TODO - make private ???
	protected final List<Integer> _memory;

	public Memory() {
		super();
		_memory = new ArrayList<Integer>(MAX_MEMORY);
		// initialize every piece of memory to EMPTY
		for (int i = 0; i < MAX_MEMORY; i++)
		{
			_memory.add(EMPTY_MEMORY_LOC);
		}
	}

	public int getStackPointer()
	{
		return _stackPtr;
	}

	protected void incStackPtr() throws VMError
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

	protected void decStackPtr() throws VMError
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

	protected void resetStackPointer()
	{
		_stackPtr = STACK_EMPTY_LOC;
	}

	protected boolean isStackPointerValid()
	{
		return _stackPtr < STACK_LIMIT;
	}

	public boolean isStackEmpty()
	{
		return _stackPtr <= STACK_EMPTY_LOC;
	}

	public boolean isStackFull()
	{
		return _stackPtr == STACK_LIMIT;
	}

	public List<Integer> memoryDump()
	{
		return _memory;
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