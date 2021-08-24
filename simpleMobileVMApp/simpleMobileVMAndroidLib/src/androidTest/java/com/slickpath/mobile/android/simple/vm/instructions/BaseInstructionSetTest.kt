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
        assertEquals(BaseInstructionSet.INSTRUCTION_SET.size, Instructions.NUM_COMMANDS)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV.size, Instructions.NUM_COMMANDS)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET[Instructions.ADD_STR], Instructions.ADD)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET[Instructions.HALT_STR], Instructions.HALT)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV[Instructions.DIV], Instructions.DIV_STR)
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV[Instructions.BREQL], Instructions.BREQL_STR)
        val keys: Set<String> = BaseInstructionSet.INSTRUCTION_SET.keys
        for (key in keys) {
            val instructionVal = BaseInstructionSet.INSTRUCTION_SET[key]
            val instructionValConverted = BaseInstructionSet.INSTRUCTION_SET_CONV[instructionVal]
            assertEquals(key, instructionValConverted)
        }
    }
}