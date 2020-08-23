package com.slickpath.mobile.android.simple.vm.instructions;


import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.Set;

import static junit.framework.Assert.assertEquals;

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4.class)
public class BaseInstructionSetTest {

    /**
     * Make sure the internal instruction sets are correct.
     */
    @Test
    public void testInternals() {
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_HT.size(), Instructions.NUM_COMMANDS);
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.size(), Instructions.NUM_COMMANDS);

        assertEquals(BaseInstructionSet.INSTRUCTION_SET_HT.get(Instructions._ADD_STR).intValue(), Instructions._ADD);
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_HT.get(Instructions._HALT_STR).intValue(), Instructions._HALT);

        assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(Instructions._DIV), Instructions._DIV_STR);
        assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(Instructions._BREQL), Instructions._BREQL_STR);


        final Set<String> keys = BaseInstructionSet.INSTRUCTION_SET_HT.keySet();

        for (final String key : keys) {
            final int val = BaseInstructionSet.INSTRUCTION_SET_HT.get(key);

            final String value = BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(val);

            assertEquals(key, value);
        }
    }

}
