/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.machine;


import java.util.List;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.machine.SimpleStack;

/**
 * @author Pete Procopio
 *
 */
public class SimpleStackTest extends AndroidTestCase {

	private SimpleStack _stack = null;

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_stack = new SimpleStack();
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.SimpleStack#isEmpty()}.
	 */
	public void testIsEmpty() {
		assertTrue(_stack.isEmpty());
		_stack.push(1);
		_stack.push(2);
		assertTrue(!_stack.isEmpty());
		_stack.pop();
		assertTrue(!_stack.isEmpty());
		_stack.reset();
		assertTrue(_stack.isEmpty());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.SimpleStack#peek()}.
	 */
	public void testPeek() {
		assertTrue(_stack.isEmpty());
		_stack.push(1);
		assertEquals(1, _stack.peek().intValue());
		_stack.push(2);
		assertEquals(2, _stack.peek().intValue());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.SimpleStack#pop()}.
	 */
	public void testPop() {
		final int[] values = {0,10,22,34,45,57};
		assertTrue(_stack.isEmpty());

		for (final int value : values) {
			_stack.push(value);
		}

		for(int j = 0; j < 100; j++)
		{
			_stack.push(111);;
		}

		for(int j = 0; j < 50; j++)
		{
			_stack.pop();;
		}

		for(int j = 0; j < 50; j++)
		{
			_stack.pop();
		}

		for (int val = values.length - 1; val >= 0; val--) {
			_stack.push(values[val]);
			assertEquals(values[val], _stack.pop().intValue());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.SimpleStack#push(java.lang.Integer)}.
	 */
	public void testPush() {
		final int[] values = {0,10,22,34,45,57};
		assertTrue(_stack.isEmpty());

		for (final int value : values) {
			_stack.push(value);
			assertTrue(!_stack.isEmpty());
		}

		for(int j = 0; j < 100; j++)
		{
			_stack.push(111);;
		}
		for(int j = 0; j < 100; j++)
		{
			_stack.pop();;
		}
		for (int val = values.length - 1; val >= 0; val--) {
			assertTrue(!_stack.isEmpty());
			assertEquals(values[val], _stack.pop().intValue());
		}
		assertTrue(_stack.isEmpty());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.SimpleStack#size()}.
	 */
	public void testSize() {
		assertTrue(_stack.isEmpty());
		_stack.push(5);
		_stack.push(12);
		assertEquals(2, _stack.size());
		_stack.pop();
		assertEquals(1, _stack.size());
		_stack.reset();
		assertEquals(0, _stack.size());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.SimpleStack#reset()}.
	 */
	public void testReset() {
		assertTrue(_stack.isEmpty());
		for(int j = 0; j < 100; j++)
		{
			_stack.push(111);;
			assertTrue(!_stack.isEmpty());
		}
		assertTrue(!_stack.isEmpty());
		_stack.reset();
		assertTrue(_stack.isEmpty());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.SimpleStack#dump()}.
	 */
	public void testDump() {
		final int[] values = {0,10,22,34,45,57};
		assertTrue(_stack.isEmpty());

		for (final int value : values) {
			_stack.push(value);
		}
		final List<Integer> stackDump = _stack.dump();
		assertNotNull(stackDump);
		assertEquals(values.length, stackDump.size());
		assertEquals(values[0], stackDump.get(0).intValue());
		assertEquals(values[1], stackDump.get(1).intValue());
		assertEquals(values[2], stackDump.get(2).intValue());
		assertEquals(values[3], stackDump.get(3).intValue());
		assertEquals(values[4], stackDump.get(4).intValue());
		assertEquals(values[5], stackDump.get(5).intValue());
	}

}
