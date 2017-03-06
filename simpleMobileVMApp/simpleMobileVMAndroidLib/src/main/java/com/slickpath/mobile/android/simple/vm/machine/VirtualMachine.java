package com.slickpath.mobile.android.simple.vm.machine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.IVMListener;
import com.slickpath.mobile.android.simple.vm.InputListener;
import com.slickpath.mobile.android.simple.vm.OutputListener;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.VMErrorType;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.instructions.Instructions;
import com.slickpath.mobile.android.simple.vm.util.Command;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Pete Procopio
 */
public class VirtualMachine extends Machine implements Instructions {

    private static final int SINGLE_PARAM_COMMAND_START = 1000;

    private static final String LOG_TAG = VirtualMachine.class.getName();
    private final Context context;
    private int numInstructionsRun = 0;
    private IVMListener vmListener;

    private static final ThreadPoolExecutor executorPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    /**
     * Constructor
     * - output will be added to log is debugVerbose is set
     * - input will be attempted to be retrieved from the console System.in
     *
     * @param context context object
     */
    public VirtualMachine(final @NonNull Context context) {
        super(null, null);
        init();
        this.context = context;
    }

    /**
     * Constructor
     * Allows caller to pass in streams for both input and output
     *
     * If outputListener is null output will be added to log is debugVerbose is set
     * If inputListener is null input will be attempted to be retrieved from the console System.in
     *
     * @param context context object
     * @param outputListener listener for output events
     * @param inputListener listener to return input on input events
     */
    public VirtualMachine(final @NonNull Context context, @Nullable OutputListener outputListener, @Nullable InputListener inputListener) {
        super(outputListener, inputListener);
        init();
        this.context = context;
    }

    /**
     * Initialize VM
     */
    private void init() {
        try {
            Class.forName("BaseInstructionSet");
        } catch (@NonNull final ClassNotFoundException e) {
            debug(LOG_TAG, e.getMessage());
        }
    }

    /**
     * get a listener to listen to events thrown by VM
     *
     * @return IVMListener
     */
    public IVMListener getVMListener() {
        return vmListener;
    }

    /**
     * Set a listener to listen to events thrown by VM
     *
     * @param listener listener on vm events
     */
    public void setVMListener(final IVMListener listener) {
        vmListener = listener;
    }

    //   EXECUTION

    /**
     * Add the contents of a Command object to the VM
     * Basically write the command id and its parameter(s) into the program memory space
     *
     * @param command command to add
     */
    public void addCommand(@NonNull final Command command) {
        final int instruction = command.getCommandId();
        String params = "X";

        if (instruction >= SINGLE_PARAM_COMMAND_START) {

            params = command.getParameters().get(0).toString();
        }
        setCommandAt(command, getProgramWriterPtr());
        debugVerbose(LOG_TAG, "Add ins=" + getInstructionString(instruction) + "(" + instruction + ")" + " params=" + params + " at " + getProgramWriterPtr());

    }

    /**
     * Launch thread that will add all the Commands in the CommandList to the VM
     * will call completedAddingInstructions on IVMListener after completion
     *
     * @param commands command container
     */
    public void addCommands(final CommandList commands) {
        resetProgramWriter();

        executorPool.execute(new Runnable() {
            @Override
            public void run() {
                doAddInstructions(commands);
            }
        });
    }

