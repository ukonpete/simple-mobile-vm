/**
 * 
 */
package com.slickpath.mobile.android.simple.vm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.util.Log;

// import android.util.Log;

/**
 * @author PJ
 *
 */
public class VirtualMachine extends Machine implements Instructions{
	
	private static final String TAG = VirtualMachine.class.getName();

    private final PrintStream _textWriter;
    private final InputStream _textReader;
    private final Scanner _textScanner;
    private int _numInstrsRun = 0;
    private boolean _bDebug = false;
    
    private Context _context;

    public VirtualMachine(final PrintStream writer, final InputStream reader, Context context)
    {
    	super();
    	_textWriter = writer;
    	_textReader = reader;
    	_textScanner = new Scanner(_textReader);
    	_context = context;
    }

    public VirtualMachine(Context context)
    {
    	super();
    	_textWriter = System.out;
    	_textReader = System.in;
    	_textScanner = new Scanner(_textReader);
    	_context = context;
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

    public void PUSHC(final int value) throws VMError
    {
        if (isStackPointerValid())
        {
           incStackPtr();
           setValAtLocation(value, getStackPointer());
        }
        else
        {
        	throw new VMError("PUSHC", VMError.VM_ERROR_TYPE_STACK_LIMIT);
        }
    }

    public void POP() throws VMError
    {
    	final int location = _pop();
    	final int value = _pop();
        setValAtLocation(value, location);
    }

    public void POPC(final int location) throws VMError
    {
    	final int value = _pop();
        setValAtLocation(value, location);
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
        //debug("WRINT" + sOut);
    }

    //  Command Category : CONTROL

    public void BRANCH(final int location) throws VMError
    {
        debug("--BR=" + location);
        _branch(location);
        debug("--BR=" + getProgramCounter());
    }

    public void JUMP() throws VMError
    {
        _jump();
    }

    public boolean BREQL(final int location) throws VMError
    {
    	final int val = _pop();
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
    	final int val = _pop();
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
    	final int val = _pop();
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
    	final int location = _pop();
    	final int val = getValueAt(location);
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
            // $$$$ debug("AI ins="+ instruction + " params=X");
        }
        if (instruction >= 1000)
        {
            if ( instructionParams != null )
            {
                String sVal = "";
            	setValAt_REG_ProgramWtr(instruction);
                for(int instPram : instructionParams)
                {
                    sVal += Integer.toString(instPram) + ":";
                	setValAt_REG_ProgramWtr(instPram);
                }
                debug("AI ins="+ instruction + " params=" + sVal);
            }
            else
            {
            	throw new VMError("addInstruction", VMError.VM_ERROR_BAD_PARAMS);
            }
        }
    }

    private void setValAt_REG_ProgramWtr(final int value) throws VMError
    {
        setValAtLocation(value, getProgramWriter());
        incProgramWriter();
    }

    protected void setValAtLocation(final int value, final int location) throws VMError
    {
        setValueAt(value, location);
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

        line[0].set((getProgramCounter() - STACK_LIMIT)/2);
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
        dumpMem("1");
    	int numInstrsRun = 0;
        resetProgramWriter();

    	int last = -1;
        int instructionVal = BaseInstructionSet._BEGIN;

        try
        {
        	while (instructionVal != BaseInstructionSet._HALT && getProgramCounter() < MAX_MEMORY && numInstrsRun < numInstrsToRun)
	        {
        		last = getProgramCounter();
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
        catch(VMError vme)
        {
        	System.out.println("PROG_CTR=" + getProgramCounter());
        	System.out.println("LAST_PROG_CTR=" + last);
        	dumpMem("2");
        	throw vme;
        }
    	System.out.println("PROG_CTR=" + getProgramCounter());
    	System.out.println("LAST_PROG_CTR=" + last);
        dumpMem("3");
    }

	/**
	 * 
	 */
	private void dumpMem(String sAppend) {
		String FILENAME = "memDump" + sAppend + ".txt";
		String sData = "";
		
		for(int i = 1; i < MAX_MEMORY; i+=2)
		{
			try {
				int parm = getValueAt(i);
				int command = getValueAt(i-1);
				sData += command + " " + parm + "\r\n";
			}
			catch (VMError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    FileOutputStream fos;
		try {
			fos = _context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		    fos.write(sData.getBytes());
		    fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void runInstructions() throws VMError
    {
        dumpMem("1");
    	_numInstrsRun = 0;
        resetProgramWriter();

        int instructionVal = BaseInstructionSet._BEGIN;
        int last = -1;

        try
        {
	        while (instructionVal != BaseInstructionSet._HALT && getProgramCounter() < MAX_MEMORY)
	        {
	        	last = getProgramCounter();
	        	instructionVal = getValueAt(getProgramCounter());
	        	System.out.println("-PROG_CTR=" + getProgramCounter() + " line=" + ((getProgramCounter() - STACK_LIMIT)/2) + " inst=" + instructionVal);
	        	incProgramCounter();
	            runCommand(instructionVal);
	        }
        	System.out.println("PROG_CTR=" + getProgramCounter());
        	System.out.println("LAST_PROG_CTR=" + last);
	        dumpMem("3");
	        resetProgramCounter();
	        resetStackPointer();
        }
        catch(VMError vme)
        {
        	System.out.println("+PROG_CTR=" + getProgramCounter());
        	System.out.println("+LAST_PROG_CTR=" + last);
        	dumpMem("2");
        	throw vme;
        }
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

        boolean bBranched = false;
        
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
            	bBranched = BREQL(getValueAt(getProgramCounter()));
                if ( !bBranched )
                {
                	incProgramCounter();
                }
                break;
            case _BRLSS:
            	bBranched = BRLSS(getValueAt(getProgramCounter()));
                if ( !bBranched )
                {
                	incProgramCounter();
                }
                break;
            case _BRGTR:
            	bBranched = BRGTR(getValueAt(getProgramCounter()));
                if ( !bBranched )
                {
                	incProgramCounter();
                }
                break;
            default:
            	throw new VMError("runCommand :" + command, VMError.VM_ERROR_BAD_UNKNOWN_COMMAND);
        }
    }

    private void debug(final String sText)
    {
        Log.d(TAG, sText);
    }
}

