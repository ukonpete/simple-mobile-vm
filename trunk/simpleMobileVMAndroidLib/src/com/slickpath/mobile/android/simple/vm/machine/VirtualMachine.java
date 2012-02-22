/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.machine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import android.content.Context;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.IVMListener;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.instructions.Instructions;
import com.slickpath.mobile.android.simple.vm.util.Command;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

// import android.util.Log;

/**
 * @author Pete Procopio
 *
 */
public class VirtualMachine extends Machine implements Instructions{

	private static final int SINGLE_PARAM_COMMAND_START = 1000;

	private static final String TAG = VirtualMachine.class.getName();

	private int _numInstrsRun = 0;
	private IVMListener _vmListener;
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
	 * Initialize VM
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
	public void setVMListener(final IVMListener listener)
	{
		_vmListener = listener;
	}

	/**
	 * Set a listener to listen to events thrown by VM
	 * @param listener
	 */
	public IVMListener getVMListener()
	{
		return _vmListener;
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
		if (instruction < 1000)
		{
			setCommandAt(command, getProgramWriterPtr());
			debugVerbose(TAG, "Add ins="+ getInstructionString(instruction)+ "(" + instruction + ")" + " params=X at " + getProgramWriterPtr());
		}
		if (instruction >= SINGLE_PARAM_COMMAND_START)
		{
			setCommandAt(command, getProgramWriterPtr());
			debugVerbose(TAG, "Add ins="+ getInstructionString(instruction)+ "(" + instruction + ")" + " params=" + command.getParameters().get(0) + " at " + getProgramWriterPtr());
		}
	}

