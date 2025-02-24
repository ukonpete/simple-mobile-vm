package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.util.Command

interface IKernel {
    fun addCommand(command: Command)
    fun resetProgramWriter()
    fun resetStack()
    fun getValueAt(location: Int): Int
    fun incrementProgramCounter()
    fun decrementProgramCounter()
    fun resetProgramCounter()
    fun getCommandAt(location: Int): Command
    fun dumpStack(): List<Int>
    fun dumpMemory(): List<Int>
    fun reset()

    val programCounter: Int
}