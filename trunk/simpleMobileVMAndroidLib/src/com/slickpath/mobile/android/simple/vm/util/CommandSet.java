package com.slickpath.mobile.android.simple.vm.util;

import java.util.ArrayList;
import java.util.List;

public class CommandSet {

	private final List<Integer> _allCommandIds;
	private final List<List<Integer>> _allParameters;


	public CommandSet()
	{
		_allCommandIds = new ArrayList<Integer>();
		_allParameters = new ArrayList<List<Integer>>();
	}

	public void addCommand(final Integer commandId , final List<Integer> parameters)
	{
		_allCommandIds.add(commandId);
		_allParameters.add(parameters);
	}

	public void addCommand(final Command command)
	{
		_allCommandIds.add(command.getCommandId());
		_allParameters.add(command.getParameters());
	}

	public Command getCommand(final int location)
	{
		return new Command(_allCommandIds.get(location), _allParameters.get(location));
	}

	/**
	 * @return the _allCommands
	 */
	public List<Integer> getAllCommandIds() {
		return _allCommandIds;
	}

	/**
	 * @return the _allParameters
	 */
	public List<List<Integer>> getAllParameters() {
		return _allParameters;
	}

	public int getNumCommands()
	{
		return _allCommandIds.size();
	}

}
