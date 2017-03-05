package com.slickpath.mobile.android.simple.vm.machine;


import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.IVMListener;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.instructions.Instructions;
import com.slickpath.mobile.android.simple.vm.parser.IParserListener;
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser;
import com.slickpath.mobile.android.simple.vm.util.Command;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Pete Procopio
 */
public class VirtualMachineTest extends AndroidTestCase {

    private static final String TAG = VirtualMachineTest.class.getName();

    private static final int NUM_COMMANDS_TO_RUN = 10;

    private VMError _vmError;
    private boolean _bHalt;
    private int _lastLineExecuted;
    private CommandList _commands;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _bHalt = false;
        _lastLineExecuted = -1;
        _vmError = null;
        _commands = null;
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#getVMListener()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#setVMListener(com.slickpath.mobile.android.simple.vm.IVMListener)}.
     */
    public void testGetVMListener() {
        VirtualMachine virtualMachine = new VirtualMachine(getContext());
        assertNull(virtualMachine.getVMListener());
        virtualMachine.setVMListener(null);
        assertNull(virtualMachine.getVMListener());
        virtualMachine.setVMListener(new IVMListener() {
            @Override
            public void completedAddingInstructions(VMError vmError) {
                VirtualMachineTest.this.completedAddingInstructions(vmError);
            }

            @Override
            public void completedRunningInstructions(boolean bHalt, int lastLineExecuted, VMError vmError) {
                VirtualMachineTest.this.completedRunningInstructions(bHalt, lastLineExecuted, vmError);
            }
        });
        assertNotNull(virtualMachine.getVMListener());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#addCommand(com.slickpath.mobile.android.simple.vm.util.Command)}.
     */
    public void testAddCommand() {
        VirtualMachine virtualMachine = new VirtualMachine(getContext());
        final int[] instructions = {Instructions._ADD, Instructions._EQUAL, Instructions._NOT, Instructions._PUSHC, Instructions._JUMP, Instructions._POPC};
        final Integer[] params = {null, null, null, 10, 20, 30};

        try {
            for (int i = 0; i < instructions.length; i++) {
                final List<Integer> paramList = new ArrayList<>();
                final Integer param = params[i];
                paramList.add(param);
                final Command command = new Command(instructions[i], paramList);
                virtualMachine.addCommand(command);
            }

            for (int i = 0; i < instructions.length; i++) {
                final Command command = virtualMachine.getCommandAt(i);
                assertEquals(instructions[i], command.getCommandId().intValue());
                assertEquals(params[i], command.getParameters().get(0));
            }

        } catch (@NonNull final VMError e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#addCommands(com.slickpath.mobile.android.simple.vm.util.CommandList)}.
     */
    public void testAddCommands() {
        VirtualMachine virtualMachine = new VirtualMachine(getContext());
        final int[] instructions = {Instructions._ADD, Instructions._EQUAL, Instructions._NOT, Instructions._PUSHC, Instructions._JUMP, Instructions._POPC};
        final Integer[] params = {null, null, null, 10, 20, 30};

        try {
            final CommandList commandList = new CommandList();

            for (int i = 0; i < instructions.length; i++) {
                final List<Integer> paramList = new ArrayList<>();
                final Integer param = params[i];
                paramList.add(param);
                final Command command = new Command(instructions[i], paramList);
                commandList.add(command);
            }

            final CountDownLatch signalAddCommands =new CountDownLatch(1);
            addVMListenerAdding(virtualMachine, signalAddCommands);
            virtualMachine.addCommands(commandList);
            assertNull(_vmError);

            try {
                // Wait for Callback
                signalAddCommands.await();
            } catch (@NonNull final InterruptedException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }// wait for callback

            for (int i = 0; i < instructions.length; i++) {
                final Command command = virtualMachine.getCommandAt(i);
                assertEquals(instructions[i], command.getCommandId().intValue());
                assertEquals(params[i], command.getParameters().get(0));
            }

        } catch (@NonNull final VMError e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#runNextInstruction()}.
     */
    public void testRunNextInstruction() {
        VirtualMachine virtualMachine = new VirtualMachine(getContext());
        final CountDownLatch signalParser = new CountDownLatch(1);

        SimpleParser parser = new SimpleParser(new FileHelperForTest(FibonacciInstructions.instructions), new IParserListener() {
            @Override
            public void completedParse(VMError vmError, CommandList commands) {
                VirtualMachineTest.this.completedParse(vmError, commands);
                signalParser.countDown();// notify the count down latch
            }
        });

        Log.d(TAG, "+...........................PARSE START ");
        parser.parse();
        try {
            // Wait for Callback
            Log.d(TAG, "+...........................PARSE WAIT ");
            signalParser.await();
            Log.d(TAG, "+...........................PARSE DONE WAIT ");
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        Log.d(TAG, "+...........................VM ADD COMMANDS START");
        final CountDownLatch signalAddCommands = new CountDownLatch(1);
        addVMListenerAdding(virtualMachine, signalAddCommands);
        virtualMachine.addCommands(_commands);

        try {
            // Wait for Callback
            Log.d(TAG, "+...........................VM ADD COMMANDS WAIT ");
            signalAddCommands.await();
            Log.d(TAG, "+...........................VM ADD COMMANDS DONE WAIT ");
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback
        assertNull(_vmError);

        Log.d(TAG, "+...........................VM RUN INSTRS START");
        final CountDownLatch signalRunInstructions = new CountDownLatch(1);
        addVMListenerRunning(virtualMachine, signalRunInstructions);
        virtualMachine.runInstructions(NUM_COMMANDS_TO_RUN);

        try {
            // Wait for Callback
            Log.d(TAG, "+...........................VM RUN INSTRS WAIT ");
            signalRunInstructions.await();
            Log.d(TAG, "+...........................VM RUN INSTRS DONE WAIT ");
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        assertFalse(_bHalt);
        assertEquals(NUM_COMMANDS_TO_RUN, _lastLineExecuted);
        assertNull(_vmError);

        for (int i = 0; i < 100; i++) {
            try {
                boolean halt = virtualMachine.runNextInstruction();
                assertFalse("(" + i + ") Halt= ", halt);
            } catch (VMError vmError) {
                assertNull("(" + i + ") LINE=" + _lastLineExecuted + " --> " + vmError.getMessage(), _vmError);
            }
        }
    }

    private void addVMListenerAdding(final VirtualMachine virtualMachine, final CountDownLatch signal) {
        virtualMachine.setVMListener(new IVMListener() {

            @Override
            public void completedAddingInstructions(VMError vmError) {
                VirtualMachineTest.this.completedAddingInstructions(vmError);
                signal.countDown();// notify the count down latch
            }

            @Override
            public void completedRunningInstructions(boolean bHalt, int lastLineExecuted, VMError vmError) {
                // Do Nothing
            }
        });
    }

    private void addVMListenerRunning(final VirtualMachine virtualMachine, final CountDownLatch signal) {
        virtualMachine.setVMListener(new IVMListener() {

            @Override
            public void completedAddingInstructions(VMError vmError) {
                // Do Nothing
            }

            @Override
            public void completedRunningInstructions(boolean bHalt, int lastLineExecuted, VMError vmError) {
                VirtualMachineTest.this.completedRunningInstructions(bHalt, lastLineExecuted, vmError);
                signal.countDown();// notify the count down latch
            }
        });
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#runInstructions()}.
     */
    public void testRunInstructions() {
        VirtualMachine virtualMachine = new VirtualMachine(getContext());
        final CountDownLatch signalParse = new CountDownLatch(1);
        SimpleParser parser = new SimpleParser(new FileHelperForTest(FibonacciInstructions.instructions), new IParserListener() {
            @Override
            public void completedParse(VMError vmError, CommandList commands) {
                VirtualMachineTest.this.completedParse(vmError, commands);
                signalParse.countDown();
            }
        });

        parser.parse();
        try {
            // Wait for Callback
            signalParse.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        final CountDownLatch signalAddCommands =new CountDownLatch(1);
        addVMListenerAdding(virtualMachine, signalAddCommands);
        virtualMachine.addCommands(_commands);
        assertNull(_vmError);
        try {
            // Wait for Callback
            signalAddCommands.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        final CountDownLatch signalRunInstructions = new CountDownLatch(1);
        addVMListenerRunning(virtualMachine, signalRunInstructions);
        virtualMachine.runInstructions();
        try {
            // Wait for Callback
            signalRunInstructions.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        assertTrue(_bHalt);
        assertEquals(35, _lastLineExecuted);
        assertNull(_vmError);
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#runInstructions(int)}.
     */
    public void testRunInstructionsInt() {
        Log.d(TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        VirtualMachine virtualMachine = new VirtualMachine(getContext());
        final CountDownLatch signalParse = new CountDownLatch(1);
        SimpleParser parser = new SimpleParser(new FileHelperForTest(FibonacciInstructions.instructions), new IParserListener() {
            @Override
            public void completedParse(VMError vmError, CommandList commands) {
                VirtualMachineTest.this.completedParse(vmError, commands);
                signalParse.countDown();
            }
        });

        parser.parse();
        try {
            // Wait for Callback
            signalParse.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        final CountDownLatch signalAddCommands =new CountDownLatch(1);
        addVMListenerAdding(virtualMachine, signalAddCommands);
        virtualMachine.addCommands(_commands);
        assertNull(_vmError);
        try {
            // Wait for Callback
            signalAddCommands.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        final CountDownLatch signalRunInstructions = new CountDownLatch(1);
        addVMListenerRunning(virtualMachine, signalRunInstructions);
        virtualMachine.runInstructions(10);

        try {
            // Wait for Callback
            signalRunInstructions.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        assertFalse(_bHalt);
        Log.d(TAG, "+......... checking  last line executed");
        assertEquals(10, _lastLineExecuted);
        assertNull(_vmError);
        Log.d(TAG, "??????????????????????????????");
    }

    private void completedAddingInstructions(final VMError vmError) {
        Log.d(TAG, "+..........completedAddingInstructions ");
        _vmError = vmError;
        Log.d(TAG, "+..........completedAddingInstructions CountDown");
    }

    private void completedRunningInstructions(final boolean bHalt,
                                             final int lastLineExecuted, final VMError vmError) {
        Log.d(TAG, "+..........CompletedRunningInstructions " + lastLineExecuted + " halt = " + bHalt + "vmError = " + vmError);
        _vmError = vmError;
        _bHalt = bHalt;
        _lastLineExecuted = lastLineExecuted;
        Log.d(TAG, "+..........CompletedRunningInstructions CountDown");
    }

    private void completedParse(final VMError vmError, final CommandList commands) {
        // Save values on callback and release test thread
        Log.d(TAG, "+..........completedParse ");
        _vmError = vmError;
        _commands = commands;
        Log.d(TAG, "+..........completedParse CountDown");
    }
}
