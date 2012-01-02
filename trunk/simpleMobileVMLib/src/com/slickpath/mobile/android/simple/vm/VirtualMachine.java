/**
 * 
 */
package com.slickpath.mobile.android.simple.vm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

// import android.util.Log;

/**
 * @author PJ
 *
 */
public class VirtualMachine extends Machine implements Instructions{
	
	// TODO private static final String TAG = VirtualMachine.class.getName();

    private final PrintStream _textWriter;
    private final InputStream _textReader;
    private final Scanner _textScanner;
    private int _numInstrsRun = 0;
    private boolean _bDebug = false;

    public VirtualMachine(final PrintStream writer, final InputStream reader)
    {
    	super();
    	_textWriter = writer;
    	_textReader = reader;
    	_textScanner = new Scanner(_textReader);
    }

    public VirtualMachine()
    {
    	super();
    	_textWriter = System.out;
    	_textReader = System.in;
    	_textScanner = new Scanner(_textReader);
    }
    
    public void setDebug(final boolean bDebug)
    {
    	_bDebug = bDebug;
    }

    //  Command Category : ARITHMATIC

    public void ADD() throws VMError
    {
        final int val1 = _pop();
        final int val2 = _pop();
        final int val3 = val1 + val2;
        PUSHC(val3);
    }

    public void SUB() throws VMError
    {
    	final int val1 = _pop();
    	final int val2 = _pop();
    	final int val3 = val1 - val2;
        PUSHC(val3);
    }

    public void MUL() throws VMError
    {
    	final int val1 = _pop();
    	final int val2 = _pop();
    	final int val3 = val1 * val2;
        PUSHC(val3);
    }

    public void DIV() throws VMError
    {
    	final int val1 = _pop();
    	final int val2 = _pop();
    	final int val3 = val1 / val2;
        PUSHC(val3);
    }

    public void NEG() throws VMError
    {
    	final int val1 = _pop();
    	final int val2 = 0 - val1;
        PUSHC(val2);
    }

    //  Command Category : BOOLEAN

    public void EQUAL() throws VMError
    {
        int equal = YES;
        final int val1 = _pop();
        final int val2 = _pop();

        if (val1 != val2)
        {
            equal = NO;
        }
        PUSHC(equal);
    }

    public void NOTEQL() throws VMError
    {
        int notEqual = NO;
        final int val1 = _pop();
        final int val2 = _pop();

        if (val1 != val2)
        {
            notEqual = YES;
        }
        PUSHC(notEqual);
    }

    public void GREATER() throws VMError
    {
        int greater = NO;
        final int val1 = _pop();
        final int val2 = _pop();

        if (val1 > val2)
        {
            greater = YES;
        }
        PUSHC(greater);
    }

    public void LESS() throws VMError
    {
        int less = NO;
        final int val1 = _pop();
        final int val2 = _pop();

        if (val1 < val2)
        {
            less = YES;
        }
        PUSHC(less);
    }

    public void GTREQL() throws VMError
    {
        int greaterOrEqual = NO;
        final int val1 = _pop();
        final int val2 = _pop();

        if (val1 >= val2)
        {
            greaterOrEqual = YES;
        }
        PUSHC(greaterOrEqual);
    }

    public void LSSEQL() throws VMError
    {
        int lessEqual = NO;
        final int val1 = _pop();
        final int val2 = _pop();

        if (val1 <= val2)
        {
            lessEqual = YES;
        }
        PUSHC(lessEqual);
    }

