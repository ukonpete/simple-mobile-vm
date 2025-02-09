package com.slickpath.mobile.android.simple.vm.machine

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.machine.ProgramManager.Companion.START_LOC
import com.slickpath.mobile.android.simple.vm.util.Command
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class MemoryStoreTest {

    private lateinit var _memoryStore: MemoryStore

    @Before
    fun before() {
        _memoryStore = MemoryStore()
    }

    @Test
    fun testMemorySetup() {
        assertEquals(START_LOC, _memoryStore.programCounter)
        assertEquals(START_LOC, _memoryStore.programWriterPtr)
        assertEquals(MemoryStore.MAX_MEMORY, _memoryStore.memoryDump().size)
        val tempMemory = _memoryStore.memoryDump()
        for (value in tempMemory) {
            assertEquals(MemoryStore.EMPTY_MEMORY_VALUE, value)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.memoryDump].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.get].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.set].
     */
    @Test
    fun testProgramMemoryDump() {
        var memDump = _memoryStore.memoryDump()
        assertNotNull(memDump)
        assertEquals(MemoryStore.MAX_MEMORY, memDump.size)
        assertEquals(MemoryStore.EMPTY_MEMORY_VALUE, memDump[0])
        assertEquals(MemoryStore.EMPTY_MEMORY_VALUE, memDump[5])
        assertEquals(MemoryStore.EMPTY_MEMORY_VALUE, memDump[124])
        assertEquals(MemoryStore.EMPTY_MEMORY_VALUE, memDump[MemoryStore.MAX_MEMORY - 1])
        val values = intArrayOf(0, 10, 22, 34, 45, 57)
        for (value in values) {
            _memoryStore[value] = value
        }
        memDump = _memoryStore.memoryDump()
        assertNotNull(memDump)
        assertEquals(MemoryStore.MAX_MEMORY, memDump.size)
        for (value in values) {
            // check if our data matched memory dump
            assertEquals(value, memDump[value])
            // check if data in memory matches memory dump
            assertEquals(_memoryStore[value], memDump[value])
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.getCommand].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.setCommand] )}.
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.programMemoryDump].
     */
    @Test
    fun testGetCommand() {
        val instruction = intArrayOf(11, 22, 35, 46, 88, 99)
        val parameters = arrayOf(15, 27, -1, 64, 60, 101)
        val location = intArrayOf(0, 1, 2, 64, 101, 499)
        for (i in instruction.indices) {
            val params: MutableList<Int> = ArrayList()
            params.add(parameters[i])
            val command = Command(instruction[i], params)
            _memoryStore.setCommand(location[i], command)
        }
        val instructionDump = _memoryStore.programMemoryDump()
        for (i in instruction.indices) {
            val command = _memoryStore.getCommand(location[i])
            assertEquals(instruction[i], command.commandId)
            assertEquals(instruction[i], instructionDump[location[i]].commandId)
            assertEquals(parameters[i], command.parameters[0])
            assertEquals(parameters[i], instructionDump[location[i]].parameters[0])
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.programCounter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.incProgramCounter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.decProgramCounter].
     */
    @Test
    fun testGetProgramCounter() {
        assertEquals(START_LOC, _memoryStore.programCounter)
        for (i in 0..99) {
            _memoryStore.incProgramCounter()
        }
        assertEquals(100, _memoryStore.programCounter)
        for (i in 0..49) {
            _memoryStore.decProgramCounter()
        }
        assertEquals(50, _memoryStore.programCounter)
        for (i in 0..49) {
            _memoryStore.decProgramCounter()
        }
        assertEquals(START_LOC, _memoryStore.programCounter)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.programCounter].
     */
    @Test
    fun testSetProgramCounter() {
        assertEquals(START_LOC, _memoryStore.programCounter)
        _memoryStore.programCounter = 100
        assertEquals(100, _memoryStore.programCounter)
        _memoryStore.programCounter = 10
        assertEquals(10, _memoryStore.programCounter)
        _memoryStore.programCounter = 249
        assertEquals(249, _memoryStore.programCounter)
        _memoryStore.programCounter = START_LOC
        assertEquals(START_LOC, _memoryStore.programCounter)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.resetProgramCounter].
     */
    @Test
    fun testResetProgramCounter() {
        assertEquals(START_LOC, _memoryStore.programCounter)
        _memoryStore.programCounter = 100
        assertEquals(100, _memoryStore.programCounter)
        _memoryStore.resetProgramCounter()
        assertEquals(START_LOC, _memoryStore.programCounter)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.programWriterPtr].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.incrementProgramWriter].
     */
    @Test
    fun testGetProgramWriterPtr() {
        assertEquals(START_LOC, _memoryStore.programWriterPtr)
        for (i in 0..99) {
            _memoryStore.incrementProgramWriter()
        }
        assertEquals(100, _memoryStore.programWriterPtr)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.resetProgramWriter].
     */
    @Test
    fun testResetProgramWriter() {
        assertEquals(START_LOC, _memoryStore.programWriterPtr)
        for (i in 0..99) {
            _memoryStore.incrementProgramWriter()
        }
        assertEquals(100, _memoryStore.programWriterPtr)
        _memoryStore.resetProgramWriter()
        assertEquals(START_LOC, _memoryStore.programWriterPtr)
    }
}