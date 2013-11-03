/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.machine;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.slickpath.mobile.android.simple.vm.VMError;

/**
 * @author Pete Procopio
 * The Machine.  The calls that are made to execute instructions
 */
public class Machine extends Kernel{

	private static final String TAG = Machine.class.getName();

	/**
	 * Stream where output can be written to
	 */
	private final PrintStream _textWriter;
	/**
	 * Scanner where input can be read from
	 */
	private final Scanner _textScanner;
	/**
	 * Stream where input can be read from
	 */
	private final InputStream _textReader;

	/**
	 * Constructor
	 * Allows caller to pass in streams for both input and output
	 * 
	 */
	public Machine(final PrintStream writer, final InputStream reader) {
		super();
		_textWriter = writer;
		_textReader = reader;
		_textScanner = new Scanner(_textReader);
	}

	/**
	 * Constructor
	 * Uses default System.in and System.out form input and output
	 * 
	 */
	public Machine()
	{
		super();
		_textWriter = System.out;
		_textReader = System.in;
		_textScanner = new Scanner(_textReader);
	}

	/////////////////////////////////////////////////  Command Category : ARITHMATIC

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. Add them together
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void ADD() throws VMError
	{
		final int val1 = pop();
		final int val2 = pop();
		final int val3 = val1 + val2;
		PUSHC(val3);
	}

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. Subtracts 2nd from 1st
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void SUB() throws VMError
	{
		final int val1 = pop();
		final int val2 = pop();
		final int val3 = val1 - val2;
		PUSHC(val3);
	}

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. Multiplies them
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void MUL() throws VMError
	{
		final int val1 = pop();
		final int val2 = pop();
		final int val3 = val1 * val2;
		PUSHC(val3);
	}

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. Divides 2nd into 1st  (remainder is truncated)
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void DIV() throws VMError
	{
		final int val1 = pop();
		final int val2 = pop();
		final int val3 = val1 / val2;
		PUSHC(val3);
	}

	/**
	 * 1. Pop 1st value off the top of the stack
	 * 2. Negates value
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void NEG() throws VMError
	{
		final int val1 = pop();
		final int val2 = 0 - val1;
		PUSHC(val2);
	}

	/////////////////////////////////////////////////  Command Category : BOOLEAN

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. If equal 1 is pushed to top of stack if not 0 is push to top of stack
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void EQUAL() throws VMError
	{
		int equal = PUSHC_YES;
		final int val1 = pop();
		final int val2 = pop();

		if (val1 != val2)
		{
			equal = PUSHC_NO;
		}
		PUSHC(equal);
	}

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. If not equal 1 is pushed to top of stack if not 0 is push to top of stack
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void NOTEQL() throws VMError
	{
		int notEqual = PUSHC_NO;
		final int val1 = pop();
		final int val2 = pop();

		if (val1 != val2)
		{
			notEqual = PUSHC_YES;
		}
		PUSHC(notEqual);
	}

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. If 1st value is greater than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void GREATER() throws VMError
	{
		int greater = PUSHC_NO;
		final int val1 = pop();
		final int val2 = pop();

		if (val1 > val2)
		{
			greater = PUSHC_YES;
		}
		PUSHC(greater);
	}

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. If 1st value is less than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void LESS() throws VMError
	{
		int less = PUSHC_NO;
		final int val1 = pop();
		final int val2 = pop();

		if (val1 < val2)
		{
			less = PUSHC_YES;
		}
		PUSHC(less);
	}

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. If 1st value is greater than or equal to the 2nd than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void GTREQL() throws VMError
	{
		int greaterOrEqual = PUSHC_NO;
		final int val1 = pop();
		final int val2 = pop();

		if (val1 >= val2)
		{
			greaterOrEqual = PUSHC_YES;
		}
		PUSHC(greaterOrEqual);
	}

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. If 1st value is less than or equal to the 2nd than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void LSSEQL() throws VMError
	{
		int lessEqual = PUSHC_NO;
		final int val1 = pop();
		final int val2 = pop();

		if (val1 <= val2)
		{
			lessEqual = PUSHC_YES;
		}
		PUSHC(lessEqual);
	}

	/**
	 * 1. Pop 1st value off the top of the stack
	 * 2. If value is 0 then 1 is pushed to top of stack if not 0 is push to top of stack
	 * 3. Push result to top of stack
	 * 
	 * @throws VMError
	 */
	protected void NOT() throws VMError
	{
		int val = pop();

		// long-hand for val == 0?1:0
		if (val == 0)
		{
			val = PUSHC_YES;
		}
		else
		{
			val = PUSHC_NO;
		}

		PUSHC(val);
	}

	///////////////////////////////////////////////// Command Category : STACK_MANIPULATION

	/**
	 * Push the value at the location in memory to the top of the stack
	 * 
	 * @param location
	 * @throws VMError
	 */
	protected void PUSH(final int location ) throws VMError
	{
		final int val = getValueAt(location);
		PUSHC(val);
	}

	/**
	 * Push the value passed in to the top of the stack
	 * 
	 * @param location
	 * @throws VMError
	 */
	protected void PUSHC(final int value) throws VMError
	{
		push(value);
	}

