package com.slickpath.mobile.android.simple.vm;

import java.util.List;

public interface VMListener {

	abstract public void completedParse(VMError vmError, List<Integer> allInstructions, List<List<Integer>> allParameters);
	
	abstract public void completedAddingInstructions(VMError vmError);
	
	abstract public void completedRunningInstructions(VMError vmError);
}
