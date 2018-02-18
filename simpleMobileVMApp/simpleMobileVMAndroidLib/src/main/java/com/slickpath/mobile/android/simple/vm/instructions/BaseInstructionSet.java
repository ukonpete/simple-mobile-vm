package com.slickpath.mobile.android.simple.vm.instructions;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

/**
 * Defines two lookup tables (maps) that hold the complete instruction set
 * <p>
 * INSTRUCTION_SET_HT -  Lookup String (name) to get value
 * <p>
 * INSTRUCTION_SET_CONV_HT  - Lookup value to get String
 *
 * @author Pete Procopio
 */
public class BaseInstructionSet implements Instructions {
    private static final Map<String, Integer> INSTRUCTION_SET_HT_INTERNAL = new Hashtable<>(NUM_COMMANDS);

    private static final Map<Integer, String> INSTRUCTION_SET_CONV_HT_INTERNAL = new Hashtable<>(NUM_COMMANDS);

    /**
     * Lookup table for Command ID (Integer) by Command Name (String)
     */
    public static final Map<String, Integer> INSTRUCTION_SET_HT = Collections.unmodifiableMap(INSTRUCTION_SET_HT_INTERNAL);
    /**
     * Lookup table for Command Name (String) by Command ID (Integer)
     */
    public static final Map<Integer, String> INSTRUCTION_SET_CONV_HT =  Collections.unmodifiableMap(INSTRUCTION_SET_CONV_HT_INTERNAL);

    static {
        //
        INSTRUCTION_SET_HT_INTERNAL.put(_ADD_STR, _ADD);
        INSTRUCTION_SET_HT_INTERNAL.put(_SUB_STR, _SUB);
        INSTRUCTION_SET_HT_INTERNAL.put(_MUL_STR, _MUL);
        INSTRUCTION_SET_HT_INTERNAL.put(_DIV_STR, _DIV);
        INSTRUCTION_SET_HT_INTERNAL.put(_NEG_STR, _NEG);
        //
        INSTRUCTION_SET_HT_INTERNAL.put(_EQUAL_STR, _EQUAL);
        INSTRUCTION_SET_HT_INTERNAL.put(_NOTEQL_STR, _NOTEQL);
        INSTRUCTION_SET_HT_INTERNAL.put(_GREATER_STR, _GREATER);
        INSTRUCTION_SET_HT_INTERNAL.put(_LESS_STR, _LESS);
        INSTRUCTION_SET_HT_INTERNAL.put(_GTREQL_STR, _GTREQL);
        INSTRUCTION_SET_HT_INTERNAL.put(_LSSEQL_STR, _LSSEQL);
        INSTRUCTION_SET_HT_INTERNAL.put(_NOT_STR, _NOT);
        //
        INSTRUCTION_SET_HT_INTERNAL.put(_PUSHC_STR, _PUSHC);
        INSTRUCTION_SET_HT_INTERNAL.put(_PUSH_STR, _PUSH);
        INSTRUCTION_SET_HT_INTERNAL.put(_POPC_STR, _POPC);
        INSTRUCTION_SET_HT_INTERNAL.put(_POP_STR, _POP);
        //
        INSTRUCTION_SET_HT_INTERNAL.put(_BRANCH_STR, _BRANCH);
        INSTRUCTION_SET_HT_INTERNAL.put(_JUMP_STR, _JUMP);
        INSTRUCTION_SET_HT_INTERNAL.put(_BREQL_STR, _BREQL);
        INSTRUCTION_SET_HT_INTERNAL.put(_BRLSS_STR, _BRLSS);
        INSTRUCTION_SET_HT_INTERNAL.put(_BRGTR_STR, _BRGTR);
        //
        INSTRUCTION_SET_HT_INTERNAL.put(_RDCHAR_STR, _RDCHAR);
        INSTRUCTION_SET_HT_INTERNAL.put(_RDINT_STR, _RDINT);
        INSTRUCTION_SET_HT_INTERNAL.put(_WRCHAR_STR, _WRCHAR);
        INSTRUCTION_SET_HT_INTERNAL.put(_WRINT_STR, _WRINT);
        //
        INSTRUCTION_SET_HT_INTERNAL.put(_CONTENTS_STR, _CONTENTS);
        INSTRUCTION_SET_HT_INTERNAL.put(_HALT_STR, _HALT);

        for (Map.Entry<String, Integer> entry : INSTRUCTION_SET_HT_INTERNAL.entrySet()) {
            INSTRUCTION_SET_CONV_HT_INTERNAL.put(entry.getValue(), entry.getKey());
        }
    }

}
