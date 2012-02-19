package com.slickpath.mobile.android.simple.vm.machine;

import com.slickpath.mobile.android.simple.vm.VMError;

/**
 * @author PJ
 * 
 */
public interface IStack {

	/**
	 * return the current pointer to the next instruction to be executed
	 * 
	 * @return int
	 */
	public abstract int getProgramCounter();

	/**
	 * increment the counter to the next position in memory
	 * 
	 */
	public abstract void incProgramCounter();

	/**
	 * decrement the counter to the previous position in memory
	 * 
	 */
	public abstract void decProgramCounter();

	/**
	 * reset the program counter to the beginning
	 * 
	 */
	public abstract void resetProgramCounter();

	/**
	 * get the current pointer in memory to where the next instruction will be written to
	 * 
	 * @return int
	 */
	public abstract int getProgramWriterPtr();

	/**
	 * reset the pointer to start of program memory
	 * 
	 */
	public abstract void resetProgramWriter();

	/**
	 * get the current location the top of the stack
	 * 
	 * @return int
	 */
	public abstract int getStackPointer();

	/**
	 * increment the stack pointer to the next location in memory
	 * 
	 * @throws VMError
	 */
	public abstract void incStackPtr() throws VMError;

	/**
	 * decrement the stack pointer to the previous location in memory
	 * 
	 * @throws VMError
	 */
	public abstract void decStackPtr() throws VMError;

	/**
	 * reset the stack pointer to the beginning
	 * 
	 */
	public abstract void resetStackPointer();

}