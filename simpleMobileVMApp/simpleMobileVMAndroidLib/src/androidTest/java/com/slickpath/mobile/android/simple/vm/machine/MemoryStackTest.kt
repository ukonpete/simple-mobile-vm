package com.slickpath.mobile.android.simple.vm.machine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MemoryStackTest {

    private lateinit var memoryStack: MemoryStack

    @Before
    fun before() {
        memoryStack = MemoryStack()
    }

    @Test
    fun testMemorySetup() {
        assertTrue(memoryStack.isEmpty)
        val tempMemory = memoryStack.stackDump()
        assertEquals(0, tempMemory.size)
    }

    @Test
    fun testMemorySize() {
        assertTrue(memoryStack.isEmpty)
        memoryStack.pushValue(1)
        var tempMemory = memoryStack.stackDump()
        assertEquals(1, tempMemory.size)
        for (i in 1..5) {
            memoryStack.pushValue(1)
        }
        tempMemory = memoryStack.stackDump()
        assertEquals(6, tempMemory.size)
        for (i in 1..99) {
            memoryStack.pushValue(1)
        }
        tempMemory = memoryStack.stackDump()
        assertEquals(105, tempMemory.size)
    }


    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStack.pushValue].
     */
    @Test
    fun testPushValue() {
        val values = intArrayOf(0, 10, 22, 34, 45, 57)
        assertTrue(memoryStack.isEmpty)
        for (value in values) {
            memoryStack.pushValue(value)
            assertFalse(memoryStack.isEmpty)
        }
        for (j in 0..99) {
            memoryStack.pushValue(111)
        }
        for (j in 0..99) {
            memoryStack.popValue()
        }
        for (value in values.indices.reversed()) {
            assertTrue(!memoryStack.isEmpty)
            assertEquals(values[value], memoryStack.popValue())
        }
        assertTrue(memoryStack.isEmpty)
    }


    /**
     * Test method for [MemoryStack.popValue]}.
     */
    @Test
    fun testPop_mem() {
        val values = intArrayOf(0, 10, 22, 34, 45, 57)
        assertTrue(memoryStack.isEmpty)
        for (value in values) {
            memoryStack.pushValue(value)
        }
        for (j in 0..99) {
            memoryStack.pushValue(111)
        }
        for (j in 0..49) {
            memoryStack.popValue()
        }
        for (j in 0..49) {
            memoryStack.popValue()
        }
        for (value in values.indices.reversed()) {
            memoryStack.pushValue(values[value])
            assertEquals(values[value], memoryStack.popValue())
        }
    }


    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStack.isEmpty].
     */
    @Test
    fun testisEmpty() {
        assertTrue(memoryStack.isEmpty)
        for (j in 0..99) {
            memoryStack.pushValue(111)
            assertTrue(!memoryStack.isEmpty)
        }
        for (j in 0..49) {
            memoryStack.popValue()
            assertTrue(!memoryStack.isEmpty)
        }
        for (j in 0..48) {
            memoryStack.popValue()
            assertTrue(!memoryStack.isEmpty)
        }
        memoryStack.popValue()
        assertTrue(memoryStack.isEmpty)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStack.reset].
     */
    @Test
    fun testResetStack() {
        assertTrue(memoryStack.isEmpty)
        for (j in 0..99) {
            memoryStack.pushValue(111)
            assertTrue(!memoryStack.isEmpty)
        }
        assertTrue(!memoryStack.isEmpty)
        memoryStack.reset()
        assertTrue(memoryStack.isEmpty)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.MemoryStore.stackDump].
     */
    @Test
    fun testStackDump() {
        val values = intArrayOf(0, 10, 22, 34, 45, 57)
        assertTrue(memoryStack.isEmpty)
        for (value in values) {
            memoryStack.pushValue(value)
        }
        val stackDump = memoryStack.stackDump()
        assertNotNull(stackDump)
        assertEquals(values.size, stackDump.size)
        assertEquals(values[0], stackDump[0])
        assertEquals(values[1], stackDump[1])
        assertEquals(values[2], stackDump[2])
        assertEquals(values[3], stackDump[3])
        assertEquals(values[4], stackDump[4])
        assertEquals(values[5], stackDump[5])
    }
}