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
class CommandTest {

    companion object {
        private const val TEST_PARAM_VAL = 15

        private const val COMMAND_1 = Instructions.PUSHC
        private const val COMMAND_PARAM_VAL_1 = TEST_PARAM_VAL
        private val COMMAND_PARAM_LIST_1: List<Int> = ArrayList(listOf(COMMAND_PARAM_VAL_1))
        private const val COMMAND_2 = Instructions.ADD
        private val COMMAND_PARAM_LIST_2: List<Int>? = null
        private lateinit var _commandWithParam: Command
        private lateinit var _commandWithOutParam: Command
    }

    @Before
    fun before() {
        _commandWithParam = Command(COMMAND_1, COMMAND_PARAM_LIST_1)
        _commandWithOutParam = Command(COMMAND_2, COMMAND_PARAM_LIST_2)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.util.Command.commandId].
     */
    @Test
    fun testGetCommandId() {
        assertEquals(_commandWithParam.commandId, COMMAND_1)
        assertEquals(_commandWithOutParam.commandId, COMMAND_2)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.util.Command.parameters].
     */
    @Test
    fun testGetParameters() {
        assertNotNull(_commandWithParam.parameters)
        assertEquals(_commandWithParam.parameters[0], COMMAND_PARAM_VAL_1)
        assertNotNull(_commandWithOutParam.parameters)
    }

}