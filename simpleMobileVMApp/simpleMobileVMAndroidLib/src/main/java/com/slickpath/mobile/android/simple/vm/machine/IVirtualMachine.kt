package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.parser.Parser
import com.slickpath.mobile.android.simple.vm.util.Command
import com.slickpath.mobile.android.simple.vm.util.CommandList

interface IVirtualMachine {
    fun addCommand(command: Command)
    suspend fun addCommands(commands: CommandList?)
    suspend fun addCommands(parser: Parser): AddInstructionsResult
    fun runNextInstruction(): RunResult
    suspend fun runInstructions(): RunResult
    suspend fun runInstructions(numInstructionsToRun: Int): RunResult
}

/**
 * Data class to hold the results of running an instruction.
 *
 * @property didHalt Indicates if the HALT instruction was executed.
 * @property lastLineExecuted The program counter value after execution.
 * @property vmError Any VMError that occurred during execution.
 */
data class RunResult(
    val didHalt: Boolean,
    val lastLineExecuted: Int,
    val vmError: VMError?
)