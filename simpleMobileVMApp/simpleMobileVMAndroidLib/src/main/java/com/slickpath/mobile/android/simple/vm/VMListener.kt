package com.slickpath.mobile.android.simple.vm

/**
 * Interface - Callback interface for Listeners to VirtualMachine Instruction Events
 *
 *
 *  * completedAddingInstructions
 *  * completedRunningInstructions
 *
 *
 * @author Pete Procopio
 */
interface VMListener {
    /**
     * Callback - Called when VM is completed adding instructions
     * VMError will be null if no error
     *
     * @param vmError if not null there was an error adding instructions
     * @see com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
     *
     * @see com.slickpath.mobile.android.simple.vm.VMError
     */
    fun completedAddingInstructions(vmError: VMError?)

    /**
     * Callback - Called when VM has completed running instructions .  Will return the line number of the program that was last executed
     * VMError will be null if no error.
     *
     * @param bHalt were the instructions halted
     * @param lastLineExecuted line number of last instruction execuyted
     * @param vmError if not null there was an error running instructions
     * @see com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
     *
     * @see com.slickpath.mobile.android.simple.vm.VMError
     */
    fun completedRunningInstructions(bHalt: Boolean, lastLineExecuted: Int, vmError: VMError?)
}