/**
 * 
 */
package com.slickpath.mobile.android.simple.vm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import android.util.Log;

/**
 * @author PJ
 *
 */
public class Machine extends Kernel{

	private static final String TAG = Machine.class.getName();

	protected final PrintStream _textWriter;
	protected final Scanner _textScanner;
	private final InputStream _textReader;

	protected boolean _bDebug = false;
	/**
	 * 
	 */
	public Machine(final PrintStream writer, final InputStream reader) {
		super();
		_textWriter = writer;
		_textReader = reader;
		_textScanner = new Scanner(_textReader);
	}

	public Machine()
	{
		super();
		_textWriter = System.out;
		_textReader = System.in;
		_textScanner = new Scanner(_textReader);
	}

	//  Command Category : ARITHMATIC

	public void ADD() throws VMError
	{
		final int val1 = pop();
		final int val2 = pop();
		final int val3 = val1 + val2;
		PUSHC(val3);
	}

	public void SUB() throws VMError
	{
		final int val1 = pop();
		final int val2 = pop();
		final int val3 = val1 - val2;
		PUSHC(val3);
	}

	public void MUL() throws VMError
	{
		final int val1 = pop();
		final int val2 = pop();
		final int val3 = val1 * val2;
		PUSHC(val3);
	}

	public void DIV() throws VMError
	{
		final int val1 = pop();
		final int val2 = pop();
		final int val3 = val1 / val2;
		PUSHC(val3);
	}

	public void NEG() throws VMError
	{
		final int val1 = pop();
		final int val2 = 0 - val1;
		PUSHC(val2);
	}

	//  Command Category : BOOLEAN

	public void EQUAL() throws VMError
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

	public void NOTEQL() throws VMError
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

	public void GREATER() throws VMError
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

	public void LESS() throws VMError
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

	public void GTREQL() throws VMError
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

	public void LSSEQL() throws VMError
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

	public void NOT() throws VMError
	{
		int val = pop();

		// longhand for val == 0?1:0
		if (val == 0)
		{
			val = 1;
		}
		else
		{
			val = 0;
		}

		PUSHC(val);
	}

	//  Command Category : STACK_MANIPULATION

	public void PUSH(final int location ) throws VMError
	{
		final int val = getValueAt(location);
		PUSHC(val);
	}

	public void PUSHC(final int value) throws VMError
	{
		if (isStackPointerValid())
		{
			push(value);
		}
		else
		{
			throw new VMError("PUSHC", VMError.VM_ERROR_TYPE_STACK_LIMIT);
		}
	}

	public void POP() throws VMError
	{
		final int location = pop();
		POPC(location);
	}

	public void POPC(final int location) throws VMError
	{
		final int value = pop();
		setValAtLocation(value, location);
	}

	//  Command Category : INPUT/OUTPUT

	public void RDCHAR() throws VMError
	{
		try {
			final char ch = (char)_textReader.read();
			final int iAsciiValue = ch;
			PUSHC(iAsciiValue);
		} catch (final IOException e) {
			throw new VMError("RDCHAR", e, VMError.VM_ERROR_TYPE_IO);
		}
	}

	public void WRCHAR() throws VMError
	{
		final int value = pop();
		final String sVal = Integer.toString(value);
		_textWriter.print(sVal);
		debug("WRCHAR: " + sVal);
	}

	public void RDINT()  throws VMError
	{
		final int val = _textScanner.nextInt();
		PUSHC(val);
	}

	public void WRINT()  throws VMError
	{
		final int value = pop();
		final String sOut = Integer.toString(value);
		_textWriter.println(sOut);
		debug("WRINT" + sOut);
	}

	//  Command Category : CONTROL

	public void BRANCH(final int location) throws VMError
	{
		debug("--BR=" + location);
		debug("--BR=" + getProgramCounter());
		branch(location);
		debug("--BR=" + getProgramCounter());
	}

	public void JUMP() throws VMError
	{
		jump();
	}

	public boolean BREQL(final int location) throws VMError
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

	public boolean BRLSS(final int location) throws VMError
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

	public boolean BRGTR(final int location) throws VMError
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

	//  Command Category : MISC

	public void CONTENTS() throws VMError
	{
		final int location = pop();
		final int val = getValueAt(location);
		PUSHC(val);
	}

	public void HALT()
	{
		debug(TAG, "HALT");
	}

	protected void setValAtLocation(final int value, final int location) throws VMError
	{
		setValueAt(value, location);
	}

	protected void debug(final String sText)
	{
		if ( _bDebug )
		{
			debug(TAG, sText);
		}
	}

	protected void debug(final String sTag, final String sText)
	{
		if ( _bDebug )
		{
			Log.d(sTag, sText);
		}
	}

	public void setDebug(final boolean bDebug)
	{
		_bDebug = bDebug;
	}
}
