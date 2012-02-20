/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.machine.test;


import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.machine.Memory;

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
		assertEquals(Memory.STACK_EMPTY_LOC, _memory.getStackPointer());
		assertEquals(Memory.STACK_LIMIT, _memory.getProgramCounter());
		assertEquals(Memory.STACK_LIMIT, _memory.getProgramWriterPtr());
		assertEquals(true, _memory.isStackEmpty());
		assertEquals(false, _memory.isStackFull());
		assertEquals(Memory.MAX_MEMORY, _memory.memoryDump().size());


		final List<Integer> tempMemory = _memory.memoryDump();
		for(final Integer val : tempMemory)
		{
			assertEquals(Memory.EMPTY_MEMORY_VALUE,val.intValue());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#getStackPointer()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#incStackPtr()}.
	 */
	@Test
	public void testGetStackPointer() {
		final int[] values = {0,10,22,34,45,57};
		try {
			assertEquals(Memory.STACK_EMPTY_LOC, _memory.getStackPointer());

			for (int val = 0; val < values.length; val++) {
				_memory.incStackPtr();
				_memory.set(_memory.getStackPointer(), values[val]);
				assertEquals(val, _memory.getStackPointer());
				assertEquals(values[val], _memory.get(val));
				assertEquals(Memory.EMPTY_MEMORY_VALUE, _memory.get(val+1));
			}

			for(int j = 0; j < 100; j++)
			{
				_memory.incStackPtr();
			}
			assertEquals(Memory.EMPTY_MEMORY_VALUE, _memory.get(_memory.getStackPointer()));
			assertEquals((values.length + 100) - 1, _memory.getStackPointer());

		} catch (final VMError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#decStackPtr()}.
	 */
	@Test
	public void testDecStackPtr() {
		final int[] values = {0,10,22,34,45,57};
		try {
			assertEquals(Memory.STACK_EMPTY_LOC, _memory.getStackPointer());

			for (final int value : values) {
				_memory.incStackPtr();
				_memory.set(_memory.getStackPointer(), value);
			}

			for(int j = 0; j < 100; j++)
			{
				_memory.incStackPtr();
			}

			for(int j = 0; j < 50; j++)
			{
				_memory.decStackPtr();
			}
			assertEquals(Memory.EMPTY_MEMORY_VALUE, _memory.get(_memory.getStackPointer()));
			assertEquals(((values.length + 100) - 50) - 1, _memory.getStackPointer());

			for(int j = 0; j < 50; j++)
			{
				_memory.decStackPtr();
			}

			for (int val = values.length - 1; val >= 0; val--) {
				_memory.set(_memory.getStackPointer(), values[val]);
				assertEquals(val, _memory.getStackPointer());
				assertEquals(values[val], _memory.get(val));
				_memory.decStackPtr();
			}
		} catch (final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#isStackEmpty()}.
	 */
	@Test
	public void testIsStackEmpty() {
		try {
			assertTrue(_memory.isStackEmpty());
			for(int j = 0; j < 100; j++)
			{
				_memory.incStackPtr();
				assertTrue(!_memory.isStackEmpty());
			}
			for(int j = 0; j < 50; j++)
			{
				_memory.decStackPtr();
				assertTrue(!_memory.isStackEmpty());
			}
			for(int j = 0; j < 49; j++)
			{
				_memory.decStackPtr();
				assertTrue(!_memory.isStackEmpty());
			}
			_memory.decStackPtr();
			assertTrue(_memory.isStackEmpty());
		} catch (final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#isStackFull()}.
	 */
	@Test
	public void testIsStackFull() {
		try {
			assertTrue(!_memory.isStackFull());
			for(int j = 0; j < 100; j++)
			{
				_memory.incStackPtr();
				assertTrue("Stack should not be full : " + _memory.getStackPointer(), !_memory.isStackFull());
			}
			for(int j = 0; j < (Memory.STACK_LIMIT - 100); j++)
			{
				_memory.incStackPtr();
				assertTrue("Memory should not be full yet : " + _memory.getStackPointer(), !_memory.isStackFull());
			}
			_memory.incStackPtr();
			assertTrue(_memory.isStackFull());
			assertEquals(Memory.STACK_LIMIT,_memory.getStackPointer());

			for(int j = Memory.STACK_LIMIT; j >= 0; j--)
			{
				_memory.decStackPtr();
				assertTrue(!_memory.isStackFull());
			}
			assertEquals(Memory.STACK_EMPTY_LOC,_memory.getStackPointer());
		} catch (final VMError e) {
			fail("VMError (" + _memory.getStackPointer() + ") " + e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#memoryDump()}.
	 */
	@Test
	public void testMemoryDump() {
		List<Integer> memDump = _memory.memoryDump();
		assertNotNull(memDump);
		assertEquals(Memory.MAX_MEMORY, memDump.size());
		assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump.get(0).intValue());
		assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump.get(5).intValue());
		assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump.get(124).intValue());
		assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump.get(Memory.MAX_MEMORY-1).intValue());

		final int[] values = {0,10,22,34,45,57};
		try {
			assertEquals(Memory.STACK_EMPTY_LOC, _memory.getStackPointer());

			for (final int value : values) {
				_memory.incStackPtr();
				_memory.set(_memory.getStackPointer(), value);
			}

			memDump = _memory.memoryDump();
			assertNotNull(memDump);
			assertEquals(Memory.MAX_MEMORY, memDump.size());
			assertEquals(values[0], memDump.get(0).intValue());
			assertEquals(values[1], memDump.get(1).intValue());
			assertEquals(values[2], memDump.get(2).intValue());
			assertEquals(values[3], memDump.get(3).intValue());
			assertEquals(values[4], memDump.get(4).intValue());
			assertEquals(values[5], memDump.get(5).intValue());
			assertEquals(Memory.EMPTY_MEMORY_VALUE, memDump.get(Memory.MAX_MEMORY-1).intValue());
		} catch (final VMError e) {
			fail("VMError (" + _memory.getStackPointer() + ") " + e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#setProgramCounter(int)}.
	 */
	@Test
	public void testSetProgramCounter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#get(int)}.
	 */
	@Test
	public void testGet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#set(int, int)}.
	 */
	@Test
	public void testSet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#resetStackPointer()}.
	 */
	@Test
	public void testResetStackPointer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#getProgramCounter()}.
	 */
	@Test
	public void testGetProgramCounter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#incProgramCounter()}.
	 */
	@Test
	public void testIncProgramCounter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#decProgramCounter()}.
	 */
	@Test
	public void testDecProgramCounter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#resetProgramCounter()}.
	 */
	@Test
	public void testResetProgramCounter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#getProgramWriterPtr()}.
	 */
	@Test
	public void testGetProgramWriterPtr() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#incProgramWriter()}.
	 */
	@Test
	public void testIncProgramWriter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#resetProgramWriter()}.
	 */
	@Test
	public void testResetProgramWriter() {
		fail("Not yet implemented");
	}

}
