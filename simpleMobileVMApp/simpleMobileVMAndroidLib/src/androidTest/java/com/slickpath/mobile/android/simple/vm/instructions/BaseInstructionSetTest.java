package com.slickpath.mobile.android.simple.vm.instructions;


import android.test.AndroidTestCase;

import java.util.Set;

/**
 * @author Pete Procopio
 */
public class BaseInstructionSetTest extends AndroidTestCase {

    /**
     * Make sure the internal instruction sets are correct.
     */
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

            final String sKey = BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(val);

            assertEquals(sKey, key);
        }
    }

}
