package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.parser.Parser
import com.slickpath.mobile.android.simple.vm.util.Command
import com.slickpath.mobile.android.simple.vm.util.CommandList

interface VirtualMachineInterface {
    fun addCommand(command: Command)
    suspend fun addCommands(commands: CommandList?)
    suspend fun addCommands(parser: Parser): AddInstructionsResult
    fun runNextInstruction(): VirtualMachine.Results
    fun runInstructions()
    fun runInstructions(numInstructionsToRun: Int)
}