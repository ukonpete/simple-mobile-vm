package com.slickpath.mobile.android.simple.vm.machine

object FibonacciInstructions {
    const val instructions = "PUSHC [HALT]\n" +
            "PUSHC 0\n" +
            "WRINT\n" +
            "PUSHC 1\n" +
            "WRINT\n" +
            "PUSHC 1\n" +
            "WRINT\n" +
            "PUSHC 0\n" +
            "POPC 10\n" +
            "PUSHC 1\n" +
            "POPC 11\n" +
            "PUSH 11\n" +
            "POPC 12\n" +
            "PUSHC 12\n" +
            "POPC 13 \n" +
            "[FIB]\n" +
            "PUSH 11\n" +
            "POPC 14\n" +
            "PUSH 14\n" +
            "POPC 10\n" +
            "PUSH 12\n" +
            "POPC 11\n" +
            "PUSH 12\n" +
            "PUSH 10\n" +
            "ADD\n" +
            "POPC 12\n" +
            "PUSH 12\n" +
            "WRINT\n" +
            "PUSHC 1\n" +
            "PUSH 13\n" +
            "SUB\n" +
            "POPC 13\n" +
            "PUSH 13\n" +
            "BREQL [HALT]\n" +
            "BRANCH [FIB]\n" +
            "[HALT]\n" +
            "HALT"
}