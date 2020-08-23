package com.slickpath.mobile.android.simple.vm.parser;


import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.machine.FibonacciInstructions;
import com.slickpath.mobile.android.simple.vm.machine.FileHelperForTest;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4.class)
public class SimpleParserTest implements IParserListener {

    private static final int FIB_LINE_NUMBER = 15;
    private static final int HALT_LINE_NUMBER = 34;
    private SimpleParser _parser;
    private CountDownLatch _signal;
    private VMError _error;
    private CommandList _commands;

    @Before
    public void before() {
        _parser = new SimpleParser(new FileHelperForTest(FibonacciInstructions.instructions), this);
        _signal = new CountDownLatch(1);
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.parser.SimpleParser#parse()}.
     */
    @Test
    public void testParse() {
        _parser.parse();
        try {
            // Wait for Callback
            _signal.await();
        } catch (@NonNull final InterruptedException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }// wait for callback
        assertNull(_error);
        assertNotNull(_commands);
        assertEquals(35, _commands.size());
        int i = 0;
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSHC); // 1
        assertNotNull(_commands.get(i).getParameters()); // 1
        assertEquals(1, _commands.get(i).getParameters().size()); // 1
        assertEquals(HALT_LINE_NUMBER, _commands.get(i++).getParameters().get(0).intValue()); // 1

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSHC); // 2
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(0, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i++).getCommandId(), BaseInstructionSet.WRINT); // 3
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSHC); // 4
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(1, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i++).getCommandId(), BaseInstructionSet.WRINT); // 5
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSHC); // 6
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(1, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i++).getCommandId(), BaseInstructionSet.WRINT); // 7
        // 8 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSHC); // 9
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(0, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.POPC); // 10
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(10, _commands.get(i++).getParameters().get(0).intValue());
        // 11 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSHC); // 12
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(1, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.POPC); // 13
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(11, _commands.get(i++).getParameters().get(0).intValue());
        // 14 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSH); // 15
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(11, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.POPC); // 16
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());
        // 17 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSHC); // 18
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.POPC); // 19
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(13, _commands.get(i++).getParameters().get(0).intValue());
        // 20 [FIB] - Symbol
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSH); // 21
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(11, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.POPC); // 22
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(14, _commands.get(i++).getParameters().get(0).intValue());
        // 23 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSH); // 24
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(14, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.POPC); // 25
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(10, _commands.get(i++).getParameters().get(0).intValue());
        // 26 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSH); // 27
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.POPC); // 28
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(11, _commands.get(i++).getParameters().get(0).intValue());
        // 29 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSH); // 30
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSH); // 31
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(10, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i++).getCommandId(), BaseInstructionSet.ADD); // 32

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.POPC); // 33
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());
        // 34 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSH); // 35
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i++).getCommandId(), BaseInstructionSet.WRINT); // 36
        // 37 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSHC); // 38
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(1, _commands.get(i++).getParameters().get(0).intValue());
        //
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSH); // 39
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(13, _commands.get(i++).getParameters().get(0).intValue());
        //
        assertEquals(_commands.get(i++).getCommandId(), BaseInstructionSet.SUB); // 40

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.POPC); // 41
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(13, _commands.get(i++).getParameters().get(0).intValue());
        // 42 - Comment
        // 43 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.PUSH); // 44
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(13, _commands.get(i++).getParameters().get(0).intValue());

        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.BREQL); // 45
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(HALT_LINE_NUMBER, _commands.get(i++).getParameters().get(0).intValue());
        //
        // 46 - Comment
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.BRANCH); // 47
        assertNotNull(_commands.get(i).getParameters());
        assertEquals(1, _commands.get(i).getParameters().size());
        assertEquals(FIB_LINE_NUMBER, _commands.get(i++).getParameters().get(0).intValue());
        // 48 - Comment
        // 49 [HALT] - Symbol
        assertEquals(_commands.get(i).getCommandId(), BaseInstructionSet.HALT); // 50
    }

    @Override
    public void completedParse(final VMError vmError, final CommandList commands) {
        // Save values on callback and release test thread
        _error = vmError;
        _commands = commands;
        _signal.countDown();// notify the count down latch
    }

}
