/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.machine.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.machine.Memory;
import com.slickpath.mobile.android.simple.vm.util.Command;

/**
 * @author PJ
 *
 */
/**
 * @author PJ
 *
 */
public class MemoryTest extends AndroidTestCase {

	private Memory _memory;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#setUp()
	 */
	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		_memory = new Memory();
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testMemorySetup()
	{
		assertTrue(_memory.isStackEmpty());
		assertEquals(Memory.START_LOC, _memory.getProgramCounter());
		assertEquals(Memory.START_LOC, _memory.getProgramWriterPtr());
		assertTrue(_memory.isStackEmpty());
		assertEquals(Memory.MAX_MEMORY, _memory.memoryDump().size());


		final List<Integer> tempMemory = _memory.memoryDump();
		for(final Integer val : tempMemory)
		{
			assertEquals(Memory.EMPTY_MEMORY_VALUE,val.intValue());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#push_mem(int);}.
	 */
	@Test
	public void testPush_mem() {
		final int[] values = {0,10,22,34,45,57};
		assertTrue(_memory.isStackEmpty());

		for (final int value : values) {
			_memory.push_mem(value);
			assertTrue(!_memory.isStackEmpty());
		}

		for(int j = 0; j < 100; j++)
		{
			_memory.push_mem(111);;
		}
		for(int j = 0; j < 100; j++)
		{
			_memory.pop_mem();;
		}
		for (int val = values.length - 1; val >= 0; val--) {
			assertTrue(!_memory.isStackEmpty());
			assertEquals(values[val], _memory.pop_mem().intValue());
		}
		assertTrue(_memory.isStackEmpty());
	}


	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#testPop_mem;}.
	 */
	@Test
	public void testPop_mem() {
		final int[] values = {0,10,22,34,45,57};
		assertTrue(_memory.isStackEmpty());

		for (final int value : values) {
			_memory.push_mem(value);
		}

		for(int j = 0; j < 100; j++)
		{
			_memory.push_mem(111);;
		}

		for(int j = 0; j < 50; j++)
		{
			_memory.pop_mem();;
		}

		for(int j = 0; j < 50; j++)
		{
			_memory.pop_mem();;
		}

		for (int val = values.length - 1; val >= 0; val--) {
			_memory.push_mem(values[val]);
			assertEquals(values[val], _memory.pop_mem().intValue());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#isStackEmpty()}.
	 */
	@Test
	public void testIsStackEmpty() {
		assertTrue(_memory.isStackEmpty());
		for(int j = 0; j < 100; j++)
		{
			_memory.push_mem(111);;
			assertTrue(!_memory.isStackEmpty());
		}
		for(int j = 0; j < 50; j++)
		{
			_memory.pop_mem();;
			assertTrue(!_memory.isStackEmpty());
		}
		for(int j = 0; j < 49; j++)
		{
			_memory.pop_mem();;
			assertTrue(!_memory.isStackEmpty());
		}
		_memory.pop_mem();;
		assertTrue(_memory.isStackEmpty());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#memoryDump()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#get(int)}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#set(int)}.
	 */
	@Test
	public void testProgramMemoryDump() {
		List<Integer> memDump = _memory.memoryDump();
		assertNotNull(memDump);
		assertEquals(Memory.MAX_MEMORY, memDump.size());
		assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump.get(0).intValue());
		assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump.get(5).intValue());
		assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump.get(124).intValue());
		assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump.get(Memory.MAX_MEMORY-1).intValue());

		final int[] values = {0,10,22,34,45,57};
		assertTrue(_memory.isStackEmpty());

		for (final int value : values) {
			_memory.set(value, value);
		}
		memDump = _memory.memoryDump();
		assertNotNull(memDump);

		assertEquals(Memory.MAX_MEMORY, memDump.size());

		for (final int value : values) {
			// check if our data matched memory dump
			assertEquals(value, memDump.get(value).intValue());
			// check if data in memory matches memory dump
			assertEquals(_memory.get(value), memDump.get(value).intValue());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#getCommand()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#setCommand()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#programMemoryDump()}.
	 */
	@Test
	public void testGetCommand()
	{
		final int [] instruction = new int[]{11,22,35,46,88,99};
		final Integer [] parameters = new Integer[]{15,27,null,64,60,101};
		final int [] location = new int[]{0,1,2,64,101,499};

		for(int i = 0; i < instruction.length; i ++)
		{
			final List<Integer> params = new ArrayList<Integer>();
			params.add(parameters[i]);
			final Command command = new Command(instruction[i], params);
			_memory.setCommand(location[i], command);
		}

		final List<Command> instructionDump = _memory.programMemoryDump();
		for(int i = 0; i < instruction.length; i ++)
		{
			final Command command = _memory.getCommand(location[i]);
			assertEquals(instruction[i], command.getCommandId().intValue());
			assertEquals(instruction[i], instructionDump.get(location[i]).getCommandId().intValue());
			assertEquals(parameters[i], command.getParameters().get(0));
			if ( parameters[i] == null )
			{
				assertNull( instructionDump.get(location[i]).getParameters().get(0));
			}
			else
			{
				assertEquals(Integer.valueOf(parameters[i]), instructionDump.get(location[i]).getParameters().get(0));
			}
		}

	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#resetStack()}.
	 */
	@Test
	public void testResetStack() {
		assertTrue(_memory.isStackEmpty());
		for(int j = 0; j < 100; j++)
		{
			_memory.push_mem(111);;
			assertTrue(!_memory.isStackEmpty());
		}
		assertTrue(!_memory.isStackEmpty());
		_memory.resetStack();
		assertTrue(_memory.isStackEmpty());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#stackDump()}.
	 */
	@Test
	public void testStackDump() {
		final int[] values = {0,10,22,34,45,57};
		assertTrue(_memory.isStackEmpty());

		for (final int value : values) {
			_memory.push_mem(value);
		}
		final List<Integer> stackDump = _memory.stackDump();
		assertNotNull(stackDump);
		assertEquals(values.length, stackDump.size());
		assertEquals(values[0], stackDump.get(0).intValue());
		assertEquals(values[1], stackDump.get(1).intValue());
		assertEquals(values[2], stackDump.get(2).intValue());
		assertEquals(values[3], stackDump.get(3).intValue());
		assertEquals(values[4], stackDump.get(4).intValue());
		assertEquals(values[5], stackDump.get(5).intValue());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#getProgramCounter()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#incProgramCounter()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#decProgramCounter()}.
	 */
	@Test
	public void testGetProgramCounter() {

		assertEquals(Memory.START_LOC, _memory.getProgramCounter());
		for(int i = 0; i < 100; i++)
		{
			_memory.incProgramCounter();
		}
		assertEquals(100, _memory.getProgramCounter());
		for(int i = 0; i < 50; i++)
		{
			_memory.decProgramCounter();
		}
		assertEquals(50, _memory.getProgramCounter());
		for(int i = 0; i < 50; i++)
		{
			_memory.decProgramCounter();
		}
		assertEquals(Memory.START_LOC, _memory.getProgramCounter());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#setProgramCounter(int)}.
	 */
	@Test
	public void testSetProgramCounter() {
		assertEquals(Memory.START_LOC, _memory.getProgramCounter());
		_memory.setProgramCounter(100);
		assertEquals(100, _memory.getProgramCounter());
		_memory.setProgramCounter(10);
		assertEquals(10, _memory.getProgramCounter());
		_memory.setProgramCounter(249);
		assertEquals(249, _memory.getProgramCounter());
		_memory.setProgramCounter(Memory.START_LOC);
		assertEquals(Memory.START_LOC, _memory.getProgramCounter());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#resetProgramCounter()}.
	 */
	@Test
	public void testResetProgramCounter() {
		assertEquals(Memory.START_LOC, _memory.getProgramCounter());
		_memory.setProgramCounter(100);
		assertEquals(100, _memory.getProgramCounter());
		_memory.resetProgramCounter();
		assertEquals(Memory.START_LOC, _memory.getProgramCounter());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#getProgramWriterPtr()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#incProgramWriter()}.
	 */
	@Test
	public void testGetProgramWriterPtr() {
		assertEquals(Memory.START_LOC, _memory.getProgramWriterPtr());
		for(int i = 0; i < 100; i++)
		{
			_memory.incProgramWriter();
		}
		assertEquals(100, _memory.getProgramWriterPtr());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#resetProgramWriter()}.
	 */
	@Test
	public void testResetProgramWriter() {
		assertEquals(Memory.START_LOC, _memory.getProgramWriterPtr());
		for(int i = 0; i < 100; i++)
		{
			_memory.incProgramWriter();
		}
		assertEquals(100, _memory.getProgramWriterPtr());
		_memory.resetProgramWriter();
		assertEquals(Memory.START_LOC, _memory.getProgramWriterPtr());
	}


}
