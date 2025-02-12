package com.slickpath.mobile.android.simple.vm.machine

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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

}