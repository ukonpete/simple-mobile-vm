package com.slickpath.mobile.android.simple.vm.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine

class SimpleVMViewModelFactory(private val virtualMachine: VirtualMachine) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SimpleVMViewModel(virtualMachine) as T
    }
}