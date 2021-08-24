package com.slickpath.mobile.android.simple.vm.machine

import androidx.test.ext.junit.runners.AndroidJUnit4
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
class MemoryTest {
    
    private lateinit var _memory: Memory
    
    @Before
    fun before() {
        _memory = Memory()
    }

    @Test
    fun testMemorySetup() {
        assertTrue(_memory.isStackEmpty)
        assertEquals(Memory.START_LOC, _memory.programCounter)
        assertEquals(Memory.START_LOC, _memory.programWriterPtr)
        assertTrue(_memory.isStackEmpty)
        assertEquals(Memory.MAX_MEMORY, _memory.memoryDump().size)
        val tempMemory = _memory.memoryDump()
        for (value in tempMemory) {
            assertEquals(Memory.EMPTY_MEMORY_VALUE, value)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.pushMEM].
     */
    @Test
    fun testPush_mem() {
        val values = intArrayOf(0, 10, 22, 34, 45, 57)
        assertTrue(_memory.isStackEmpty)
        for (value in values) {
            _memory.pushMEM(value)
            assertTrue(!_memory.isStackEmpty)
        }
        for (j in 0..99) {
            _memory.pushMEM(111)
        }
        for (j in 0..99) {
            _memory.popMEM()
        }
        for (value in values.indices.reversed()) {
            assertTrue(!_memory.isStackEmpty)
            assertEquals(values[value], _memory.popMEM())
        }
        assertTrue(_memory.isStackEmpty)
    }

    /**
     * Test method for [Memory.popMEM]}.
     */
    @Test
    fun testPop_mem() {
        val values = intArrayOf(0, 10, 22, 34, 45, 57)
        assertTrue(_memory.isStackEmpty)
        for (value in values) {
            _memory.pushMEM(value)
        }
        for (j in 0..99) {
            _memory.pushMEM(111)
        }
        for (j in 0..49) {
            _memory.popMEM()
        }
        for (j in 0..49) {
            _memory.popMEM()
        }
        for (value in values.indices.reversed()) {
            _memory.pushMEM(values[value])
            assertEquals(values[value], _memory.popMEM())
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.isStackEmpty].
     */
    @Test
    fun testIsStackEmpty() {
        assertTrue(_memory.isStackEmpty)
        for (j in 0..99) {
            _memory.pushMEM(111)
            assertTrue(!_memory.isStackEmpty)
        }
        for (j in 0..49) {
            _memory.popMEM()
            assertTrue(!_memory.isStackEmpty)
        }
        for (j in 0..48) {
            _memory.popMEM()
            assertTrue(!_memory.isStackEmpty)
        }
        _memory.popMEM()
        assertTrue(_memory.isStackEmpty)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.memoryDump].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.get].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.set].
     */
    @Test
    fun testProgramMemoryDump() {
        var memDump = _memory.memoryDump()
        assertNotNull(memDump)
        assertEquals(Memory.MAX_MEMORY, memDump.size)
        assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump[0])
        assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump[5])
        assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump[124])
        assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump[Memory.MAX_MEMORY - 1])
        val values = intArrayOf(0, 10, 22, 34, 45, 57)
        assertTrue(_memory.isStackEmpty)
        for (value in values) {
            _memory[value] = value
        }
        memDump = _memory.memoryDump()
        assertNotNull(memDump)
        assertEquals(Memory.MAX_MEMORY, memDump.size)
        for (value in values) {
            // check if our data matched memory dump
            assertEquals(value, memDump[value])
            // check if data in memory matches memory dump
            assertEquals(_memory[value], memDump[value])
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.getCommand].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.setCommand] )}.
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.programMemoryDump].
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
            _memory.setCommand(location[i], command)
        }
        val instructionDump = _memory.programMemoryDump()
        for (i in instruction.indices) {
            val command = _memory.getCommand(location[i])
            assertEquals(instruction[i], command.commandId)
            assertEquals(instruction[i], instructionDump[location[i]].commandId)
            assertEquals(parameters[i], command.parameters[0])
            assertEquals(parameters[i], instructionDump[location[i]].parameters[0])
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.resetStack].
     */
    @Test
    fun testResetStack() {
        assertTrue(_memory.isStackEmpty)
        for (j in 0..99) {
            _memory.pushMEM(111)
            assertTrue(!_memory.isStackEmpty)
        }
        assertTrue(!_memory.isStackEmpty)
        _memory.resetStack()
        assertTrue(_memory.isStackEmpty)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.stackDump].
     */
    @Test
    fun testStackDump() {
        val values = intArrayOf(0, 10, 22, 34, 45, 57)
        assertTrue(_memory.isStackEmpty)
        for (value in values) {
            _memory.pushMEM(value)
        }
        val stackDump = _memory.stackDump()
        assertNotNull(stackDump)
        assertEquals(values.size, stackDump.size)
        assertEquals(values[0], stackDump[0])
        assertEquals(values[1], stackDump[1])
        assertEquals(values[2], stackDump[2])
        assertEquals(values[3], stackDump[3])
        assertEquals(values[4], stackDump[4])
        assertEquals(values[5], stackDump[5])
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.programCounter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.incProgramCounter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.decProgramCounter].
     */
    @Test
    fun testGetProgramCounter() {
        assertEquals(Memory.START_LOC, _memory.programCounter)
        for (i in 0..99) {
            _memory.incProgramCounter()
        }
        assertEquals(100, _memory.programCounter)
        for (i in 0..49) {
            _memory.decProgramCounter()
        }
        assertEquals(50, _memory.programCounter)
        for (i in 0..49) {
            _memory.decProgramCounter()
        }
        assertEquals(Memory.START_LOC, _memory.programCounter)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.programCounter].
     */
    @Test
    fun testSetProgramCounter() {
        assertEquals(Memory.START_LOC, _memory.programCounter)
        _memory.programCounter = 100
        assertEquals(100, _memory.programCounter)
        _memory.programCounter = 10
        assertEquals(10, _memory.programCounter)
        _memory.programCounter = 249
        assertEquals(249, _memory.programCounter)
        _memory.programCounter = Memory.START_LOC
        assertEquals(Memory.START_LOC, _memory.programCounter)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.resetProgramCounter].
     */
    @Test
    fun testResetProgramCounter() {
        assertEquals(Memory.START_LOC, _memory.programCounter)
        _memory.programCounter = 100
        assertEquals(100, _memory.programCounter)
        _memory.resetProgramCounter()
        assertEquals(Memory.START_LOC, _memory.programCounter)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.programWriterPtr].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.incrementProgramWriter].
     */
    @Test
    fun testGetProgramWriterPtr() {
        assertEquals(Memory.START_LOC, _memory.programWriterPtr)
        for (i in 0..99) {
            _memory.incrementProgramWriter()
        }
        assertEquals(100, _memory.programWriterPtr)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Memory.resetProgramWriter].
     */
    @Test
    fun testResetProgramWriter() {
        assertEquals(Memory.START_LOC, _memory.programWriterPtr)
        for (i in 0..99) {
            _memory.incrementProgramWriter()
        }
        assertEquals(100, _memory.programWriterPtr)
        _memory.resetProgramWriter()
        assertEquals(Memory.START_LOC, _memory.programWriterPtr)
    }
}