package com.slickpath.mobile.android.simple.vm.util;

import java.util.List;


public class Command{

	private final Integer _commandId;
	private final List<Integer> _parameters;
	/**
	 * 
	 */
	
	public Command(Integer commandId, List<Integer> params)
	{
		_commandId = commandId;
		_parameters = params;
	}
	
	/**
	 * @return the _command
	 */
	public  Integer getCommandId() {
		return _commandId;
	}

	/**
	 * @return the _parameters
	 */
	public List<Integer> getParameters() {
		return _parameters;
	}

}
