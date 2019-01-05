package com.slickpath.mobile.android.simple.vm.machine;


import android.support.test.runner.AndroidJUnit4;

import com.slickpath.mobile.android.simple.vm.util.Command;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4.class)
public class ProgramManagerTest {

    private ProgramManager _programManager = null;

    @Before
    public void setUp() {
        _programManager = new ProgramManager();

    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#setProgramCounter(int)}.
     */
    @Test
    public void testSetProgramCounter() {
        assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
        _programManager.setProgramCounter(100);
        assertEquals(100, _programManager.getProgramCounter());
        _programManager.setProgramCounter(10);
        assertEquals(10, _programManager.getProgramCounter());
        _programManager.setProgramCounter(249);
        assertEquals(249, _programManager.getProgramCounter());
        _programManager.setProgramCounter(Memory.START_LOC);
        assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#getProgramCounter()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#incProgramCounter()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#decProgramCounter()}.
     */
    @Test
    public void testGetProgramCounter() {
        assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
        for (int i = 0; i < 100; i++) {
            _programManager.incProgramCounter();
        }
        assertEquals(100, _programManager.getProgramCounter());
        for (int i = 0; i < 50; i++) {
            _programManager.decProgramCounter();
        }
        assertEquals(50, _programManager.getProgramCounter());
        for (int i = 0; i < 50; i++) {
            _programManager.decProgramCounter();
        }
        assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#resetProgramCounter()}.
     */
    @Test
    public void testResetProgramCounter() {
        assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
        _programManager.setProgramCounter(100);
        assertEquals(100, _programManager.getProgramCounter());
        _programManager.resetProgramCounter();
        assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#getProgramWriterPtr()}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#incProgramWriter()}.
     */
    @Test
    public void testGetProgramWriterPtr() {
        assertEquals(Memory.START_LOC, _programManager.getProgramWriterPtr());
        for (int i = 0; i < 100; i++) {
            _programManager.incProgramWriter();
        }
        assertEquals(100, _programManager.getProgramWriterPtr());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#getCommandAt(int)}.
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#setCommandAt(int, Command)}.
     */
    @Test
    public void testGetCommandAt() {
        final int[] instruction = new int[]{11, 22, 35, 46, 88, 99};
        final Integer[] parameters = new Integer[]{15, 27, null, 64, 60, 101};
        final int[] location = new int[]{0, 1, 2, 64, 101, 499};

        for (int i = 0; i < instruction.length; i++) {
            final List<Integer> params = new ArrayList<>();
            params.add(parameters[i]);
            final Command command = new Command(instruction[i], params);
            _programManager.setCommandAt(location[i], command);
        }

        final List<Command> instructionDump = _programManager.dumpProgramStore();
        for (int i = 0; i < instruction.length; i++) {
            final Command command = _programManager.getCommandAt(location[i]);
            assertEquals(instruction[i], command.getCommandId().intValue());
            assertEquals(instruction[i], instructionDump.get(location[i]).getCommandId().intValue());
            assertEquals(parameters[i], command.getParameters().get(0));
            if (parameters[i] == null) {
                assertNull(instructionDump.get(location[i]).getParameters().get(0));
            } else {
                assertEquals(parameters[i], instructionDump.get(location[i]).getParameters().get(0));
            }
        }

    }


    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#resetProgramWriter()}.
     */
    @Test
    public void testResetProgramWriter() {
        assertEquals(Memory.START_LOC, _programManager.getProgramWriterPtr());
        for (int i = 0; i < 100; i++) {
            _programManager.incProgramWriter();
        }
        assertEquals(100, _programManager.getProgramWriterPtr());
        _programManager.resetProgramWriter();
        assertEquals(Memory.START_LOC, _programManager.getProgramWriterPtr());
    }
}
