package com.slickpath.mobile.android.simple.vm.machine

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.VMError
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class MachineTest {

    companion object {
        private const val MEM_LOC_1 = 10
        private const val RESULT_LOC = 5
        private const val LOW_VAL = 15
        private const val HIGH_VAL = 17
    }

    private lateinit var _machine: Machine
    
    @Before
    fun before() {
        _machine = Machine()
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.ADD].
     */
    @Test
    fun testADD() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.ADD()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            val valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(HIGH_VAL + LOW_VAL, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("ADD should have thrown VMError", didMethodFail(_machine, "ADD"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.SUB].
     */
    @Test
    fun testSUB() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.SUB()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            val valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(HIGH_VAL - LOW_VAL, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("ADD should have thrown VMError", didMethodFail(_machine, "SUB"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.MUL].
     */
    @Test
    fun testMUL() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.MUL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            val valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(HIGH_VAL * LOW_VAL, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("MUL should have thrown VMError", didMethodFail(_machine, "MUL"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.DIV].
     */
    @Test
    fun testDIV() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.DIV()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            val valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(HIGH_VAL / LOW_VAL, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("DIV should have thrown VMError", didMethodFail(_machine, "DIV"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.NEG].
     */
    @Test
    fun testNEG() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.NEG()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            val valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(-LOW_VAL, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("NEG should have thrown VMError", didMethodFail(_machine, "NEG"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.EQUAL].
     */
    @Test
    fun testEQUAL() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.EQUAL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            var valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("EQUAL should have thrown VMError", didMethodFail(_machine, "EQUAL"))
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.EQUAL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(1, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("EQUAL should have thrown VMError", didMethodFail(_machine, "EQUAL"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.NOTEQL].
     */
    @Test
    fun testNOTEQL() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.NOTEQL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            var valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(1, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("NOTEQL should have thrown VMError", didMethodFail(_machine, "NOTEQL"))
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.NOTEQL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("NOTEQL should have thrown VMError", didMethodFail(_machine, "NOTEQL"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.GREATER].
     */
    @Test
    fun testGREATER() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.GREATER()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            var valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(1, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("GREATER should have thrown VMError", didMethodFail(_machine, "GREATER"))
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.GREATER()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("GREATER should have thrown VMError", didMethodFail(_machine, "GREATER"))
            _machine.PUSHC(HIGH_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.GREATER()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("GREATER should have thrown VMError", didMethodFail(_machine, "GREATER"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.LESS].
     */
    @Test
    fun testLESS() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.LESS()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            var valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("LESS should have thrown VMError", didMethodFail(_machine, "LESS"))
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.LESS()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("LESS should have thrown VMError", didMethodFail(_machine, "LESS"))
            _machine.PUSHC(HIGH_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.LESS()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(1, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("LESS should have thrown VMError", didMethodFail(_machine, "LESS"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.GTREQL].
     */
    @Test
    fun testGTREQL() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.GTREQL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            var valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(1, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("GTREQL should have thrown VMError", didMethodFail(_machine, "GTREQL"))
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.GTREQL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(1, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("GTREQL should have thrown VMError", didMethodFail(_machine, "GTREQL"))
            _machine.PUSHC(HIGH_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.GTREQL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("GTREQL should have thrown VMError", didMethodFail(_machine, "GTREQL"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.LSSEQL].
     */
    @Test
    fun testLSSEQL() {
        try {
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(HIGH_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.LSSEQL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            var valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("LSSEQL should have thrown VMError", didMethodFail(_machine, "LSSEQL"))
            _machine.PUSHC(LOW_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.LSSEQL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(1, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("LSSEQL should have thrown VMError", didMethodFail(_machine, "LSSEQL"))
            _machine.PUSHC(HIGH_VAL)
            _machine.setValueAt(LOW_VAL, MEM_LOC_1)
            _machine.PUSH(MEM_LOC_1)
            _machine.LSSEQL()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(1, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("LSSEQL should have thrown VMError", didMethodFail(_machine, "LSSEQL"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.NOT].
     */
    @Test
    fun testNOT() {
        try {
            _machine.PUSHC(0)
            _machine.NOT()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            var valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(1, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("NOT should have thrown VMError", didMethodFail(_machine, "NOT"))
            _machine.PUSHC(1)
            _machine.NOT()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("NOT should have thrown VMError", didMethodFail(_machine, "NOT"))
            _machine.PUSHC(-1)
            _machine.NOT()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("NOT should have thrown VMError", didMethodFail(_machine, "NOT"))
            _machine.PUSHC(14)
            _machine.NOT()
            _machine.PUSHC(RESULT_LOC)
            _machine.POP()
            valAt = _machine.getValueAt(RESULT_LOC)
            assertEquals(0, valAt)
            assertTrue("POP should have thrown VMError", didPopFail())
            assertTrue("NOT should have thrown VMError", didMethodFail(_machine, "NOT"))
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.PUSH].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.POPC].
     */
    @Test
    fun testPUSH() {
        try {
            val values = intArrayOf(20, 15, 17, 44, 101)
            val locations = intArrayOf(33, 45, 62, 77, 249)
            for (i in values.indices) {
                _machine.setValueAt(values[i], locations[i])
                _machine.PUSH(locations[i])
            }
            val stackDump = _machine.dumpStack()
            for (i in values.indices) {
                val memVal = _machine.getValueAt(locations[i])
                assertEquals(values[i], memVal)
                val stackVal = stackDump[i]
                assertEquals(values[i], stackVal)
                _machine.POPC(i)
                val poppedVal = _machine.getValueAt(i)
                assertEquals(values[values.size - i - 1], poppedVal)
            }
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.PUSHC].
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.POPC].
     */
    @Test
    fun testPUSHC() {
        try {
            val values = intArrayOf(20, 15, 17, 44, 101)
            for (valAt in values) {
                _machine.PUSHC(valAt)
            }
            val stackDump = _machine.dumpStack()
            for (i in values.indices) {
                val stackVal = stackDump[i]
                assertEquals(values[i], stackVal)
                _machine.POPC(i)
                val poppedVal = _machine.getValueAt(i)
                assertEquals(values[values.size - i - 1], poppedVal)
            }
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.POP].
     */
    @Test
    fun testPOP() {
        try {
            val values = intArrayOf(20, 15, 17, 44, 101)
            val locations = intArrayOf(33, 45, 62, 77, 249)
            for (i in values.indices) {
                _machine.PUSHC(values[i])
                _machine.PUSHC(locations[i])
            }
            val stackDump = _machine.dumpStack()
            for (i in values.indices) {
                val stackVal = stackDump[i * 2]
                val stackValLoc = stackDump[i * 2 + 1]
                assertEquals(values[i], stackVal)
                assertEquals(locations[i], stackValLoc)
                _machine.POP()
                val poppedVal = _machine.getValueAt(locations[values.size - i - 1])
                assertEquals(values[values.size - i - 1], poppedVal)
            }
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.BRANCH].
     */
    @Test
    fun testBRANCH() {
        try {
            assertEquals(Memory.START_LOC, _machine.programCounter)
            for (i in 0..99) {
                _machine.incProgramCounter()
            }
            assertEquals(100, _machine.programCounter)
            for (i in 0..19) {
                _machine.decProgramCounter()
            }
            assertEquals(80, _machine.programCounter)
            _machine.branch(50)
            assertEquals(50, _machine.programCounter)
            _machine.branch(249)
            assertEquals(249, _machine.programCounter)
            _machine.branch(1)
            assertEquals(1, _machine.programCounter)
            _machine.resetProgramCounter()
            assertEquals(Memory.START_LOC, _machine.programCounter)
            _machine.branch(25)
            assertEquals(25, _machine.programCounter)
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.JUMP].
     */
    @Test
    fun testJUMP() {
        try {
            assertEquals(Memory.START_LOC, _machine.programCounter)
            for (i in 0..99) {
                _machine.incProgramCounter()
            }
            assertEquals(100, _machine.programCounter)
            for (i in 0..19) {
                _machine.decProgramCounter()
            }
            assertEquals(80, _machine.programCounter)
            _machine.push(2)
            _machine.push(12)
            _machine.push(72)
            _machine.push(99)
            _machine.jump()
            assertEquals(99, _machine.programCounter)
            _machine.jump()
            assertEquals(72, _machine.programCounter)
            _machine.jump()
            assertEquals(12, _machine.programCounter)
            _machine.resetStack()
            _machine.push(7)
            assertEquals(12, _machine.programCounter)
            _machine.jump()
            assertEquals(7, _machine.programCounter)
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.BREQL].
     */
    @Test
    fun testBREQL() {
        try {
            assertEquals(Memory.START_LOC, _machine.programCounter)
            for (i in 0..99) {
                _machine.incProgramCounter()
            }
            assertEquals(100, _machine.programCounter)
            for (i in 0..19) {
                _machine.decProgramCounter()
            }
            assertEquals(80, _machine.programCounter)
            _machine.PUSHC(1)
            _machine.BREQL(12)
            assertEquals(80, _machine.programCounter)
            _machine.PUSHC(0)
            _machine.BREQL(56)
            assertEquals(56, _machine.programCounter)
            var bFailed = false
            try {
                _machine.PUSHC(0)
                _machine.BREQL(602)
            } catch (vmError: VMError) {
                bFailed = true
            }
            assertTrue("BREQL 602 is out of bounds", bFailed)
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.BRLSS].
     */
    @Test
    fun testBRLSS() {
        try {
            assertEquals(Memory.START_LOC, _machine.programCounter)
            for (i in 0..99) {
                _machine.incProgramCounter()
            }
            assertEquals(100, _machine.programCounter)
            for (i in 0..19) {
                _machine.decProgramCounter()
            }
            assertEquals(80, _machine.programCounter)
            _machine.PUSHC(1)
            _machine.BRLSS(12)
            assertEquals(80, _machine.programCounter)
            _machine.PUSHC(0)
            _machine.BRLSS(12)
            assertEquals(80, _machine.programCounter)
            _machine.PUSHC(-1)
            _machine.BRLSS(56)
            assertEquals(56, _machine.programCounter)
            var bFailed = false
            try {
                _machine.PUSHC(-782)
                _machine.BRLSS(602)
            } catch (vmError: VMError) {
                bFailed = true
            }
            assertTrue("BREQL 602 is out of bounds", bFailed)
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.BRGTR].
     */
    @Test
    fun testBRGTR() {
        try {
            assertEquals(Memory.START_LOC, _machine.programCounter)
            for (i in 0..99) {
                _machine.incProgramCounter()
            }
            assertEquals(100, _machine.programCounter)
            for (i in 0..19) {
                _machine.decProgramCounter()
            }
            assertEquals(80, _machine.programCounter)
            _machine.PUSHC(-1)
            _machine.BRGTR(12)
            assertEquals(80, _machine.programCounter)
            _machine.PUSHC(0)
            _machine.BRGTR(12)
            assertEquals(80, _machine.programCounter)
            _machine.PUSHC(1)
            _machine.BRGTR(56)
            assertEquals(56, _machine.programCounter)
            var bFailed = false
            try {
                _machine.PUSHC(782)
                _machine.BRGTR(602)
            } catch (vmError: VMError) {
                bFailed = true
            }
            assertTrue("BRGTR 602 is out of bounds", bFailed)
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.CONTENTS].
     */
    @Test
    fun testCONTENTS() {
        try {
            val values = intArrayOf(20, 15, 17, 44, 101)
            val locations = intArrayOf(33, 45, 62, 77, 249)
            for (i in values.indices) {
                _machine.PUSHC(locations[i])
                _machine.setValueAt(values[i], locations[i])
            }
            for (i in values.indices) {
                _machine.CONTENTS()
                _machine.POPC(i)
            }
            for (i in values.indices) {
                val valAt = _machine.getValueAt(i)
                assertEquals(values[values.size - i - 1], valAt)
            }
            var bFailed = false
            try {
                _machine.CONTENTS()
            } catch (vmError: VMError) {
                bFailed = true
            }
            assertTrue("CONTENTS is out of bounds", bFailed)
        } catch (e: VMError) {
            e.printStackTrace()
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.Machine.HALT].
     */
    @Test
    fun testHALT() {
        val memory = _machine.dumpMemory()
        val stack = _machine.dumpStack()
        for (i in 0..99) {
            _machine.incProgramCounter()
        }
        assertEquals(100, _machine.programCounter)
        for (i in 0..19) {
            _machine.decProgramCounter()
        }
        assertEquals(80, _machine.programCounter)
        _machine.HALT()
        assertEquals(80, _machine.programCounter)
        _machine.HALT()
        assertEquals(80, _machine.programCounter)
        assertEquals(_machine.dumpMemory(), memory)
        assertEquals(_machine.dumpStack(), stack)
    }

    /**
     * Does a POP and returns if it failed or not (true if VMError is thrown)
     *
     * @return true if pop failed
     */
    private fun didPopFail(): Boolean {
        var bFailed = false
        try {
            _machine.POP()
        } catch (e: VMError) {
            bFailed = true
        }
        return bFailed
    }

    /**
     * Does a Machine.Method fail and returns if it failed or not (true if VMError is thrown)
     * Uses reflection so that this one method could be called in place of having the code repeated over and over again for each
     * method.
     *
     * @param machine Machine object
     * @param methodName name of method to check
     * @return true if method failed
     */
    private fun didMethodFail(machine: Machine?, methodName: String): Boolean {
        var bFailed = false
        val method: Method
        try {
            method = Machine::class.java.getDeclaredMethod(methodName)
            method.invoke(machine)
        } catch (e: InvocationTargetException) {
            if (e.cause is VMError) {
                bFailed = true
                println("VMError for instruction failed: $methodName")
            } else {
                e.printStackTrace()
            }
        } catch (e1: SecurityException) {
            e1.printStackTrace()
        } catch (e1: NoSuchMethodException) {
            e1.printStackTrace()
        } catch (e: Exception) {
            bFailed = true
        }
        return bFailed
    }
}