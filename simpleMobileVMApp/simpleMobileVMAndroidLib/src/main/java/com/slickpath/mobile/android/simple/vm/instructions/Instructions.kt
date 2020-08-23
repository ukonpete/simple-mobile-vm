package com.slickpath.mobile.android.simple.vm.instructions

/**
 * Contains the string and integer definitions for all the instructions
 *
 *
 * If command value is
 * 0 - 999 			: No Parameters
 * 1000 - 1999 		: 1 Parameters
 * All other values 	: Undefined
 *
 * @author Pete Procopio
 */
interface Instructions {
    companion object {
        const val _BEGIN = -1

        // All commands in the 0-999 range are 0 parameter commands
        // ARITHMETIC
        const val _ADD = 0
        const val _SUB = 1
        const val _MUL = 2
        const val _DIV = 3
        const val _NEG = 4

        // LOGIC
        const val _EQUAL = 5
        const val _NOTEQL = 6
        const val _GREATER = 7
        const val _LESS = 8
        const val _GTREQL = 9
        const val _LSSEQL = 10
        const val _NOT = 11

        // All commands in the 1000-1999 range are 1 parameter commands
        // STACK
        const val _PUSHC = 1000
        const val _PUSH = 1001
        const val _POPC = 1002
        const val _POP = 15

        // PROGRAM FLOW
        const val _BRANCH = 1003
        const val _JUMP = 17
        const val _BREQL = 1004
        const val _BRLSS = 1005
        const val _BRGTR = 1006

        // I/O
        const val _RDCHAR = 21
        const val _RDINT = 22
        const val _WRCHAR = 23
        const val _WRINT = 24

        // MISC
        const val _CONTENTS = 25
        const val _HALT = 26
        const val NUM_COMMANDS = _HALT + 1
        const val _ADD_STR = "ADD"
        const val _SUB_STR = "SUB"
        const val _MUL_STR = "MUL"
        const val _DIV_STR = "DIV"
        const val _NEG_STR = "NEG"

        // LOGIC
        const val _EQUAL_STR = "EQUAL"
        const val _NOTEQL_STR = "NOTEQL"
        const val _GREATER_STR = "GREATER"
        const val _LESS_STR = "LESS"
        const val _GTREQL_STR = "GTREQL"
        const val _LSSEQL_STR = "LSSEQL"
        const val _NOT_STR = "NOT"

        // STACK
        const val _PUSHC_STR = "PUSHC"
        const val _PUSH_STR = "PUSH"
        const val _POPC_STR = "POPC"
        const val _POP_STR = "POP"

        // PROGRAM FLOW
        const val _BRANCH_STR = "BRANCH"
        const val _JUMP_STR = "JUMP"
        const val _BREQL_STR = "BREQL"
        const val _BRLSS_STR = "BRLSS"
        const val _BRGTR_STR = "BRGTR"

        // I/O
        const val _RDCHAR_STR = "RDCHAR"
        const val _RDINT_STR = "RDINT"
        const val _WRCHAR_STR = "WRCHAR"
        const val _WRINT_STR = "WRINT"

        // MISC
        const val _CONTENTS_STR = "CONTENTS"
        const val _HALT_STR = "HALT"
    }
}