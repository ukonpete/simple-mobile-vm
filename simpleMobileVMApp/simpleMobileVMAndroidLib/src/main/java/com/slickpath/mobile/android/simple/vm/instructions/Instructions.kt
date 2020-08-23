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
        const val BEGIN = -1

        // All commands in the 0-999 range are 0 parameter commands
        // ARITHMETIC
        const val ADD = 0
        const val SUB = 1
        const val MUL = 2
        const val DIV = 3
        const val NEG = 4

        // LOGIC
        const val EQUAL = 5
        const val NOTEQL = 6
        const val GREATER = 7
        const val LESS = 8
        const val GTREQL = 9
        const val LSSEQL = 10
        const val NOT = 11

        // All commands in the 1000-1999 range are 1 parameter commands
        // STACK
        const val PUSHC = 1000
        const val PUSH = 1001
        const val POPC = 1002
        const val POP = 15

        // PROGRAM FLOW
        const val BRANCH = 1003
        const val JUMP = 17
        const val BREQL = 1004
        const val BRLSS = 1005
        const val BRGTR = 1006

        // I/O
        const val RDCHAR = 21
        const val RDINT = 22
        const val WRCHAR = 23
        const val WRINT = 24

        // MISC
        const val CONTENTS = 25
        const val HALT = 26
        const val NUM_COMMANDS = HALT + 1

        const val ADD_STR = "ADD"
        const val SUB_STR = "SUB"
        const val MUL_STR = "MUL"
        const val DIV_STR = "DIV"
        const val NEG_STR = "NEG"

        // LOGIC
        const val EQUAL_STR = "EQUAL"
        const val NOTEQL_STR = "NOTEQL"
        const val GREATER_STR = "GREATER"
        const val LESS_STR = "LESS"
        const val GTREQL_STR = "GTREQL"
        const val LSSEQL_STR = "LSSEQL"
        const val NOT_STR = "NOT"

        // STACK
        const val PUSHC_STR = "PUSHC"
        const val PUSH_STR = "PUSH"
        const val POPC_STR = "POPC"
        const val POP_STR = "POP"

        // PROGRAM FLOW
        const val BRANCH_STR = "BRANCH"
        const val JUMP_STR = "JUMP"
        const val BREQL_STR = "BREQL"
        const val BRLSS_STR = "BRLSS"
        const val BRGTR_STR = "BRGTR"

        // I/O
        const val RDCHAR_STR = "RDCHAR"
        const val RDINT_STR = "RDINT"
        const val WRCHAR_STR = "WRCHAR"
        const val WRINT_STR = "WRINT"

        // MISC
        const val CONTENTS_STR = "CONTENTS"
        const val HALT_STR = "HALT"
    }
}