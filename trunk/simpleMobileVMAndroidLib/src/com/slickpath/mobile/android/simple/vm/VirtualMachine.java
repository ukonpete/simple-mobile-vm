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

import android.content.Context;

import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.instructions.Instructions;
import com.slickpath.mobile.android.simple.vm.util.Command;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

// import android.util.Log;

/**
 * @author PJ
 *
 */
public class VirtualMachine extends Machine implements Instructions{

	private static final int SINGLE_PARAM_COMMAND_START = 1000;

	private static final String TAG = VirtualMachine.class.getName();

	private int _numInstrsRun = 0;
	private VMListener _vmListener;
	private final Context _context;

	public VirtualMachine(final PrintStream writer, final InputStream reader, final Context context)
	{
		super(writer, reader);
		init();
		_context = context;
	}

	public VirtualMachine(final Context context)
	{
		super();
		init();
		_context = context;
	}

	/**
	 * Initialize VM Stuff
	 * 
	 * @param context
	 */
	private void init() {
		try
		{
			Class.forName("BaseInstructionSet");
		}
		catch (final ClassNotFoundException e)
		{
			debug(TAG, e.getMessage());
		}
	}

	/**
	 * Set a listener to listen to events thrown by VM
	 * @param listener
	 */
	public void setVMListener(final VMListener listener)
	{
		_vmListener = listener;
	}

	//   EXECUTION

	/**
	 * Add the contents of a Command object to the VM
	 * Basically write the command id and its parameter(s) into the program memory space
	 * 
	 * @param command
	 * @throws VMError
	 */
	public void addCommand(final Command command) throws VMError
	{
		final int instruction = command.getCommandId();
		final List<Integer> instructionParams = command.getParameters();
		if (instruction < 1000)
		{
			setValAtProgramWtr(instruction);
			setValAtProgramWtr(0);
			debug(TAG, "Add ins="+ BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(instruction)+ "(" + instruction + ")" + " params=X");
		}
		if (instruction >= SINGLE_PARAM_COMMAND_START)
		{
			if ( instructionParams != null )
			{
				final StringBuffer sVal = new StringBuffer("");
				setValAtProgramWtr(instruction);
				for(final int instPram : instructionParams)
				{
					sVal.append(Integer.toString(instPram)).append(':');
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

	/**
	 * Write a value at the current location of the programWriter pointer
	 * @param value
	 * @throws VMError
	 */
	private void setValAtProgramWtr(final int value) throws VMError
	{
		setValAtLocation(value, getProgramWriterPtr());
		incProgramWriter();
	}

	/**
	 * Launch thread that will add all the Commands in the CommandList to the VM
	 * will call completedAddingInstructions on VMListener after completion
	 * 
	 * @param commandList
	 */
	public void addCommands(final CommandList commands)
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				doAddInstructions(commands);
			}
		}).start();
	}

	/**
	 * Add all the Commands in the CommandList to the VM
	 * will call completedAddingInstructions on VMListener after completion
	 * 
	 * @param commands
	 */
	private void doAddInstructions(final CommandList commands) {
		VMError vmError = null;
		if (commands != null )
		{
			final int numCommands = commands.size();
			for(int i = 0 ; i < numCommands; i++)
			{
				try {
					addCommand(commands.get(i));
				} catch (final VMError e) {
					vmError = e;
				}
			}
		}
		else
		{
			vmError = new VMError("addInstructions instructions", VMError.VM_ERROR_BAD_PARAMS);
		}
		if ( _vmListener != null)
		{
			_vmListener.completedAddingInstructions(vmError);
		}
	}

