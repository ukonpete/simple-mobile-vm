package com.slickpath.mobile.android.simple.vm.instructions

import java.util.Collections
import java.util.Hashtable

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
    private val INSTRUCTION_SET_INTERNAL: MutableMap<String, Int> =
        Hashtable(Instructions.NUM_COMMANDS)
    private val INSTRUCTION_SET_CONV_INTERNAL: MutableMap<Int, String> =
        Hashtable(Instructions.NUM_COMMANDS)

    /**
     * Lookup table for Command ID (Integer) by Command Name (String)
     */
    @JvmField
    val INSTRUCTION_SET: MutableMap<String, Int> =
        Collections.unmodifiableMap(INSTRUCTION_SET_INTERNAL)

    /**
     * Lookup table for Command Name (String) by Command ID (Integer)
     */
    @JvmField
    val INSTRUCTION_SET_CONV: MutableMap<Int, String> =
        Collections.unmodifiableMap(INSTRUCTION_SET_CONV_INTERNAL)

    init {
        //
        INSTRUCTION_SET_INTERNAL[Instructions.ADD_STR] = Instructions.ADD
        INSTRUCTION_SET_INTERNAL[Instructions.SUB_STR] = Instructions.SUB
        INSTRUCTION_SET_INTERNAL[Instructions.MUL_STR] = Instructions.MUL
        INSTRUCTION_SET_INTERNAL[Instructions.DIV_STR] = Instructions.DIV
        INSTRUCTION_SET_INTERNAL[Instructions.NEG_STR] = Instructions.NEG
        //
        INSTRUCTION_SET_INTERNAL[Instructions.EQUAL_STR] = Instructions.EQUAL
        INSTRUCTION_SET_INTERNAL[Instructions.NOTEQL_STR] = Instructions.NOTEQL
        INSTRUCTION_SET_INTERNAL[Instructions.GREATER_STR] = Instructions.GREATER
        INSTRUCTION_SET_INTERNAL[Instructions.LESS_STR] = Instructions.LESS
        INSTRUCTION_SET_INTERNAL[Instructions.GTREQL_STR] = Instructions.GTREQL
        INSTRUCTION_SET_INTERNAL[Instructions.LSSEQL_STR] = Instructions.LSSEQL
        INSTRUCTION_SET_INTERNAL[Instructions.NOT_STR] = Instructions.NOT
        //
        INSTRUCTION_SET_INTERNAL[Instructions.PUSHC_STR] = Instructions.PUSHC
        INSTRUCTION_SET_INTERNAL[Instructions.PUSH_STR] = Instructions.PUSH
        INSTRUCTION_SET_INTERNAL[Instructions.POPC_STR] = Instructions.POPC
        INSTRUCTION_SET_INTERNAL[Instructions.POP_STR] = Instructions.POP
        //
        INSTRUCTION_SET_INTERNAL[Instructions.BRANCH_STR] = Instructions.BRANCH
        INSTRUCTION_SET_INTERNAL[Instructions.JUMP_STR] = Instructions.JUMP
        INSTRUCTION_SET_INTERNAL[Instructions.BREQL_STR] = Instructions.BREQL
        INSTRUCTION_SET_INTERNAL[Instructions.BRLSS_STR] = Instructions.BRLSS
        INSTRUCTION_SET_INTERNAL[Instructions.BRGTR_STR] = Instructions.BRGTR
        //
        INSTRUCTION_SET_INTERNAL[Instructions.RDCHAR_STR] = Instructions.RDCHAR
        INSTRUCTION_SET_INTERNAL[Instructions.RDINT_STR] = Instructions.RDINT
        INSTRUCTION_SET_INTERNAL[Instructions.WRCHAR_STR] = Instructions.WRCHAR
        INSTRUCTION_SET_INTERNAL[Instructions.WRINT_STR] = Instructions.WRINT
        //
        INSTRUCTION_SET_INTERNAL[Instructions.CONTENTS_STR] = Instructions.CONTENTS
        INSTRUCTION_SET_INTERNAL[Instructions.HALT_STR] = Instructions.HALT

        for ((key, value) in INSTRUCTION_SET_INTERNAL) {
            INSTRUCTION_SET_CONV_INTERNAL[value] = key
        }
    }
}