    /**
     * Add all the Commands in the CommandList to the VM
     * will call completedAddingInstructions on IVMListener after completion
     *
     * @param commands commands to add
     */
    private void doAddInstructions(@Nullable final CommandList commands) {
        VMError vmError = null;
        if (commands != null) {
            final int numCommands = commands.size();
            for (int i = 0; i < numCommands; i++) {
                addCommand(commands.get(i));
            }
        } else {
            vmError = new VMError("addInstructions instructions", VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        }
        if (vmListener != null) {
            vmListener.completedAddingInstructions(vmError);
        }
    }

    /**
     * Will run the instruction the program pointer is pointing at.
     * This is a synchronous call and will not call into the completedAddingInstructions
     *
     * @return instruction was a halt
     * @throws VMError on a VM error
     */
    public boolean runNextInstruction() throws VMError {
        Results results = doRunNextInstruction();

        if(results.vmError != null) {
            throw results.vmError;
        }
        return results.halt;
    }

    /**
     * Run the instruction the program pointer is pointing at
     * will call completedRunningInstruction on IVMListener after completion
     */
    private Results doRunNextInstruction() {
        Log.d(LOG_TAG, "+doRunNextInstruction " + getProgramCounter());
        boolean bHalt = false;
        VMError vmError = null;

        int instructionVal = -1;
        try {
            instructionVal = getInstruction();
            runCommand(instructionVal);
        } catch (@NonNull final VMError e) {
            vmError = e;
        }
        if (instructionVal == BaseInstructionSet._HALT) {
            numInstructionsRun = 0;
            resetProgramWriter();
            resetStack();
            bHalt = true;
        }

        return new Results(bHalt, vmError);
    }

    private static class Results {
        final boolean halt;
        final VMError vmError;

        public Results(boolean halt, VMError vmError) {
            this.halt = halt;
            this.vmError = vmError;
        }
    }

    /**
     * Launches thread that does - Run all remaining instructions - starting from current program ptr location
     * will call completedRunningInstructions on IVMListener after completion
     */
    public void runInstructions() {
        executorPool.execute(new Runnable() {
            @Override
            public void run() {
                doRunInstructions();
            }
        });
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
     * @param numInstrsToRun number of instructions to run until running stops
     */
    protected void runInstructions(final int numInstrsToRun) {
        executorPool.execute(new Runnable() {
            @Override
            public void run() {
                doRunInstructions(numInstrsToRun);
            }
        });
    }

    /**
     * Run N number of instructions - starting from current program ptr location
     * will call completedRunningInstructions on IVMListener after completion
     *
     * @param numInstrsToRun number of instructions to run until running stops
     */
    private void doRunInstructions(final int numInstrsToRun) {
        Log.d(LOG_TAG, "+START++++++++++++++++++");
        VMError vmError = null;
        dumpMem("1");
        int numInstrsRun = 0;

        int lastProgCtr = -1;
        int instructionVal = BaseInstructionSet._BEGIN;

        try {
            while ((instructionVal != BaseInstructionSet._HALT) && (getProgramCounter() < Memory.MAX_MEMORY) && ((numInstrsRun < numInstrsToRun) || (numInstrsToRun == -1))) {
                lastProgCtr = getProgramCounter();
                numInstrsRun++;
                instructionVal = getInstruction();
                runCommand(instructionVal);
            }
            debug(LOG_TAG, "=========================");
            logAdditionalInfo(numInstrsRun, lastProgCtr, instructionVal);
            debug(LOG_TAG, "=========================");
        } catch (@NonNull final VMError vme) {
            debug(LOG_TAG, "=========================");
            debug(LOG_TAG, "VMError=(" + vme.getType() + ") " + vme.getMessage());
            logAdditionalInfo(numInstrsRun, lastProgCtr, instructionVal);
            dumpMem("2");
            vmError = vme;
            debug(LOG_TAG, "=========================");
        }
        dumpMem("3");
        Log.d(LOG_TAG, "+DONE PROCESSING+++++++++");
        if (vmListener != null) {
            vmListener.completedRunningInstructions(instructionVal == BaseInstructionSet._HALT, getProgramCounter(), vmError);
        } else {
            debug(LOG_TAG, "NO VMListener");
        }
        Log.d(LOG_TAG, "+END+++++++++++++++++++++");
    }

    /**
     * @param numInstrsRun   number of instructions to run until running stops
     * @param lastProgCtr    last program counter location
     * @param instructionVal last instruction value
     */
    private void logAdditionalInfo(final int numInstrsRun, final int lastProgCtr,
                                   final int instructionVal) {
        debug(LOG_TAG, "LAST_INSTRUCTION=(" + getInstructionString(instructionVal) + ") " + instructionVal);
        debug(LOG_TAG, "NUM INSTRUCTIONS RUN=" + numInstrsRun);
        debug(LOG_TAG, "PROG_CTR=" + getProgramCounter());
        debug(LOG_TAG, "LAST_PROG_CTR=" + lastProgCtr);
    }

    /**
     * @param instructionVal instruction val
     * @return instruction string representation
     */
    private String getInstructionString(final int instructionVal) {
        return BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(instructionVal);
    }

    /**
     * Dump the memory into a file for debugging purposes
     * file name : "memDump<sAppend>.text
     */
    private void dumpMem(final String append) {
        if (getDebugDump()) {
            final String FILENAME = "memDump" + append + ".txt";
            final StringBuilder data = new StringBuilder("");

            for (int i = 1; i < Memory.MAX_MEMORY; i += 2) {
                try {
                    final int parm = getValueAt(i);
                    final int command = getValueAt(i - 1);
                    data.append(command).append(' ').append(parm).append("\r\n");
                } catch (@NonNull final VMError e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write(data.toString().getBytes());
            } catch (@NonNull final IOException e) {
                if(fos !=null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Run the selected commandId
     *
     * @param commandId id of command to run
     * @throws VMError VM error on running a command
     */
    private void runCommand(final int commandId) throws VMError {
        if (getDebugVebose()) {
            doRunCommandDebug(commandId);
        }
        numInstructionsRun++;

        boolean bBranched;

        switch (commandId) {
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
                if (!bBranched) {
                    incProgramCounter();
                }
                break;
            case _BRLSS:
                bBranched = BRLSS(getParameter());
                if (!bBranched) {
                    incProgramCounter();
                }
                break;
            case _BRGTR:
                bBranched = BRGTR(getParameter());
                if (!bBranched) {
                    incProgramCounter();
                }
                break;
            default:
                throw new VMError("BAD runCommand :" + commandId, VMErrorType.VM_ERROR_TYPE_BAD_UNKNOWN_COMMAND);
        }
    }

    private int getInstruction() throws VMError {
        final Command command = getCommandAt(getProgramCounter());
        return command.getCommandId();
    }

    private int getParameter() throws VMError {
        final Command command = getCommandAt(getProgramCounter());
        if (command.getParameters().size() == 0) {
            throw new VMError("No Parameters", VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        } else {
            try {
                return command.getParameters().get(0);
            } catch(Throwable t) {
                throw new VMError(t.getMessage(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
            }
        }
    }

    /**
     * Debug output of runCommand
     *
     * @param commandId id of command to run in debug
     * @throws VMError on vm error
     */
    private void doRunCommandDebug(final int commandId) throws VMError {
        final StringBuilder lineCount = new StringBuilder("[");
        lineCount.append(numInstructionsRun);
        lineCount.append(']');
        lineCount.append(" Line=");
        lineCount.append((getProgramCounter() - 1));
        lineCount.append(" CMD=");
        lineCount.append(getInstructionString(commandId));
        lineCount.append(" (");
        lineCount.append(commandId);
        lineCount.append(")");
        if (commandId >= 1000) {
            lineCount.append(" PARAM=");
            lineCount.append(getParameter());
        }
        debug(LOG_TAG, lineCount.toString());
    }
}