	/**
	 * 1. Pop 1st two values off the top of the stack
	 * 2. save the 2nd value popped off in memory location indicated by the fist
	 * 
	 * @throws VMError
	 */
	protected void POP() throws VMError
	{
		final int location = pop();
		POPC(location);
	}

	/**
	 * 1. Pop 1st value at top of the stack
	 * 2. Set the value popped at the location in memory indicated by passed in value.
	 * 
	 * @param location
	 * @throws VMError
	 */
	protected void POPC(final int location) throws VMError
	{
		final int value = pop();
		setValueAt(value, location);
	}

	///////////////////////////////////////////////// Command Category : INPUT/OUTPUT

	/**
	 * 1. Read character from Input Stream
	 * 2. Push value to top of stack
	 * 3. If debug is enabled, write value to log.d
	 * 
	 * @throws VMError
	 */
	protected void RDCHAR() throws VMError
	{
		try {
			final char ch = (char)_textReader.read();
			final int iAsciiValue = ch;
			PUSHC(iAsciiValue);
		} catch (final IOException e) {
			throw new VMError("RDCHAR", e, VMError.VM_ERROR_TYPE_IO);
		}
	}

	/**
	 * 1. Pop character from top of stack
	 * 2. Write value to output Stream
	 * 3. If debug is enabled, write value to log.d
	 * 
	 * @throws VMError
	 */
	protected void WRCHAR() throws VMError
	{
		final int value = pop();
		final char sVal = (char)value;
		_textWriter.print(sVal);
		debugVerbose(TAG, "WRCHAR: " + sVal);
	}

	/**
	 * 1. Read int from Input Stream
	 * 2. Push value to top of stack
	 * 3. If debug is enabled, write value to log.d
	 * 
	 * @throws VMError
	 */
	protected void RDINT()  throws VMError
	{
		final int val = _textScanner.nextInt();
		PUSHC(val);
	}

	/**
	 * 1. Pop int from top of stack
	 * 2. Write value to output Stream
	 * 3. If debug is enabled, write value to log.d
	 * 
	 * @throws VMError
	 */
	protected void WRINT()  throws VMError
	{
		final int value = pop();
		final String sOut = Integer.toString(value);
		_textWriter.println(sOut);
		debugVerbose(TAG, "WRINT" + sOut);
	}

	////////////////////////////////////////////////////  Command Category : CONTROL

	/**
	 * Set the program counter (the pointer to the next instruction to be run) to the location that is passed in
	 * 
	 * @param location
	 * @throws VMError
	 */
	protected void BRANCH(final int location) throws VMError
	{
		debugVerbose(TAG, "--BR=" + location);
		branch(location);
	}

	/**
	 * Set the program counter (the pointer to the next instruction to be run) to the location that the top of the stack indicates. The value used is popped from the stack
	 * 
	 * @throws VMError
	 */
	protected void JUMP() throws VMError
	{
		jump();
	}

	/**
	 * 
	 * 1. Pop 1st value at top of the stack
	 * 2. Branch to passed in location if value == 0
	 * 
	 * @see BRANCH
	 * 
	 * @param location
	 * @return boolean - if branch occurred
	 * @throws VMError
	 */
	protected boolean BREQL(final int location) throws VMError
	{
		final int val = pop();
		boolean bBranched = false;
		if (val == 0)
		{
			BRANCH(location);
			bBranched = true;
		}
		return bBranched;
	}

	/**
	 * 1. Pop 1st value at top of the stack
	 * 2. Branch to passed in location if value < 0
	 * 
	 * @see BRANCH
	 * 
	 * @param location
	 * @return boolean - if branch occurred
	 * @throws VMError
	 */
	protected boolean BRLSS(final int location) throws VMError
	{
		final int val = pop();
		boolean bBranched = false;
		if (val < 0)
		{
			BRANCH(location);
			bBranched = true;
		}
		return bBranched;
	}

	/**
	 * 1. Pop 1st value at top of the stack
	 * 2. Branch to passed in location if value > 0
	 *
	 * @see BRANCH
	 * 
	 * @param location
	 * @return boolean - if branch occurred
	 * @throws VMError
	 */
	protected boolean BRGTR(final int location) throws VMError
	{
		final int val = pop();
		boolean bBranched = false;
		if (val > 0)
		{
			BRANCH(location);
			bBranched = true;
		}
		return bBranched;
	}

	///////////////////////////////////////////////////  Command Category : MISC

	/**
	 * 1. Pop 1st value at top of the stack
	 * 2. Get value at memory location indicated by value that was popped
	 * 3. Push the value to stop of the stack
	 * 
	 * @throws VMError
	 */
	protected void CONTENTS() throws VMError
	{
		final int location = pop();
		final int val = getValueAt(location);
		PUSHC(val);
	}

	/**
	 * Stop program - NO-OP instruction for Machine.java
	 * 
	 * This will send info to Log if debug is enabled
	 * 
	 */
	protected void HALT()
	{
		debug(TAG, "HALT");
	}
}
