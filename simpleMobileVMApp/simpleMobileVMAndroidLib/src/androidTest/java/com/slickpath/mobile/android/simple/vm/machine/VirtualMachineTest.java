package com.slickpath.mobile.android.simple.vm.machine;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
public class VirtualMachineTest extends AndroidTestCase implements IVMListener, IParserListener {

    private static final String TAG = VirtualMachineTest.class.getName();

    private static final int NUM_COMMANDS_TO_RUN = 10;

    @Nullable
    private VirtualMachine _vm = null;

    private SimpleParser _parser;
    private CountDownLatch _signal;
    private VMError _vmError;
    private boolean _bHalt;
    private int _lastLineExecuted;
    private CommandList _commands;
    private int _count;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _vm = new VirtualMachine(getContext());
        _vm.setVMListener(this);
        _bHalt = false;
        _lastLineExecuted = -1;
        _count = 0;
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#getVMListener()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#setVMListener(com.slickpath.mobile.android.simple.vm.IVMListener)}.
     */
    public void testGetVMListener() {
        assertNotNull(_vm.getVMListener());
        _vm.setVMListener(null);
        assertNull(_vm.getVMListener());
        _vm.setVMListener(this);
        assertNotNull(_vm.getVMListener());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#addCommand(com.slickpath.mobile.android.simple.vm.util.Command)}.
     */
    public void testAddCommand() {
        final int[] instructions = {Instructions._ADD, Instructions._EQUAL, Instructions._NOT, Instructions._PUSHC, Instructions._JUMP, Instructions._POPC};
        final Integer[] params = {null, null, null, 10, 20, 30};

        try {
            for (int i = 0; i < instructions.length; i++) {
                final List<Integer> paramList = new ArrayList<>();
                final Integer param = params[i];
                paramList.add(param);
                final Command command = new Command(instructions[i], paramList);
                _vm.addCommand(command);
            }

            for (int i = 0; i < instructions.length; i++) {
                final Command command = _vm.getCommandAt(i);
                assertEquals(instructions[i], command.getCommandId().intValue());
                assertEquals(params[i], command.getParameters().get(0));
            }

        } catch (@NonNull final VMError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#addCommands(com.slickpath.mobile.android.simple.vm.util.CommandList)}.
     */
    public void testAddCommands() {
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
            _signal = new CountDownLatch(1);
            _vm.addCommands(commandList);
            assertNull(_vmError);

            try {
                // Wait for Callback
                _signal.await();
            } catch (@NonNull final InterruptedException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }// wait for callback

            for (int i = 0; i < instructions.length; i++) {
                final Command command = _vm.getCommandAt(i);
                assertEquals(instructions[i], command.getCommandId().intValue());
                assertEquals(params[i], command.getParameters().get(0));
            }

        } catch (@NonNull final VMError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#runNextInstruction()}.
     */
    public void testRunNextInstruction() {
        _parser = new SimpleParser(new FileHelperForTest(FibonacciInstructions.instructions), this);

        _signal = new CountDownLatch(1);
        Log.d(TAG, "+...........................PARSE START ");
        _parser.parse();
        try {
            // Wait for Callback
            Log.d(TAG, "+...........................PARSE WAIT ");
            _signal.await();
            Log.d(TAG, "+...........................PARSE DONE WAIT ");
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        _signal = new CountDownLatch(1);
        Log.d(TAG, "+...........................VM ADD COMMANDS START");
        _vm.addCommands(_commands);

        try {
            // Wait for Callback
            Log.d(TAG, "+...........................VM ADD COMMANDS WAIT ");
            _signal.await();
            Log.d(TAG, "+...........................VM ADD COMMANDS DONE WAIT ");
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback
        assertNull(_vmError);

        _signal = new CountDownLatch(1);
        Log.d(TAG, "+...........................VM RUN INSTRS START");
        _vm.runInstructions(NUM_COMMANDS_TO_RUN);

        try {
            // Wait for Callback
            Log.d(TAG, "+...........................VM RUN INSTRS WAIT ");
            _signal.await();
            Log.d(TAG, "+...........................VM RUN INSTRS DONE WAIT ");
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        assertFalse(_bHalt);
        assertEquals(NUM_COMMANDS_TO_RUN, _lastLineExecuted);
        assertNull(_vmError);

        for (int i = 0; i < 100; i++) {
            _signal = new CountDownLatch(1);
            _vm.runNextInstruction();
            try {
                // Wait for Callback
                _signal.await();
            } catch (@NonNull final InterruptedException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }// wait for callback
            assertFalse("(" + _count + ")LINE=" + _lastLineExecuted, _bHalt);
            assertNull("(" + _count + ")LINE=" + _lastLineExecuted, _vmError);
        }
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#runInstructions()}.
     */
    public void testRunInstructions() {
        _parser = new SimpleParser(new FileHelperForTest(FibonacciInstructions.instructions), this);
        _signal = new CountDownLatch(1);

        _parser.parse();
        try {
            // Wait for Callback
            _signal.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        _vm.addCommands(_commands);
        assertNull(_vmError);

        _signal = new CountDownLatch(1);

        _vm.runInstructions();
        try {
            // Wait for Callback
            _signal.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        assertFalse(_bHalt);
        assertEquals(-1, _lastLineExecuted);
        assertNull(_vmError);
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#runInstructions(int)}.
     */
    public void testRunInstructionsInt() {
        _parser = new SimpleParser(new FileHelperForTest(FibonacciInstructions.instructions), this);
        _signal = new CountDownLatch(1);

        _parser.parse();
        try {
            // Wait for Callback
            _signal.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        _vm.addCommands(_commands);
        assertNull(_vmError);

        _signal = new CountDownLatch(1);
        _vm.runInstructions(10);

        try {
            // Wait for Callback
            _signal.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback

        assertFalse(_bHalt);
        assertEquals(10, _lastLineExecuted);
        assertNull(_vmError);
    }

    @Override
    public void completedAddingInstructions(final VMError vmError) {
        // TODO Auto-generated method stub
        Log.d(TAG, "+...........................completedAddingInstructions ");
        _vmError = vmError;
        _signal.countDown();// notify the count down latch
        Log.d(TAG, "+...........................completedAddingInstructions CountDown");
    }

    @Override
    public void completedRunningInstructions(final boolean bHalt,
                                             final int lastLineExecuted, final VMError vmError) {
        // TODO Auto-generated method stub
        Log.d(TAG, "+...........................CompletedRunningInstructions " + lastLineExecuted);
        _vmError = vmError;
        _bHalt = bHalt;
        _lastLineExecuted = lastLineExecuted;
        _count++;
        _signal.countDown();// notify the count down latch
        Log.d(TAG, "+...........................CompletedRunningInstructions CountDown");
    }

    @Override
    public void completedParse(final VMError vmError, final CommandList commands) {
        // Save values on callback and release test thread
        Log.d(TAG, "+...........................completedParse ");
        _vmError = vmError;
        _commands = commands;
        _signal.countDown();// notify the count down latch
        Log.d(TAG, "+...........................completedParse CountDown");
    }
}
