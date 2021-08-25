package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.parser.Parser
import com.slickpath.mobile.android.simple.vm.util.Command
import com.slickpath.mobile.android.simple.vm.util.CommandList

interface VirtualMachineInterface {
    fun addCommand(command: Command)
    fun addCommands(commands: CommandList?)
    fun addCommands(parser: Parser)
    fun runNextInstruction(): VirtualMachine.Results
    fun runInstructions()
    fun runInstructions(numInstructionsToRun: Int)
}