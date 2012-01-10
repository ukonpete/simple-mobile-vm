package com.slickpath.mobile.android.simple.vm;

public interface VMListener {

	abstract public void completedAddingInstructions(VMError vmError);
	
	abstract public void completedRunningInstructions(VMError vmError);
}