	/**
	 * Launch thread that will add all the Commands in the CommandList to the VM
	 * will call completedAddingInstructions on IVMListener after completion
	 * 
	 * @param commandList
	 */
	public void addCommands(final CommandList commands)
	{
		resetProgramWriter();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				doAddInstructions(commands);
			}
		}).start();
	}

	/**
	 * Add all the Commands in the CommandList to the VM
	 * will call completedAddingInstructions on IVMListener after completion
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
	 * will call completedRunningInstruction on IVMListener after completion
	 */
	public void runNextInstruction()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				doRunNextInstruction();
			}
		}).start();
	}

	/**
	 * Run the instruction the program pointer is pointing at
	 * will call completedRunningInstruction on IVMListener after completion
	 */
	private void doRunNextInstruction()
	{
		Log.d(TAG, "+doRunNextInstruction " + getProgramCounter());
		boolean bHalt = false;
		VMError vmError = null;

		int instructionVal = -1;
		try {
			instructionVal = getInstruction();
			runCommand(instructionVal);
		} catch (final VMError e) {
			vmError = e;
		}
		if (instructionVal == BaseInstructionSet._HALT)
		{
			_numInstrsRun = 0;
			resetProgramWriter();
			resetStack();
			bHalt = true;
		}
		if ( _vmListener != null)
		{
			_vmListener.completedRunningInstructions(bHalt, getProgramCounter() , vmError);
		}
	}

	/**
	 * Launches thread that does - Run all remaining instructions - starting from current program ptr location
	 * will call completedRunningInstructions on IVMListener after completion
	 * 
	 */
	public void runInstructions()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				doRunInstructions();
			}
		}).start();
	}

	/**
	 * Run all remaining instructions - starting from current program ptr location
	 * will call completedRunningInstructions on IVMListener after completion
	 */
	private void doRunInstructions() {
		doRunInstructions(-1);
	}

	/**
	 * Launch thread that will Run N number of instructions - starting from current program ptr location
	 * will call completedRunningInstructions on IVMListener after completion
	 * 
	 * @param numInstrsToRun
	 */
	public void runInstructions(final int numInstrsToRun)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				doRunInstructions(numInstrsToRun);
			}
		}).start();
	}

	/**
	 * Run N number of instructions - starting from current program ptr location
	 * will call completedRunningInstructions on IVMListener after completion
	 * 
	 * @param numInstrsToRun
	 */
	private void doRunInstructions(final int numInstrsToRun)
	{
		Log.d(TAG, "+START++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		VMError vmError = null;
		dumpMem("1");
		int numInstrsRun = 0;

		int lastProgCtr = -1;
		int instructionVal = BaseInstructionSet._BEGIN;

		try
		{
			while ((instructionVal != BaseInstructionSet._HALT) && (getProgramCounter() < Memory.MAX_MEMORY) && ((numInstrsRun < numInstrsToRun) || (numInstrsToRun == -1)))
			{
				lastProgCtr = getProgramCounter();
				numInstrsRun++;
				instructionVal = getInstruction();
				runCommand(instructionVal);
			}
			debug(TAG, "=============================================================");
			debug(TAG, "LAST_INSTRUCTION=(" + getInstructionString(instructionVal) + ") " + instructionVal);
			debug(TAG, "NUM INTRUCTIONS RUN=" + numInstrsRun);
			debug(TAG, "PROG_CTR=" + getProgramCounter());
			debug(TAG, "LAST_PROG_CTR=" + lastProgCtr);
			debug(TAG, "=============================================================");
		}
		catch(final VMError vme)
		{
			debug(TAG, "=============================================================");
			debug(TAG, "VMError=(" + vme.getType() + ") " + vme.getMessage());
			debug(TAG, "LAST_INSTRUCTION=(" + getInstructionString(instructionVal) + ") " + instructionVal);
			debug(TAG, "NUM INTRUCTIONS RUN=" + numInstrsRun);
			debug(TAG, "PROG_CTR=" + getProgramCounter());
			debug(TAG, "LAST_PROG_CTR=" + lastProgCtr);
			dumpMem("2");
			vmError = vme;
			debug(TAG, "=============================================================");
		}
		dumpMem("3");
		Log.d(TAG, "+END++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if ( _vmListener != null)
		{
			_vmListener.completedRunningInstructions(instructionVal == BaseInstructionSet._HALT, getProgramCounter(), vmError);
		}
	}

	/**
	 * @param instructionVal
	 * @return
	 */
	private String getInstructionString(final int instructionVal) {
		return BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(instructionVal);
	}

	/**
	 * Converts current program pointer to an actual line number
	 * 
	 * @return Current line number of the program
	 * 
	 */
	private int getLineNumber() {
		return getProgramCounter() + 1;
	}

	/**
	 * Dump the memory into a file for debugging purposes
	 * file name : "memDump<sAppend>.text
	 */
	private void dumpMem(final String sAppend) {
		if ( _bDebugDump )
		{
			final String FILENAME = "memDump" + sAppend + ".txt";
			final StringBuffer sData = new StringBuffer("");

			for(int i = 1; i < Memory.MAX_MEMORY; i+=2)
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
		if (_bDebugVerbose)
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
			PUSHC(getParameter());
			incProgramCounter();
			break;
		case _PUSH:
			PUSH(getParameter());
			incProgramCounter();
			break;
		case _POPC:
			POPC(getParameter());
			incProgramCounter();
			break;
		case _BRANCH:
			BRANCH(getParameter());
			//incProgramCounter();
			break;
		case _BREQL:
			bBranched = BREQL(getParameter());
			if ( !bBranched )
			{
				incProgramCounter();
			}
			break;
		case _BRLSS:
			bBranched = BRLSS(getParameter());
			if ( !bBranched )
			{
				incProgramCounter();
			}
			break;
		case _BRGTR:
			bBranched = BRGTR(getParameter());
			if ( !bBranched )
			{
				incProgramCounter();
			}
			break;
		default:
			throw new VMError("BAD runCommand :" + commandId, VMError.VM_ERROR_BAD_UNKNOWN_COMMAND);
		}
	}

	private int getInstruction() throws VMError {
		final Command command = getCommandAt(getProgramCounter());
		return command.getCommandId();
	}

	private int getParameter() throws VMError {
		final Command command = getCommandAt(getProgramCounter());
		return command.getParameters().get(0);
	}

	/**
	 * Debug output of runCommand
	 * 
	 * @param commandId
	 * @throws VMError
	 */
	private void doRunCommandDebug(final int commandId) throws VMError {
		final StringBuffer sLineCount = new StringBuffer("[");
		sLineCount.append(_numInstrsRun);
		sLineCount.append(']');
		sLineCount.append(" Line=");
		sLineCount.append((getProgramCounter() - 1));
		sLineCount.append(" CMD=");
		sLineCount.append(getInstructionString(commandId));
		sLineCount.append(" (");
		sLineCount.append(commandId);
		sLineCount.append(")");
		if (commandId >= 1000)
		{
			sLineCount.append(" PARAM=");
			sLineCount.append(getParameter());
		}
		debug(TAG, sLineCount.toString());
	}
}

