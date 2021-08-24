package com.slickpath.mobile.android.simple.vm.app.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slickpath.mobile.android.simple.vm.VMListener
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.app.SimpleMobileVMAppActivity
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
import com.slickpath.mobile.android.simple.vm.util.CommandList

class SimpleVMViewModel(private var virtualMachine: VirtualMachine) : ViewModel(), VMListener {


    init {
        virtualMachine.vMListener = this
    }

    val onCompletedAddingInstructions: MutableLiveData<CompletedAddingInstructionsStatus> by lazy {
        MutableLiveData<CompletedAddingInstructionsStatus>()
    }

    val onCompletedRunningInstructionsInstructionsStatus: MutableLiveData<CompletedRunningInstructionsStatus> by lazy {
        MutableLiveData<CompletedRunningInstructionsStatus>()
    }

    fun addCommands(commands: CommandList?) {
        virtualMachine.reset()
        virtualMachine.addCommands(commands)
    }

    fun runInstructions() {
        virtualMachine.runInstructions()
    }

    fun reset(context: Context, outputListener: SimpleMobileVMAppActivity.SimpleVMOutputListener) {
        virtualMachine = VirtualMachine(context, outputListener, null)
        virtualMachine.vMListener = this
    }

    override fun completedAddingInstructions(vmError: VMError?) {
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
