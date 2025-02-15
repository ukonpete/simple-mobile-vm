package com.slickpath.mobile.android.simple.vm.machine

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.machine.ProgramManager.Companion.START_LOC
import com.slickpath.mobile.android.simple.vm.util.Command
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class KernelTest {
    private lateinit var _kernel: Kernel

    @Before
    fun before() {
        _kernel = Kernel()
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.getValueAt].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.setValueAt].
     */
    @Test
    fun testGetValueAt() {
        try {
            _kernel.setValueAt(0, 0)
            _kernel.setValueAt(1, 1)
            _kernel.setValueAt(2, 2)
            _kernel.setValueAt(3, 55)
            _kernel.setValueAt(4, 100)
            assertEquals(0, _kernel.getValueAt(0))
            assertEquals(1, _kernel.getValueAt(1))
            assertEquals(2, _kernel.getValueAt(2))
            assertEquals(3, _kernel.getValueAt(55))
            assertEquals(4, _kernel.getValueAt(100))
            for (i in 0 until MemoryStore.MAX_MEMORY) {
                if (i != 0 && i != 1 && i != 2 && i != 55 && i != 100) {
                    assertEquals(MemoryStore.EMPTY_MEMORY_VALUE, _kernel.getValueAt(i))
                }
            }
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.pop].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.push].
     */
    @Test
    fun testPop() {
        try {
            val values = intArrayOf(0, 10, 22, 34, 45, 57)
            for (value in values) {
                _kernel.push(value)
            }
            for (i in 0..99) {
                _kernel.push(i)
            }
            for (i in 0..99) {
                _kernel.pop() // ignore return val
            }
            for (i in values.indices.reversed()) {
                assertEquals(values[i], _kernel.pop())
            }
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.branch].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.resetProgramCounter].
     */
    @Test
    fun testBranch() {
        try {
            assertEquals(START_LOC, _kernel.programCounter)
            for (i in 0..99) {
                _kernel.incProgramCounter()
            }
            assertEquals(100, _kernel.programCounter)
            for (i in 0..19) {
                _kernel.decProgramCounter()
            }
            assertEquals(80, _kernel.programCounter)
            _kernel.branch(50)
            assertEquals(50, _kernel.programCounter)
            _kernel.branch(249)
            assertEquals(249, _kernel.programCounter)
            _kernel.branch(1)
            assertEquals(1, _kernel.programCounter)
            _kernel.resetProgramCounter()
            assertEquals(START_LOC, _kernel.programCounter)
            _kernel.branch(25)
            assertEquals(25, _kernel.programCounter)
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.jump].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.programCounter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.incProgramCounter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.decProgramCounter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.resetStack].
     */
    @Test
    fun testJump() {
        try {
            assertEquals(START_LOC, _kernel.programCounter)
            for (i in 0..99) {
                _kernel.incProgramCounter()
            }
            assertEquals(100, _kernel.programCounter)
            for (i in 0..19) {
                _kernel.decProgramCounter()
            }
            assertEquals(80, _kernel.programCounter)
            _kernel.push(2)
            _kernel.push(12)
            _kernel.push(72)
            _kernel.push(99)
            _kernel.jump()
            assertEquals(99, _kernel.programCounter)
            _kernel.jump()
            assertEquals(72, _kernel.programCounter)
            _kernel.jump()
            assertEquals(12, _kernel.programCounter)
            _kernel.resetStack()
            _kernel.push(7)
            assertEquals(12, _kernel.programCounter)
            _kernel.jump()
            assertEquals(7, _kernel.programCounter)
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.programWriterPtr].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.incrementProgramWriter].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.resetProgramWriter].
     */
    @Test
    fun testGetProgramWriterPtr() {
        assertEquals(START_LOC, _kernel.programWriterPtr)
        _kernel.incrementProgramWriter()
        _kernel.incrementProgramWriter()
        _kernel.incrementProgramWriter()
        assertEquals(3, _kernel.programWriterPtr)
        for (i in 0..99) {
            _kernel.incrementProgramWriter()
        }
        assertEquals(103, _kernel.programWriterPtr)
        _kernel.resetProgramWriter()
        assertEquals(START_LOC, _kernel.programWriterPtr)
        _kernel.incrementProgramWriter()
        _kernel.incrementProgramWriter()
        _kernel.incrementProgramWriter()
        assertEquals(3, _kernel.programWriterPtr)
        for (i in 0..99) {
            _kernel.incrementProgramWriter()
        }
        assertEquals(103, _kernel.programWriterPtr)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.getCommandAt].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.setCommandAt].
     */
    @Test
    fun testGetCommandAt() {
        try {
            val instruction = intArrayOf(11, 22, 35, 46, 88, 99)
            val parameters = arrayOf(15, 27, -1, 64, 60, 101)
            val location = intArrayOf(0, 1, 2, 64, 101, 499)
            for (i in instruction.indices) {
                val params: MutableList<Int> = ArrayList()
                if (parameters[i] != -1) {
                    params.add(parameters[i])
                }
                val command = Command(instruction[i], params)
                _kernel.setCommandAt(command, location[i])
            }
            val instructionDump = _kernel.dumpInstructionMemory()
            for (i in instruction.indices) {
                val command = _kernel.getCommandAt(location[i])
                assertEquals(instruction[i], command.commandId)
                assertEquals(instruction[i], instructionDump[location[i]].commandId)
                if (command.commandId == 35) {
                    assertEquals(0, command.parameters.size)
                } else {
                    assertEquals(parameters[i], command.parameters[0])
                    assertEquals(parameters[i], instructionDump[location[i]].parameters[0])
                }
            }
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Kernel.dumpStack].
     */
    @Test
    fun testDumpStack() {
        try {
            val values = intArrayOf(0, 10, 22, 34, 45, 57)
            for (value in values) {
                _kernel.push(value)
            }
            val stackDump = _kernel.dumpStack()
            assertNotNull(stackDump)
            assertEquals(values.size, stackDump.size)
            assertEquals(values[0], stackDump[0])
            assertEquals(values[1], stackDump[1])
            assertEquals(values[2], stackDump[2])
            assertEquals(values[3], stackDump[3])
            assertEquals(values[4], stackDump[4])
            assertEquals(values[5], stackDump[5])
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }
}