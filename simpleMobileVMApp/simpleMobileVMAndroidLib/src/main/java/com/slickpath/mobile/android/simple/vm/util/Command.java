package com.slickpath.mobile.android.simple.vm.util;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * The Command object is the pairing of a commandId to a list of parameters
 * Immutable
 * 
 * @author Pete Procopio
 *
 */
public class Command{

	private final Integer _commandId;
	@Nullable
    private final List<Integer> _parameters;

	/**
	 * Constructor
	 */
	public Command(final Integer commandId, @Nullable List<Integer> params)
	{
		_commandId = commandId;
		if ( params == null )
		{
			params = new ArrayList<>();
		}
		_parameters = params;
		if ( _parameters.size() == 0 )
		{
			_parameters.add(null);
		}
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
	@Nullable
    public List<Integer> getParameters() {
		return _parameters;
	}

}