	/**
	 * Launches thread that will run the instruction the program pointer is pointing at
	 * will call completedRunningInstruction on VMListener after completion
	 */
	public void runNextInstruction()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				doRunNextInstruction();
			}
		}).start();
	}

	/**
	 * Run the instruction the program pointer is pointing at
	 * will call completedRunningInstruction on VMListener after completion
	 */
	private void doRunNextInstruction()
	{
		boolean bHalt = false;
		VMError vmError = null;
		resetProgramWriter();

		final int line = getLineNumber();
		int instructionVal = -1;
		try {
			instructionVal = getValueAt(getProgramCounter());
			runCommand(instructionVal);
		} catch (final VMError e) {
			vmError = e;
		}
		if (instructionVal == BaseInstructionSet._HALT)
		{
			_numInstrsRun = 0;
			resetProgramWriter();
			resetStackPointer();
			bHalt = true;
		}
		if ( _vmListener != null)
		{
			_vmListener.completedRunningInstruction(bHalt, line , vmError);
		}
	}

	/**
	 * Launches thread that does - Run all remaining instructions - starting from current program ptr location
	 * will call completedRunningInstructions on VMListener after completion
	 * 
	 */
	public void runInstructions()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				doRunInstructions();
			}
		}).start();
	}

	/**
	 * Run all remaining instructions - starting from current program ptr location
	 * will call completedRunningInstructions on VMListener after completion
	 */
	private void doRunInstructions() {
		VMError vmError = null;
		dumpMem("1");
		_numInstrsRun = 0;
		resetProgramWriter();

		int instructionVal = BaseInstructionSet._BEGIN;
		int lastLine = -1;
		int lastProgCtr = -1;

		try
		{
			while ((instructionVal != BaseInstructionSet._HALT) && (getProgramCounter() < MAX_MEMORY))
			{
				lastProgCtr = getProgramCounter();
				instructionVal = getValueAt(getProgramCounter());
				debug(TAG, "-PROG_CTR=" + getProgramCounter() + " line=" + ((getProgramCounter() - STACK_LIMIT)/2) + " inst=" + instructionVal);
				runCommand(instructionVal);
			}
			debug(TAG, "PROG_CTR=" + getProgramCounter());
			debug(TAG, "LAST_PROG_CTR=" + lastProgCtr);
			dumpMem("3");
			lastLine = getLineNumber();
			resetProgramCounter();
			resetStackPointer();
		}
		catch(final VMError vme)
		{
			debug(TAG, "+PROG_CTR=" + getProgramCounter());
			debug(TAG, "+LAST_PROG_CTR=" + lastProgCtr);
			dumpMem("2");
			vmError = vme;
		}
		if ( _vmListener != null)
		{
			_vmListener.completedRunningInstructions(lastLine , vmError);
		}
	}

	/**
	 * Launch thread that will Run N number of instructions - starting from current program ptr location
	 * will call completedRunningInstructions on VMListener after completion
	 * 
	 * @param numInstrsToRun
	 */
	public void runInstructions(final int numInstrsToRun)
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				doRunInstructions(numInstrsToRun);
			}
		}).start();
	}

	/**
	 * Run N number of instructions - starting from current program ptr location
	 * will call completedRunningInstructions on VMListener after completion
	 * 
	 * @param numInstrsToRun
	 */
	private void doRunInstructions(final int numInstrsToRun)
	{
		VMError vmError = null;
		dumpMem("1");
		int numInstrsRun = 0;
		resetProgramWriter();

		int lastProgCtr = -1;
		int instructionVal = BaseInstructionSet._BEGIN;

		try
		{
			while ((instructionVal != BaseInstructionSet._HALT) && (getProgramCounter() < MAX_MEMORY) && (numInstrsRun < numInstrsToRun))
			{
				lastProgCtr = getProgramCounter();
				numInstrsRun++;
				instructionVal = getValueAt(getProgramCounter());
				runCommand(instructionVal);
			}
			debug(TAG, "PROG_CTR=" + getProgramCounter());
			debug(TAG, "LAST_PROG_CTR=" + lastProgCtr);
		}
		catch(final VMError vme)
		{
			debug(TAG, "PROG_CTR=" + getProgramCounter());
			debug(TAG, "LAST_PROG_CTR=" + lastProgCtr);
			dumpMem("2");
			vmError = vme;
		}
		dumpMem("3");
		if ( _vmListener != null)
		{
			_vmListener.completedRunningInstructions(getLineNumber(), vmError);
		}
	}

	/**
	 * Converts current program pointer to an actual line number
	 * 
	 * @return Current line number of the program
	 * 
	 */
	private int getLineNumber() {
		return (getProgramCounter() - 2 - STACK_LIMIT)/2;
	}

	/**
	 * Dump the memory into a file for debugging purposes
	 * file name : "memDump<sAppend>.text
	 */
	private void dumpMem(final String sAppend) {
		if ( _bDebug )
		{
			final String FILENAME = "memDump" + sAppend + ".txt";
			final StringBuffer sData = new StringBuffer("");

			for(int i = 1; i < MAX_MEMORY; i+=2)
			{
				try {
					final int parm = getValueAt(i);
					final int command = getValueAt(i-1);
					sData.append(command).append(' ').append(parm).append("\r\n");
				}
				catch (final VMError e) {
					e.printStackTrace();
				}
			}
			FileOutputStream fos;
			try {
				fos = _context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(sData.toString().getBytes());
				fos.close();
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Run the selected commandId
	 * 
	 * @param commandId
	 * @throws VMError
	 */
	private void runCommand(final int commandId) throws VMError
	{
		incProgramCounter();
		if (_bDebug)
		{
			doRunCommandDebug(commandId);
		}
		_numInstrsRun++;

		boolean bBranched = false;

		switch (commandId)
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
			throw new VMError("BAD runCommand :" + commandId, VMError.VM_ERROR_BAD_UNKNOWN_COMMAND);
		}
	}

	/**
	 * Debug output of runCommand
	 * 
	 * @param commandId
	 * @throws VMError
	 */
	private void doRunCommandDebug(final int commandId) throws VMError {
		final StringBuffer sLineCount = new StringBuffer("[");
		sLineCount.append(_numInstrsRun).append(']');
		final StringBuffer sParam = new StringBuffer(" Line=");
		sParam.append(getProgramCounter() - 1);
		sLineCount.append("CMD=").append(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(commandId));
		debug(TAG, sLineCount.toString());
		if (commandId >= 1000)
		{
			sParam.append(" PARAM=").append(getValueAt(getProgramCounter()));
		}
		debug(TAG, sParam.toString());
	}
}

