/**
 * 
 */
package com.slickpath.mobile.android.simple.vm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.instructions.Instructions;
import com.slickpath.mobile.android.simple.vm.util.Command;
import com.slickpath.mobile.android.simple.vm.util.CommandSet;
import com.slickpath.mobile.android.simple.vm.util.LineNumber;

import android.content.Context;

// import android.util.Log;

/**
 * @author PJ
 *
 */
public class VirtualMachine extends Machine implements Instructions{
	
	private static final String TAG = VirtualMachine.class.getName();

    private int _numInstrsRun = 0;
    private VMListener vmListener;
    private Context _context;

    public VirtualMachine(final PrintStream writer, final InputStream reader, Context context)
    {
    	super(writer, reader);
    	init(context);
    }

    public VirtualMachine(Context context)
    {
    	super();
    	init(context);
    }

	/**
	 * @param context
	 */
	private void init(Context context) {
		try {
			Class.forName("BaseInstructionSet");
		} catch (ClassNotFoundException e) {
			debug(TAG, e.getMessage());
		}
    	_context = context;
	}

    public void setVMListener(VMListener listener)
    {
    	vmListener = listener;
    }


    //   EXECUTION

    public void addInstruction(final Command command) throws VMError
    {
        int instruction = command.getCommandId();
        List<Integer> instructionParams = command.getParameters();
    	if (instruction < 1000)
        {
            setValAtProgramWtr(instruction);
            setValAtProgramWtr(0);
            debug(TAG, "Add ins="+ BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(instruction)+ "(" + instruction + ")" + " params=X");
        }
        if (instruction >= 1000)
        {
            if ( instructionParams != null )
            {
                String sVal = "";
            	setValAtProgramWtr(instruction);
                for(int instPram : instructionParams)
                {
                    sVal += Integer.toString(instPram) + ":";
                	setValAtProgramWtr(instPram);
                }
                debug(TAG, "Add ins="+ BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(instruction)+ "(" + instruction + ")" + " params=" + sVal);
            }
            else
            {
            	throw new VMError("addInstruction NULL parameter list", VMError.VM_ERROR_BAD_PARAMS);
            }
        }
    }

    private void setValAtProgramWtr(final int value) throws VMError
    {
        setValAtLocation(value, getProgramWriter());
        incProgramWriter();
    }

    public void addInstructions(final CommandSet commandSet)
    {
    	new Thread(new Runnable()
    	{
			public void run()
			{
				_addInstructions(commandSet);
			}
    	}).start();	
    }

    // Mutable Line number ( to make it an "out" variable )
    public void runInstructions(final int numInstrsToRun, final LineNumber... line)
    {    	
    	new Thread(new Runnable()
    	{
			public void run()
			{
				_runInstructions(numInstrsToRun, line);
			}
    	}).start();	
    }

    public void runInstructions()
    {
    	new Thread(new Runnable()
    	{
			public void run()
			{
				_runInstructions();
			}
    	}).start();
    }

    // Mutable Line number ( to make it an "out" variable )
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

    /**
	 * 
	 */
	private void _runInstructions() {
		VMError vmError = null;
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
		    	debug(TAG, "-PROG_CTR=" + getProgramCounter() + " line=" + ((getProgramCounter() - STACK_LIMIT)/2) + " inst=" + instructionVal);
		    	incProgramCounter();
		        runCommand(instructionVal);
		    }
			debug(TAG, "PROG_CTR=" + getProgramCounter());
			debug(TAG, "LAST_PROG_CTR=" + last);
		    dumpMem("3");
		    resetProgramCounter();
		    resetStackPointer();
		}
		catch(VMError vme)
		{
			debug(TAG, "+PROG_CTR=" + getProgramCounter());
			debug(TAG, "+LAST_PROG_CTR=" + last);
			dumpMem("2");
			vmError = vme;
		}
		if ( vmListener != null)
		{
			vmListener.completedRunningInstructions(vmError);
		}
	}

	/**
	 * @param instructions
	 * @param instructionParams
	 */
	private void _addInstructions(final CommandSet commandSet) {
		VMError vmError = null;
		if (commandSet != null )
		{
			int numCommands = commandSet.getNumCommands();
			for(int i = 0 ; i < numCommands; i++)
	        {
	        	try {
	        		addInstruction(commandSet.getCommand(i));
				} catch (VMError e) {
					vmError = e;
				}
	        }
		}
		else
		{
			vmError = new VMError("addInstructions instructions", VMError.VM_ERROR_BAD_PARAMS);
		}
		if ( vmListener != null)
		{
			vmListener.completedAddingInstructions(vmError);
		}
	}
	
	/**
	 * @param numInstrsToRun
	 * @param line
	 * @throws VMError
	 */
	private void _runInstructions(final int numInstrsToRun,
			final LineNumber... line)
	{
		VMError vmError = null;
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
        	debug(TAG, "PROG_CTR=" + getProgramCounter());
        	debug(TAG, "LAST_PROG_CTR=" + last);
        	dumpMem("2");
        	vmError = vme;
        }
    	debug(TAG, "PROG_CTR=" + getProgramCounter());
    	debug(TAG, "LAST_PROG_CTR=" + last);
        dumpMem("3");
		if ( vmListener != null)
		{
			vmListener.completedRunningInstructions(vmError);
		}
	}

	/**
	 * 
	 */
	private void dumpMem(String sAppend) {
		if ( _bDebug )
		{
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
            debug(TAG, sLineCount.toString());
            if (command >= 1000)
            {
            	sParam.append(" PARAM=").append(getValueAt(getProgramCounter()));
            }
            debug(TAG, sParam.toString());
        }
        _numInstrsRun++;

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
                //incProgramCounter();
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
            	throw new VMError("BAD runCommand :" + command, VMError.VM_ERROR_BAD_UNKNOWN_COMMAND);
        }
    }
}

