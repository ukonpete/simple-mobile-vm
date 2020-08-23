package com.slickpath.mobile.android.simple.vm.machine;


import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.util.Command;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4.class)
public class KernelTest {

    private Kernel _kernel = null;

    @Before
    public void before() {
        _kernel = new Kernel();
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#getValueAt(int)}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#setValueAt(int, int)}.
     */
    @Test
    public void testGetValueAt() {
        try {
            _kernel.setValueAt(0, 0);
            _kernel.setValueAt(1, 1);
            _kernel.setValueAt(2, 2);
            _kernel.setValueAt(3, 55);
            _kernel.setValueAt(4, 100);

            assertEquals(0, _kernel.getValueAt(0));
            assertEquals(1, _kernel.getValueAt(1));
            assertEquals(2, _kernel.getValueAt(2));
            assertEquals(3, _kernel.getValueAt(55));
            assertEquals(4, _kernel.getValueAt(100));

            for (int i = 0; i < Memory.MAX_MEMORY; i++) {
                if ((i != 0) && (i != 1) && (i != 2) && (i != 55) && (i != 100)) {
                    assertEquals(Memory.EMPTY_MEMORY_VALUE, _kernel.getValueAt(i));
                }
            }
        } catch (@NonNull final VMError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#pop()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#push(int)}.
     */
    @Test
    public void testPop() {
        try {
            final int[] values = {0, 10, 22, 34, 45, 57};
            for (final int value : values) {
                _kernel.push(value);
            }
            for (int i = 0; i < 100; i++) {
                _kernel.push(i);
            }
            for (int i = 0; i < 100; i++) {
                _kernel.pop(); // ignore return val
            }
            for (int i = values.length - 1; i >= 0; i--) {
                assertEquals(values[i], _kernel.pop());
            }
        } catch (@NonNull final VMError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#branch(int)}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#resetProgramCounter()}.
     */
    @Test
    public void testBranch() {
        try {
            assertEquals(Memory.START_LOC, _kernel.getProgramCounter());
            for (int i = 0; i < 100; i++) {
                _kernel.incProgramCounter();
            }
            assertEquals(100, _kernel.getProgramCounter());
            for (int i = 0; i < 20; i++) {
                _kernel.decProgramCounter();
            }
            assertEquals(80, _kernel.getProgramCounter());
            _kernel.branch(50);
            assertEquals(50, _kernel.getProgramCounter());
            _kernel.branch(249);
            assertEquals(249, _kernel.getProgramCounter());
            _kernel.branch(1);
            assertEquals(1, _kernel.getProgramCounter());
            _kernel.resetProgramCounter();
            assertEquals(Memory.START_LOC, _kernel.getProgramCounter());
            _kernel.branch(25);
            assertEquals(25, _kernel.getProgramCounter());
        } catch (@NonNull final VMError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#jump()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#getProgramCounter()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#incProgramCounter()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#decProgramCounter()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#resetStack()}.
     */
    @Test
    public void testJump() {
        try {

            assertEquals(Memory.START_LOC, _kernel.getProgramCounter());
            for (int i = 0; i < 100; i++) {
                _kernel.incProgramCounter();
            }
            assertEquals(100, _kernel.getProgramCounter());
            for (int i = 0; i < 20; i++) {
                _kernel.decProgramCounter();
            }
            assertEquals(80, _kernel.getProgramCounter());
            _kernel.push(2);
            _kernel.push(12);
            _kernel.push(72);
            _kernel.push(99);
            _kernel.jump();
            assertEquals(99, _kernel.getProgramCounter());
            _kernel.jump();
            assertEquals(72, _kernel.getProgramCounter());
            _kernel.jump();
            assertEquals(12, _kernel.getProgramCounter());
            _kernel.resetStack();
            _kernel.push(7);
            assertEquals(12, _kernel.getProgramCounter());
            _kernel.jump();
            assertEquals(7, _kernel.getProgramCounter());
        } catch (@NonNull final VMError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#getProgramWriterPtr()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#incProgramWriter()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#resetProgramWriter()}.
     */
    @Test
    public void testGetProgramWriterPtr() {
        assertEquals(Memory.START_LOC, _kernel.getProgramWriterPtr());
        _kernel.incProgramWriter();
        _kernel.incProgramWriter();
        _kernel.incProgramWriter();
        assertEquals(3, _kernel.getProgramWriterPtr());
        for (int i = 0; i < 100; i++) {
            _kernel.incProgramWriter();
        }
        assertEquals(103, _kernel.getProgramWriterPtr());
        _kernel.resetProgramWriter();
        assertEquals(Memory.START_LOC, _kernel.getProgramWriterPtr());
        _kernel.incProgramWriter();
        _kernel.incProgramWriter();
        _kernel.incProgramWriter();
        assertEquals(3, _kernel.getProgramWriterPtr());
        for (int i = 0; i < 100; i++) {
            _kernel.incProgramWriter();
        }
        assertEquals(103, _kernel.getProgramWriterPtr());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#getCommandAt(int)}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#setCommandAt(Command, int)}.
     */
    @Test
    public void testGetCommandAt() {
        try {
            final int[] instruction = new int[]{11, 22, 35, 46, 88, 99};
            final Integer[] parameters = new Integer[]{15, 27, null, 64, 60, 101};
            final int[] location = new int[]{0, 1, 2, 64, 101, 499};

            for (int i = 0; i < instruction.length; i++) {
                final List<Integer> params = new ArrayList<>();
                params.add(parameters[i]);
                final Command command = new Command(instruction[i], params);
                _kernel.setCommandAt(command, location[i]);
            }

            final List<Command> instructionDump = _kernel.dumpInstructionMemory();
            for (int i = 0; i < instruction.length; i++) {
                final Command command = _kernel.getCommandAt(location[i]);
                assertEquals(instruction[i], command.getCommandId().intValue());
                assertEquals(instruction[i], instructionDump.get(location[i]).getCommandId().intValue());
                assertEquals(parameters[i], command.getParameters().get(0));
                if (parameters[i] == null) {
                    assertNull(instructionDump.get(location[i]).getParameters().get(0));
                } else {
                    assertEquals(parameters[i], instructionDump.get(location[i]).getParameters().get(0));
                }
            }
        } catch (@NonNull final VMError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#dumpStack()}.
     */
    @Test
    public void testDumpStack() {
        try {
            final int[] values = {0, 10, 22, 34, 45, 57};

            for (final int value : values) {
                _kernel.push(value);
            }
            final List<Integer> stackDump = _kernel.dumpStack();
            assertNotNull(stackDump);
            assertEquals(values.length, stackDump.size());
            assertEquals(values[0], stackDump.get(0).intValue());
            assertEquals(values[1], stackDump.get(1).intValue());
            assertEquals(values[2], stackDump.get(2).intValue());
            assertEquals(values[3], stackDump.get(3).intValue());
            assertEquals(values[4], stackDump.get(4).intValue());
            assertEquals(values[5], stackDump.get(5).intValue());
        } catch (@NonNull final VMError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
