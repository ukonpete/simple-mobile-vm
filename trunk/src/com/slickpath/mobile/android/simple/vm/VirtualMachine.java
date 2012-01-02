/**
 * 
 */
package com.slickpath.mobile.android.simple.vm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.util.Log;

import com.slickpath.mobile.android.simple.vm.util.VMUtil;

/**
 * @author PJ
 *
 */
public class VirtualMachine implements Instructions{
	
	private static final String TAG = VirtualMachine.class.getName();

    private int _stackPtr = -1;
    private int _stackLimit = VMUtil.STACK_LIMIT;
    private int _programCtr = VMUtil.STACK_LIMIT;
    private int _programWriter = VMUtil.STACK_LIMIT;

    private List<Integer> _memory = null;

    PrintStream _textWriter;
    InputStream _textReader;
    Scanner _textScanner;
    private int _numInstrsRun = 0;
    private boolean _bDebug = false;

    public VirtualMachine(PrintStream writer, InputStream reader) throws IOException
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

    public void connect(PipedInputStream stream) throws IOException
    {
    	_textWriter.flush();
    	_textWriter.close();
    }

	/**
	 * 
	 */
	private void initMemory() {
		_memory = new ArrayList<Integer>(VMUtil.MAX_MEMORY);
        // initialize every piece of memory to EMPTY
        for (int i = 0; i < VMUtil.MAX_MEMORY; i++)
        {
        	_memory.set(i++, VMUtil.EMPTY_MEMORY_LOC);
        }
	}
    
    //  Command Category : ARITHMATIC

    public void ADD()
    {
        int val1 = _pop();
        int val2 = _pop();
        int val3 = val1 + val2;
        PUSHC(val3);
    }

    public void SUB()
    {
        int val1 = _pop();
        int val2 = _pop();
        int val3 = val1 - val2;
        PUSHC(val3);
    }

    public void MUL()
    {
        int val1 = _pop();
        int val2 = _pop();
        int val3 = val1 * val2;
        PUSHC(val3);
    }

    public void DIV()
    {
        int val1 = _pop();
        int val2 = _pop();
        int val3 = val1 / val2;
        PUSHC(val3);
    }

    public void NEG()
    {
        int val1 = _pop();
        int val2 = 0 - val1;
        PUSHC(val2);
    }

    //  Command Category : BOOLEAN

    public void EQUAL()
    {
        int equal = VMUtil.YES;
        int val1 = _pop();
        int val2 = _pop();

        if (val1 != val2)
        {
            equal = VMUtil.NO;
        }
        PUSHC(equal);
    }

    public void NOTEQL()
    {
        int notEqual = VMUtil.NO;
        int val1 = _pop();
        int val2 = _pop();

        if (val1 != val2)
        {
            notEqual = VMUtil.YES;
        }
        PUSHC(notEqual);
    }

    public void GREATER()
    {
        int greater = VMUtil.NO;
        int val1 = _pop();
        int val2 = _pop();

        if (val1 > val2)
        {
            greater = VMUtil.YES;
        }
        PUSHC(greater);
    }

    public void LESS()
    {
        int less = VMUtil.NO;
        int val1 = _pop();
        int val2 = _pop();

        if (val1 < val2)
        {
            less = VMUtil.YES;
        }
        PUSHC(less);
    }

    public void GTREQL()
    {
        int greaterOrEqual = VMUtil.NO;
        int val1 = _pop();
        int val2 = _pop();

        if (val1 >= val2)
        {
            greaterOrEqual = VMUtil.YES;
        }
        PUSHC(greaterOrEqual);
    }

    public void LSSEQL()
    {
        int lessEqual = VMUtil.NO;
        int val1 = _pop();
        int val2 = _pop();

        if (val1 <= val2)
        {
            lessEqual = VMUtil.YES;
        }
        PUSHC(lessEqual);
    }

    public void NOT()
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

    public void PUSH(int location )
    {
        int val = (int)_memory.get(location);
        PUSHC(val);
    }

    public void PUSHC(int arg)
    {
        if (_stackPtr < _stackLimit)
        {
           _memory.set(arg, ++_stackPtr);
        }
        else
        {
            // TODO
        	// throw new Exception("PUSHC");
        }
    }

    public void POP()
    {
        int locationVal = _pop();
        int arg = _pop();
        setValAtLocation(arg, locationVal);
    }

    public void POPC(int locationVal)
    {
        int arg = _pop();
        setValAtLocation(arg, locationVal);
    }

    //  Command Category : INPUT/OUTPUT

