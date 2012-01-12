package com.slickpath.mobile.android.simple.vm;

public interface VMListener {

	abstract public void completedAddingInstructions(VMError vmError);

	abstract public void completedRunningInstructions(int lastLineExecuted, VMError vmError);

	abstract public void completedRunningInstruction(boolean bHalt, int lineExecuted, VMError vmError);
}
