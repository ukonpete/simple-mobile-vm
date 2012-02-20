package com.slickpath.mobile.android.simple.vm.machine;

import java.util.List;
import java.util.Stack;

public class SimpleStack{

	private final Stack<Integer> _stack = new Stack<Integer>();

	public boolean isEmpty()
	{
		return _stack.empty();
	}

	public Integer peek()
	{
		return _stack.peek();
	}

	public Integer pop()
	{
		return _stack.pop();
	}

	public Integer push(final Integer value)
	{
		return _stack.push(value);
	}

	public int size()
	{
		return _stack.size();
	}

	public void reset()
	{
		_stack.clear();
	}

	public List<Integer> dump()
	{
		return _stack.subList(0, _stack.size());
	}
}
