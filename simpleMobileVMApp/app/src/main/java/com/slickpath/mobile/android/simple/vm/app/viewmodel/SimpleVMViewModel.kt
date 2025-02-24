package com.slickpath.mobile.android.simple.vm.app.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.app.SimpleMobileVMAppActivity
import com.slickpath.mobile.android.simple.vm.machine.AddInstructionsResult
import com.slickpath.mobile.android.simple.vm.machine.RunResult
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
import com.slickpath.mobile.android.simple.vm.parser.Parser

class SimpleVMViewModel(private var virtualMachine: VirtualMachine) : ViewModel() {

    val onCompletedAddingInstructions: MutableLiveData<CompletedAddingInstructionsStatus> by lazy {
        MutableLiveData<CompletedAddingInstructionsStatus>()
    }

    val onCompletedRunningInstructionsInstructionsStatus: MutableLiveData<CompletedRunningInstructionsStatus> by lazy {
        MutableLiveData<CompletedRunningInstructionsStatus>()
    }

    suspend fun addCommands(parser: Parser): AddInstructionsResult {
        virtualMachine.reset()
        val result = virtualMachine.addCommands(parser)
        completedAddingInstructions(result)
        return result
    }

    suspend fun runInstructions(): RunResult {
        val result = virtualMachine.runInstructions()
        completedRunningInstructions(result)
        return result
    }

    fun reset(context: Context, outputListener: SimpleMobileVMAppActivity.SimpleVMOutputListener) {
        virtualMachine = VirtualMachine(context, outputListener, null)
    }

    private fun completedAddingInstructions(addInstructionsResult: AddInstructionsResult) {
        onCompletedAddingInstructions.postValue(
            CompletedAddingInstructionsStatus(
                addInstructionsResult.vmError
            )
        )
    }

    private fun completedRunningInstructions(
        runResult: RunResult
    ) {
        onCompletedRunningInstructionsInstructionsStatus.postValue(
            CompletedRunningInstructionsStatus(
                runResult.didHalt,
                runResult.lastLineExecuted,
                runResult.vmError
            )
        )
    }
}

data class CompletedRunningInstructionsStatus(
    val onHalt: Boolean,
    val lastLineExecuted: Int,
    val vmError: VMError?
)

data class CompletedAddingInstructionsStatus(val vmError: VMError?)
