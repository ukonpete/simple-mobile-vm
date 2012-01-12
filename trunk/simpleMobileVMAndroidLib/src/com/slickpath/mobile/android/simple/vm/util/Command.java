package com.slickpath.mobile.android.simple.vm.util;

import java.util.List;


/**
 * The Command object is the pairing of a commandId to a list of parameters
 * Immutable
 * 
 * @author PJ
 *
 */
public class Command{

	private final Integer _commandId;
	private final List<Integer> _parameters;

	/**
	 * Constructor
	 */
	public Command(final Integer commandId, final List<Integer> params)
	{
		_commandId = commandId;
		_parameters = params;
	}

	/**
	 * @return the command Id
	 */
	public  Integer getCommandId() {
		return _commandId;
	}

	/**
	 * @return the parameters
	 */
	public List<Integer> getParameters() {
		return _parameters;
	}

}
