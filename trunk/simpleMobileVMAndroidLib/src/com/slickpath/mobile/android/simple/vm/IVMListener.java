package com.slickpath.mobile.android.simple.vm;

/**
 * Interface - Callback interface for Listeners to VirrualMachaince Instruction Events
 * - completedAddingInstructions
 * - completedRunningInstructions
 * 
 * @author Pete Procopio
 *
 */
public interface IVMListener {

	/**
	 * Callback - Called when VM is completed adding instructions
	 * VMError will be null if no error
	 * 
	 * @see com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
	 * @see com.slickpath.mobile.android.simple.vm.VMError
	 * 
	 * @param vmError
	 */
	abstract public void completedAddingInstructions(VMError vmError);

	/**
	 * Callback - Called when VM has completed running instructions .  Will return the line number of the program that was last executed
	 * VMError will be null if no error.
	 * 
	 * @see com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
	 * @see com.slickpath.mobile.android.simple.vm.VMError
	 * 
	 * @param bHalt
	 * @param lastLineExecuted
	 * @param vmError
	 */
	abstract public void completedRunningInstructions(boolean bHalt, int lastLineExecuted, VMError vmError);

}
