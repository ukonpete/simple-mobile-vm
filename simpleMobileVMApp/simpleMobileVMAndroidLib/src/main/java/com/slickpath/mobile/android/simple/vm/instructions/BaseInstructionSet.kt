package com.slickpath.mobile.android.simple.vm.instructions

import java.util.*

/**
 * Defines two lookup tables (maps) that hold the complete instruction set
 *
 *
 * INSTRUCTION_SET_HT -  Lookup String (name) to get value
 *
 *
 * INSTRUCTION_SET_CONV_HT  - Lookup value to get String
 *
 * @author Pete Procopio
 */
object BaseInstructionSet : Instructions {
    private val INSTRUCTION_SET_HT_INTERNAL: MutableMap<String, Int> = Hashtable(Instructions.NUM_COMMANDS)
    private val INSTRUCTION_SET_CONV_HT_INTERNAL: MutableMap<Int, String> = Hashtable(Instructions.NUM_COMMANDS)

    /**
     * Lookup table for Command ID (Integer) by Command Name (String)
     */
    @JvmField
    val INSTRUCTION_SET_HT: MutableMap<String, Int> = Collections.unmodifiableMap(INSTRUCTION_SET_HT_INTERNAL)

    /**
     * Lookup table for Command Name (String) by Command ID (Integer)
     */
    @JvmField
    val INSTRUCTION_SET_CONV_HT: MutableMap<Int, String> = Collections.unmodifiableMap(INSTRUCTION_SET_CONV_HT_INTERNAL)

    init {
        //
        INSTRUCTION_SET_HT_INTERNAL[Instructions._ADD_STR] = Instructions._ADD
        INSTRUCTION_SET_HT_INTERNAL[Instructions._SUB_STR] = Instructions._SUB
        INSTRUCTION_SET_HT_INTERNAL[Instructions._MUL_STR] = Instructions._MUL
        INSTRUCTION_SET_HT_INTERNAL[Instructions._DIV_STR] = Instructions._DIV
        INSTRUCTION_SET_HT_INTERNAL[Instructions._NEG_STR] = Instructions._NEG
        //
        INSTRUCTION_SET_HT_INTERNAL[Instructions._EQUAL_STR] = Instructions._EQUAL
        INSTRUCTION_SET_HT_INTERNAL[Instructions._NOTEQL_STR] = Instructions._NOTEQL
        INSTRUCTION_SET_HT_INTERNAL[Instructions._GREATER_STR] = Instructions._GREATER
        INSTRUCTION_SET_HT_INTERNAL[Instructions._LESS_STR] = Instructions._LESS
        INSTRUCTION_SET_HT_INTERNAL[Instructions._GTREQL_STR] = Instructions._GTREQL
        INSTRUCTION_SET_HT_INTERNAL[Instructions._LSSEQL_STR] = Instructions._LSSEQL
        INSTRUCTION_SET_HT_INTERNAL[Instructions._NOT_STR] = Instructions._NOT
        //
        INSTRUCTION_SET_HT_INTERNAL[Instructions._PUSHC_STR] = Instructions._PUSHC
        INSTRUCTION_SET_HT_INTERNAL[Instructions._PUSH_STR] = Instructions._PUSH
        INSTRUCTION_SET_HT_INTERNAL[Instructions._POPC_STR] = Instructions._POPC
        INSTRUCTION_SET_HT_INTERNAL[Instructions._POP_STR] = Instructions._POP
        //
        INSTRUCTION_SET_HT_INTERNAL[Instructions._BRANCH_STR] = Instructions._BRANCH
        INSTRUCTION_SET_HT_INTERNAL[Instructions._JUMP_STR] = Instructions._JUMP
        INSTRUCTION_SET_HT_INTERNAL[Instructions._BREQL_STR] = Instructions._BREQL
        INSTRUCTION_SET_HT_INTERNAL[Instructions._BRLSS_STR] = Instructions._BRLSS
        INSTRUCTION_SET_HT_INTERNAL[Instructions._BRGTR_STR] = Instructions._BRGTR
        //
        INSTRUCTION_SET_HT_INTERNAL[Instructions._RDCHAR_STR] = Instructions._RDCHAR
        INSTRUCTION_SET_HT_INTERNAL[Instructions._RDINT_STR] = Instructions._RDINT
        INSTRUCTION_SET_HT_INTERNAL[Instructions._WRCHAR_STR] = Instructions._WRCHAR
        INSTRUCTION_SET_HT_INTERNAL[Instructions._WRINT_STR] = Instructions._WRINT
        //
        INSTRUCTION_SET_HT_INTERNAL[Instructions._CONTENTS_STR] = Instructions._CONTENTS
        INSTRUCTION_SET_HT_INTERNAL[Instructions._HALT_STR] = Instructions._HALT

        for ((key, value) in INSTRUCTION_SET_HT_INTERNAL) {
            INSTRUCTION_SET_CONV_HT_INTERNAL[value] = key
        }
    }
}