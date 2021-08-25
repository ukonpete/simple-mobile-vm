package com.slickpath.mobile.android.simple.vm.app.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slickpath.mobile.android.simple.vm.VMListener
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.app.SimpleMobileVMAppActivity
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
import com.slickpath.mobile.android.simple.vm.parser.Parser
import com.slickpath.mobile.android.simple.vm.util.CommandList

class SimpleVMViewModel(private var virtualMachine: VirtualMachine) : ViewModel(), VMListener {


    init {
        virtualMachine.vmListener = this
    }

    val onCompletedAddingInstructions: MutableLiveData<CompletedAddingInstructionsStatus> by lazy {
        MutableLiveData<CompletedAddingInstructionsStatus>()
    }

    val onCompletedRunningInstructionsInstructionsStatus: MutableLiveData<CompletedRunningInstructionsStatus> by lazy {
        MutableLiveData<CompletedRunningInstructionsStatus>()
    }

    fun addCommands(parser: Parser) {
        virtualMachine.reset()
        virtualMachine.addCommands(parser)
    }

    fun runInstructions() {
        virtualMachine.runInstructions()
    }

    fun reset(context: Context, outputListener: SimpleMobileVMAppActivity.SimpleVMOutputListener) {
        virtualMachine = VirtualMachine(context, outputListener, null)
        virtualMachine.vmListener = this
    }

    override fun completedAddingInstructions(vmError: VMError?, instructionsAdded: Int) {
        onCompletedAddingInstructions.postValue(CompletedAddingInstructionsStatus(vmError))
    }

    override fun completedRunningInstructions(
        bHalt: Boolean,
        lastLineExecuted: Int,
        vmError: VMError?
    ) {
        onCompletedRunningInstructionsInstructionsStatus.postValue(
            CompletedRunningInstructionsStatus(bHalt, lastLineExecuted, vmError)
        )
    }
}

data class CompletedRunningInstructionsStatus(
    val onHalt: Boolean,
    val lastLineExecuted: Int,
    val vmError: VMError?
)

data class CompletedAddingInstructionsStatus(val vmError: VMError?)
