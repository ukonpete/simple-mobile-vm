package com.slickpath.mobile.android.simple.vm.instructions

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class BaseInstructionSetTest {
    /**
     * Make sure the internal instruction sets are correct.
     */
    @Test
    fun testInternals() {
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_HT.size, Instructions.NUM_COMMANDS)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.size, Instructions.NUM_COMMANDS)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_HT[Instructions.ADD_STR]!!.toInt(), Instructions.ADD)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_HT[Instructions.HALT_STR]!!.toInt(), Instructions.HALT)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV_HT[Instructions.DIV], Instructions.DIV_STR)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV_HT[Instructions.BREQL], Instructions.BREQL_STR)
        val keys: Set<String> = BaseInstructionSet.INSTRUCTION_SET_HT.keys
        for (key in keys) {
            val `val` = BaseInstructionSet.INSTRUCTION_SET_HT[key]!!
            val value = BaseInstructionSet.INSTRUCTION_SET_CONV_HT[`val`]
            assertEquals(key, value)
        }
    }
}