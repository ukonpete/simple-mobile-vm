package com.slickpath.mobile.android.simple.vm.machine

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.util.Command
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class ProgramManagerTest {

    private lateinit var _programManager: ProgramManager

    @Before
    fun setUp() {
        _programManager = ProgramManager()
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.programCounter].
     */
    @Test
    fun testSetProgramCounter() {
        assertEquals(Memory.START_LOC, _programManager.programCounter)
        _programManager.programCounter = 100
        assertEquals(100, _programManager.programCounter)
        _programManager.programCounter = 10
        assertEquals(10, _programManager.programCounter)
        _programManager.programCounter = 249
        assertEquals(249, _programManager.programCounter)
        _programManager.programCounter = Memory.START_LOC
        assertEquals(Memory.START_LOC, _programManager.programCounter)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.programCounter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.incProgramCounter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.decProgramCounter].
     */
    @Test
    fun testGetProgramCounter() {
        assertEquals(Memory.START_LOC, _programManager.programCounter)
        for (i in 0..99) {
            _programManager.incProgramCounter()
        }
        assertEquals(100, _programManager.programCounter)
        for (i in 0..49) {
            _programManager.decProgramCounter()
        }
        assertEquals(50, _programManager.programCounter)
        for (i in 0..49) {
            _programManager.decProgramCounter()
        }
        assertEquals(Memory.START_LOC, _programManager.programCounter)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.resetProgramCounter].
     */
    @Test
    fun testResetProgramCounter() {
        assertEquals(Memory.START_LOC, _programManager.programCounter)
        _programManager.programCounter = 100
        assertEquals(100, _programManager.programCounter)
        _programManager.resetProgramCounter()
        assertEquals(Memory.START_LOC, _programManager.programCounter)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.programWriterPtr].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.incProgramWriter].
     */
    @Test
    fun testGetProgramWriterPtr() {
        assertEquals(Memory.START_LOC, _programManager.programWriterPtr)
        for (i in 0..99) {
            _programManager.incProgramWriter()
        }
        assertEquals(100, _programManager.programWriterPtr)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.getCommandAt].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.setCommandAt].
     */
    @Test
    fun testGetCommandAt() {
        val instruction = intArrayOf(11, 22, 35, 46, 88, 99)
        val parameters = arrayOf(15, 27, -1, 64, 60, 101)
        val location = intArrayOf(0, 1, 2, 64, 101, 499)
        for (i in instruction.indices) {
            val params: MutableList<Int> = ArrayList()
            params.add(parameters[i])
            val command = Command(instruction[i], params)
            _programManager.setCommandAt(location[i], command)
        }
        val instructionDump = _programManager.dumpProgramStore()
        for (i in instruction.indices) {
            val command = _programManager.getCommandAt(location[i])
            assertEquals(instruction[i], command.commandId)
            assertEquals(instruction[i], instructionDump[location[i]].commandId)
            assertEquals(parameters[i], command.parameters[0])
            assertEquals(parameters[i], instructionDump[location[i]].parameters[0])
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.ProgramManager.resetProgramWriter].
     */
    @Test
    fun testResetProgramWriter() {
        assertEquals(Memory.START_LOC, _programManager.programWriterPtr)
        for (i in 0..99) {
            _programManager.incProgramWriter()
        }
        assertEquals(100, _programManager.programWriterPtr)
        _programManager.resetProgramWriter()
        assertEquals(Memory.START_LOC, _programManager.programWriterPtr)
    }
}