    public void NOT() throws VMError
    {
        int val = _pop();

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

    public void PUSHC(final int arg) throws VMError
    {
        if (isStackPointerValid())
        {
           incStackPtr();
           setValAtLocation(arg, getStackPointer());
        }
        else
        {
        	throw new VMError("PUSHC", VMError.VM_ERROR_TYPE_STACK_LIMIT);
        }
    }

    public void POP() throws VMError
    {
    	final int locationVal = _pop();
    	final int arg = _pop();
        setValAtLocation(arg, locationVal);
    }

    public void POPC(final int locationVal) throws VMError
    {
    	final int arg = _pop();
        setValAtLocation(arg, locationVal);
    }

    //  Command Category : INPUT/OUTPUT

    public void RDCHAR() throws VMError
    {
		try {
			final char ch = (char)_textReader.read();
			final int iAsciiValue = (int)ch;
	        PUSHC(iAsciiValue);
		} catch (IOException e) {
			throw new VMError("RDCHAR", e, VMError.VM_ERROR_TYPE_IO);
		}
    }

    public void WRCHAR() throws VMError
    {
    	final int value = _pop();
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
    	final int value = _pop();
        final String sOut = Integer.toString(value);
        _textWriter.println(sOut);
        debug("WRINT" + sOut);
    }

    //  Command Category : CONTROL

    public void BRANCH(final int locationVal) throws VMError
    {
        debug("--BR=" + locationVal);
        _branch(locationVal);
        debug("--BR=" + getProgramCounter());
    }

    public void JUMP() throws VMError
    {
        _jump();
    }

    public void BREQL(final int locationVal) throws VMError
    {
    	final int val = _pop();
        if (val == 0)
        {
            BRANCH(locationVal);
        }
    }

    public void BRLSS(final int locationVal) throws VMError
    {
    	final int val = _pop();
        if (val < 0)
        {
            BRANCH(locationVal);
        }
    }

    public void BRGTR(final int locationVal) throws VMError
    {
    	final int val = _pop();
        if (val > 0)
        {
            BRANCH(locationVal);
        }
    }

    //  Command Category : MISC

    public void CONTENTS() throws VMError
    {
    	final int locationVal = _pop();
    	final int val = getValueAt(locationVal);
        PUSHC(val);
    }

    public void HALT()
    {
    	// TODO
    }

    //   EXECUTION

    public int getProgramMemoryLoc()
    {
        return STACK_LIMIT;
    }

    public void addInstruction(final int instruction, final List<Integer> instructionParams) throws VMError
    {
        if (instruction < 1000)
        {
            setValAt_REG_ProgramWtr(instruction);
            setValAt_REG_ProgramWtr(0);
        }
        if (instruction >= 1000)
        {
            if ( instructionParams != null )
            {
                setValAt_REG_ProgramWtr(instruction);
                for(int instPram : instructionParams)
                {
                    setValAt_REG_ProgramWtr(instPram);
                }
            }
            else
            {
            	throw new VMError("addInstruction", VMError.VM_ERROR_BAD_PARAMS);
            }
        }
    }

    private void setValAt_REG_ProgramWtr(final int arg) throws VMError
    {
        setValAtLocation(arg, getProgramWriter());
        incProgramWriter();
    }

    protected void setValAtLocation(final int arg, final int locationVal) throws VMError
    {
        setValueAt(arg, locationVal);
    }
    public void addInstructions(final List<Integer> instructions, final List<List<Integer>> instructionParams) throws VMError
    {
        if (instructions != null )
        {
            if (instructionParams == null || instructionParams.size() == instructions.size())
            {
                int location = 0;
                for (int instruction : instructions)
                {
                    addInstruction(instruction, instructionParams.get(location++));
                }
            }
            else
            {
            	throw new VMError("addInstructions instructionParams", VMError.VM_ERROR_BAD_PARAMS);
            }
        }
        else
        {
        	throw new VMError("addInstructions instructions", VMError.VM_ERROR_BAD_PARAMS);
        }
    }

    //public boolean runNextInstruction(out int line)
    public boolean runNextInstruction(final LineNumber... line) throws VMError
    {
        boolean bReturn = false;
        resetProgramWriter();

        if ( line.length > 0)
        {
        	line[0].set((getProgramCounter() - STACK_LIMIT)/2);
        }
        final int instructionVal = getValueAt(getProgramCounter());
        incProgramWriter();
        runCommand(instructionVal);

        if (instructionVal == BaseInstructionSet._HALT)
        {
            _numInstrsRun = 0;
            resetProgramWriter();
            resetStackPointer();
            bReturn = true;
        }
        return bReturn;
    }

    // Mutable Line number ( to make it an "out" variable )
    public void runInstructions(final int numInstrsToRun, final LineNumber... line) throws VMError
    {
        int numInstrsRun = 0;
        resetProgramWriter();

        int instructionVal = BaseInstructionSet._BEGIN;

        while (instructionVal != BaseInstructionSet._HALT && getProgramCounter() < MAX_MEMORY && numInstrsRun < numInstrsToRun)
        {
            numInstrsRun++;
            instructionVal = getValueAt(getProgramCounter());
            incProgramCounter();
            
            runCommand(instructionVal);
        }
        if ( line.length > 0)
        {
        	line[0].set((getProgramCounter() - 2 - STACK_LIMIT)/2);
        }
    }

    public void runInstructions() throws VMError
    {
        _numInstrsRun = 0;
        resetProgramWriter();

        int instructionVal = BaseInstructionSet._BEGIN;

        while (instructionVal != BaseInstructionSet._HALT && getProgramCounter() < MAX_MEMORY)
        {
            instructionVal = getValueAt(getProgramCounter());
            incProgramCounter();
            runCommand(instructionVal);
        }
        resetProgramCounter();
        resetStackPointer();
    }

    private void runCommand(final int command) throws VMError
    {
        if (_bDebug)
        {
        	final StringBuffer sLineCount = new StringBuffer("[");
        	sLineCount.append(_numInstrsRun).append(']');
        	final StringBuffer sParam = new StringBuffer(" Line=");
        	sParam.append(getProgramCounter() - 1);
        	sLineCount.append("CMD=").append(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(command));
            debug(sLineCount.toString());
            if (command >= 1000)
            {
            	sParam.append(" PARAM=").append(getValueAt(getProgramCounter()));
            }
            debug(sParam.toString());
        }

        switch (command)
        {
            case _ADD:
                ADD();
                incProgramCounter();
                break;
            case _SUB:
                SUB();
                incProgramCounter();
                break;
            case _MUL:
                MUL();
                incProgramCounter();
                break;
            case _DIV:
                DIV();
                incProgramCounter();
                break;
            case _NEG:
                NEG();
                incProgramCounter();
                break;
            case _EQUAL:
                EQUAL();
                incProgramCounter();
                break;
            case _NOTEQL:
                NOTEQL();
                incProgramCounter();
                break;
            case _GREATER:
                GREATER();
                incProgramCounter();
                break;
            case _LESS:
                LESS();
                incProgramCounter();
                break;
            case _GTREQL:
                GTREQL();
                incProgramCounter();
                break;
            case _LSSEQL:
                LSSEQL();
                incProgramCounter();
                break;
            case _NOT:
                NOT();
                incProgramCounter();
                break;
            case _POP:
                POP();
                incProgramCounter();
                break;
            case _JUMP:
                JUMP();
                //incProgramCounter();
                break;
            case _RDCHAR:
                RDCHAR();
                incProgramCounter();
                break;
            case _RDINT:
                RDINT();
                incProgramCounter();
                break;
            case _WRCHAR:
                WRCHAR();
                incProgramCounter();
                break;
            case _WRINT:
                WRINT();
                incProgramCounter();
                break;
            case _CONTENTS:
                CONTENTS();
                incProgramCounter();
                break;
            case _HALT:
                HALT();
                incProgramCounter();
                break;
            // 1 PARAM COMMANDS
            case _PUSHC:
                PUSHC(getValueAt(getProgramCounter()));
                incProgramCounter();
                break;
            case _PUSH:
                PUSH(getValueAt(getProgramCounter()));
                incProgramCounter();
                break;
            case _POPC:
                POPC(getValueAt(getProgramCounter()));
                incProgramCounter();
                break;
            case _BRANCH:
                BRANCH(getValueAt(getProgramCounter()));
                incProgramCounter();
                break;
            case _BREQL:
                BREQL(getValueAt(getProgramCounter()));
                incProgramCounter();
                break;
            case _BRLSS:
                BRLSS(getValueAt(getProgramCounter()));
                incProgramCounter();
                break;
            case _BRGTR:
                BRGTR(getValueAt(getProgramCounter()));
                incProgramCounter();
                break;
            default:
            	throw new VMError("runCommand :" + command, VMError.VM_ERROR_BAD_UNKNOWN_COMMAND);
        }
    }

    private void debug(final String sText)
    {
        // TODO Log.d(TAG, sText);
    }
}

