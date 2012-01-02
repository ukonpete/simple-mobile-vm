/**
 * 
 */
package com.slickpath.mobile.android.simple.vm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// import android.util.Log;

/**
 * @author PJ
 *
 */
public class VirtualMachine implements Instructions{
	
	// TODO private static final String TAG = VirtualMachine.class.getName();

    public static final int EMPTY_MEMORY_LOC = 999999;
    public static final int MAX_MEMORY = 500;
    public static final int STACK_LIMIT = MAX_MEMORY / 2;
    public static final int YES = 1;
    public static final int NO = 0;
    
    private int _stackPtr = -1;
    private int _programCtr = STACK_LIMIT;
    private int _programWriter = STACK_LIMIT;

    private List<Integer> _memory = null;

    private final PrintStream _textWriter;
    private final InputStream _textReader;
    private final Scanner _textScanner;
    private int _numInstrsRun = 0;
    private boolean _bDebug = false;

    public VirtualMachine(final PrintStream writer, final InputStream reader)
    {
    	_textWriter = writer;
    	_textReader = reader;
    	_textScanner = new Scanner(_textReader);
    	initMemory();    	
    }

    public VirtualMachine()
    {
    	_textWriter = System.out;
    	_textReader = System.in;
    	_textScanner = new Scanner(_textReader);
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
        	_memory.set(i++, EMPTY_MEMORY_LOC);
        }
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
    	final int val = (int)_memory.get(location);
        PUSHC(val);
    }

    public void PUSHC(final int arg) throws VMError
    {
        if (_stackPtr < STACK_LIMIT)
        {
           _memory.set(arg, ++_stackPtr);
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
    	final int tempLocationVal = (locationVal * 2) + STACK_LIMIT;
        if (tempLocationVal >= STACK_LIMIT)
        {
            debug("--BR=" + tempLocationVal);
            _programCtr = tempLocationVal;
            debug("--BR=" + _programCtr);
        }
        else
        {
        	throw new VMError("BRANCH", VMError.VM_ERROR_TYPE_STACK_LIMIT);
        }
    }

    public void JUMP() throws VMError
    {
    	final int locationVal = _pop();
        debug("--JMP=" + locationVal);
        _programCtr = (locationVal * 2) + STACK_LIMIT;
        debug("--JMP=" + _programCtr);
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
    	_programWriter = STACK_LIMIT;

        if ( line.length > 0)
        {
        	line[0].set((_programCtr - STACK_LIMIT)/2);
        }
        final int instructionVal = getValueAt(_programCtr++);
        runCommand(instructionVal);

        if (instructionVal == BaseInstructionSet._HALT)
        {
            _numInstrsRun = 0;
            _programCtr = STACK_LIMIT;
            _stackPtr = 0;
            bReturn = true;
        }
        return bReturn;
    }

    // Mutable Line number ( to make it an "out" variable )
    public void runInstructions(final int numInstrsToRun, final LineNumber... line) throws VMError
    {
        int numInstrsRun = 0;
        _programWriter = STACK_LIMIT;

        int instructionVal = BaseInstructionSet._BEGIN;

        while (instructionVal != BaseInstructionSet._HALT && _programCtr < MAX_MEMORY && numInstrsRun < numInstrsToRun)
        {
            numInstrsRun++;
            instructionVal = getValueAt(_programCtr++);
            runCommand(instructionVal);
        }
        if ( line.length > 0)
        {
        	line[0].set((_programCtr - 2 - STACK_LIMIT)/2);
        }
    }

    public void runInstructions() throws VMError
    {
        _numInstrsRun = 0;
        _programWriter = STACK_LIMIT;

        int instructionVal = BaseInstructionSet._BEGIN;

        while (instructionVal != BaseInstructionSet._HALT && _programCtr < MAX_MEMORY)
        {
            instructionVal = getValueAt(_programCtr++);
            runCommand(instructionVal);
        }
        _programCtr = STACK_LIMIT;
        _stackPtr = 0;
    }

    // PRIVATE_METHODS
    private int getValueAt(final int locationVal) throws VMError
    {
        int returnVal = 0;
        if (locationVal < MAX_MEMORY)
        {
            returnVal = (int)_memory.get(locationVal);
        }
        else
        {
        	throw new VMError("getValueAt", VMError.VM_ERROR_TYPE_MAX_MEMORY);
        }
        return returnVal;
    }

    private int _pop() throws VMError
    {
        if (_stackPtr >= 0)
        {
        	final int returnVal = (int)_memory.get(_stackPtr--);
            // Reset every memory position we pop to 99999
           _memory.set(EMPTY_MEMORY_LOC, (int)(_stackPtr + 1));
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

    private void setValAt_REG_ProgramWtr(final int arg) throws VMError
    {
        setValAtLocation(arg, _programWriter++);
    }

    private void setValAtLocation(final int arg, final int locationVal) throws VMError
    {
        if (locationVal < MAX_MEMORY)
        {
           _memory.set(arg, locationVal);
        }
        else
        {
        	throw new VMError("setValAtLocation", VMError.VM_ERROR_TYPE_LOC_MAX_MEMORY);
        }
    }

    private void runCommand(final int command) throws VMError
    {
        if (_bDebug)
        {
        	final StringBuffer sLineCount = new StringBuffer("[");
        	sLineCount.append(_numInstrsRun).append(']');
        	final StringBuffer sParam = new StringBuffer(" Line=");
        	sParam.append(_programCtr - 1);
        	sLineCount.append("CMD=").append(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(command));
            debug(sLineCount.toString());
            if (command >= 1000)
            {
            	sParam.append(" PARAM=").append(getValueAt(_programCtr));
            }
            debug(sParam.toString());
        }

        switch (command)
        {
            case _ADD:
                ADD();
                _programCtr++;
                break;
            case _SUB:
                SUB();
                _programCtr++;
                break;
            case _MUL:
                MUL();
                _programCtr++;
                break;
            case _DIV:
                DIV();
                _programCtr++;
                break;
            case _NEG:
                NEG();
                _programCtr++;
                break;
            case _EQUAL:
                EQUAL();
                _programCtr++;
                break;
            case _NOTEQL:
                NOTEQL();
                _programCtr++;
                break;
            case _GREATER:
                GREATER();
                _programCtr++;
                break;
            case _LESS:
                LESS();
                _programCtr++;
                break;
            case _GTREQL:
                GTREQL();
                _programCtr++;
                break;
            case _LSSEQL:
                LSSEQL();
                _programCtr++;
                break;
            case _NOT:
                NOT();
                _programCtr++;
                break;
            case _POP:
                POP();
                _programCtr++;
                break;
            case _JUMP:
                JUMP();
                //_programCtr++;
                break;
            case _RDCHAR:
                RDCHAR();
                _programCtr++;
                break;
            case _RDINT:
                RDINT();
                _programCtr++;
                break;
            case _WRCHAR:
                WRCHAR();
                _programCtr++;
                break;
            case _WRINT:
                WRINT();
                _programCtr++;
                break;
            case _CONTENTS:
                CONTENTS();
                _programCtr++;
                break;
            case _HALT:
                HALT();
                _programCtr++;
                break;
            // 1 PARAM COMMANDS
            case _PUSHC:
                PUSHC(getValueAt(_programCtr++));
                break;
            case _PUSH:
                PUSH(getValueAt(_programCtr++));
                break;
            case _POPC:
                POPC(getValueAt(_programCtr++));
                break;
            case _BRANCH:
                BRANCH(getValueAt(_programCtr++));
                break;
            case _BREQL:
                BREQL(getValueAt(_programCtr++));
                break;
            case _BRLSS:
                BRLSS(getValueAt(_programCtr++));
                break;
            case _BRGTR:
                BRGTR(getValueAt(_programCtr++));
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

