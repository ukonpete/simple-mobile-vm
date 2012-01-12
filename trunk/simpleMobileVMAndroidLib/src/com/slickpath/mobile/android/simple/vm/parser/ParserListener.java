package com.slickpath.mobile.android.simple.vm.parser;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

/**
 * An interface allowing a calling entity of SimpleParser to listen for the completedParse event after SimpleParser.parse() is called
 * 
 * @see com.slickpath.mobile.android.simple.vm.parser.SimpleParser
 * 
 * @see com.slickpath.mobile.android.simple.vm.VMError
 * 
 * @author PJ
 *
 */
public interface ParserListener {

	/**
	 * Called when SimpleParser.parse() is completed so the caller can react to the completion or error state of parse
	 * 
	 * @param vmError - If an error is seen this object contains error information, else returns Null
	 * @param commands - The CommandList of the parsed data
	 */
	abstract public void completedParse(VMError vmError, CommandList commands);
}