    public void RDCHAR()
    {
        // TODO
    	char ch;
		try {
			ch = (char)_textReader.read();
	        int iAsciiValue = (int)ch;
	        PUSHC(iAsciiValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void WRCHAR()
    {
        // TODO
    	int value = _pop();
        String sVal = Integer.toString(value);
        _textWriter.print(sVal);
        if ( !_textWriter.equals(System.out))
        {
        	System.out.print(sVal);
        }
    }

    public void RDINT()
    {
        // TODO: handle parsing error
    	int val = _textScanner.nextInt();
        PUSHC(val);
    }

    public void WRINT() 
    {
        // TODO: handle parsing error
        int value = _pop();
        String sOut = "" + value;
        _textWriter.println(sOut);
        if ( !_textWriter.equals(System.out))
        {
        	System.out.println(sOut);
        }
    }

    //  Command Category : CONTROL

    public void BRANCH(int locationVal)
    {
        locationVal = (locationVal * 2) + VMUtil.STACK_LIMIT;
        if (locationVal >= _stackLimit)
        {
            debug("--BR=" + locationVal);
            _programCtr = locationVal;
            debug("--BR=" + _programCtr);
        }
        else
        {
            // TODO
        	// throw new Exception("BRANCH");
        }
    }

    public void JUMP()
    {
        int locationVal = _pop();
        debug("--JMP=" + locationVal);
        _programCtr = (locationVal * 2) + VMUtil.STACK_LIMIT;
        debug("--JMP=" + _programCtr);
    }

    public void BREQL(int locationVal)
    {
        int val = _pop();
        if (val == 0)
        {
            BRANCH(locationVal);
        }
    }

    public void BRLSS(int locationVal)
    {
        int val = _pop();
        if (val < 0)
        {
            BRANCH(locationVal);
        }
    }

    public void BRGTR(int locationVal)
    {
        int val = _pop();
        if (val > 0)
        {
            BRANCH(locationVal);
        }
    }

    //  Command Category : MISC

    public void CONTENTS()
    {
        int locationVal = _pop();
        int val = getValueAt(locationVal);
        PUSHC(val);
    }

    public void HALT()
    {

    }

    //   EXECUTION

    public int getProgramMemoryLoc()
    {
        return VMUtil.STACK_LIMIT;
    }

    public void addInstruction(int instruction, List<Integer> instructionParams)
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
        }
    }

    public void addInstructions(List<Integer> instructions, List<List<Integer>> instructionParams)
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
        }
    }

    //public boolean runNextInstruction(out int line)
    // TODO - line is an OUT parameter
    public boolean runNextInstruction(Integer line)
    {
        _programWriter = VMUtil.STACK_LIMIT;

        line = (_programCtr - VMUtil.STACK_LIMIT)/2;
        int instructionVal = getValueAt(_programCtr++);
        runCommand(instructionVal);

        if (instructionVal == BaseInstructionSet._HALT)
        {
            _numInstrsRun = 0;
            _programCtr = VMUtil.STACK_LIMIT;
            _stackPtr = 0;
            return true;
        }
        return false;
    }

   // public void runInstructions(int numInstrsToRun, out int line
    // TODO - line is an OUT parameter
    public void runInstructions(int numInstrsToRun, Integer line)
    {
        int numInstrsRun = 0;
        _programWriter = VMUtil.STACK_LIMIT;

        int instructionVal = BaseInstructionSet._BEGIN;

        while (instructionVal != BaseInstructionSet._HALT && _programCtr < VMUtil.MAX_MEMORY && numInstrsRun < numInstrsToRun)
        {
            numInstrsRun++;
            instructionVal = getValueAt(_programCtr++);
            runCommand(instructionVal);
        }
        line = (_programCtr - 2 - VMUtil.STACK_LIMIT)/2;
    }

    public void runInstructions()
    {
        _numInstrsRun = 0;
        _programWriter = VMUtil.STACK_LIMIT;

        int instructionVal = BaseInstructionSet._BEGIN;

        while (instructionVal != BaseInstructionSet._HALT && _programCtr < VMUtil.MAX_MEMORY)
        {
            instructionVal = getValueAt(_programCtr++);
            runCommand(instructionVal);
        }
        _programCtr = VMUtil.STACK_LIMIT;
        _stackPtr = 0;
    }

    // PRIVATE_METHODS
    private int getValueAt(int locationVal)
    {
        int returnVal = 0;
        if (locationVal < VMUtil.MAX_MEMORY)
        {
            returnVal = (int)_memory.get(locationVal);
        }
        else
        {
            // TODO
        	// throw new Exception("getValueAt");
        }
        return returnVal;
    }

    private int _pop()
    {
        if (_stackPtr >= 0)
        {
            int returnVal = (int)_memory.get(_stackPtr--);
            // Reset every memory position we pop to 99999
           _memory.set(VMUtil.EMPTY_MEMORY_LOC, (int)(_stackPtr + 1));
            return returnVal;
        }
        else
        {
            // TODO
        	// throw new Exception("_pop");
        	return -1;
        }
    }

    public List<Integer> memoryDump()
    {
        return _memory;
    }

    private void setValAt_REG_ProgramWtr(int arg)
    {
        setValAtLocation(arg, _programWriter++);
    }

    private void setValAtLocation(int arg, int locationVal)
    {
        if (locationVal < VMUtil.MAX_MEMORY)
        {
           _memory.set(arg, locationVal);
        }
        else
        {
            // TODO 
        	// throw new Exception("setValAtLocation");
        }
    }

    private void runCommand(int command)
    {
        if (_bDebug)
        {
            String sLineCount = "[" + _numInstrsRun++ + "]";
            String sParam = " Line=" + (_programCtr - 1);
            debug(sLineCount + "CMD=" + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(command));
            if (command >= 1000)
            {
                sParam += " PARAM=" + getValueAt(_programCtr);
            }
            debug(sParam);
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
        }
    }

    // TODO - make this a log
    private void debug(String sText)
    {
        Log.d(TAG, sText);
    }
}

