package com.slickpath.mobile.android.simple.vm.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.instructions.Instructions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class CommandListTest {

    companion object {
        private const val TEST_PARAM_VAL1 = 15
        private const val TEST_PARAM_VAL2 = 25
        private const val COMMAND_1 = Instructions.PUSHC
        private const val COMMAND_PARAM_VAL_1 = TEST_PARAM_VAL1
        private val COMMAND_PARAM_LIST_1: List<Int> = ArrayList(listOf(COMMAND_PARAM_VAL_1))
        private const val COMMAND_2 = Instructions.PUSH
        private const val COMMAND_PARAM_VAL_2 = TEST_PARAM_VAL2
        private val COMMAND_PARAM_LIST_2: List<Int> = ArrayList(listOf(COMMAND_PARAM_VAL_2))
        private const val COMMAND_3 = Instructions.ADD
        private val COMMAND_PARAM_LIST_3: List<Int>? = null
    }

    private lateinit var testList1: CommandList

    @Before
    fun before() {
        testList1 = CommandList()
        testList1.add(Command(COMMAND_1, COMMAND_PARAM_LIST_1))
        testList1.add(COMMAND_2, COMMAND_PARAM_LIST_2)
        testList1.add(COMMAND_3, COMMAND_PARAM_LIST_3)
    }

    @Test
    fun testCommands() {
        assertNotNull(testList1)
        assertEquals(testList1.size, 3)
        assertNotNull(testList1[0])
        assertEquals(testList1[0].commandId, COMMAND_1)
        assertNotNull(testList1[0].parameters)
        assertEquals(testList1[0].parameters.size, 1)
        assertEquals(testList1[0].parameters[0], COMMAND_PARAM_VAL_1)
        assertNotNull(testList1[1])
        assertEquals(testList1[1].commandId, COMMAND_2)
        assertNotNull(testList1[1].parameters)
        assertEquals(testList1[1].parameters.size, 1)
        assertEquals(testList1[1].parameters[0], COMMAND_PARAM_VAL_2)
        assertNotNull(testList1[2])
        assertEquals(testList1[2].commandId, COMMAND_3)
        assertEquals(0, testList1[2].parameters.size)
    }
}