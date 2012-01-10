package com.slickpath.mobile.android.simple.vm.parser;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.util.CommandSet;

public interface ParserListener {

	abstract public void completedParse(VMError vmError, CommandSet commandSet);
}
