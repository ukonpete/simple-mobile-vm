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
	 */
	@Test
	public void testGetStackPointer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#incStackPtr()}.
	 */
	@Test
	public void testIncStackPtr() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#decStackPtr()}.
	 */
	@Test
	public void testDecStackPtr() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#isStackEmpty()}.
	 */
	@Test
	public void testIsStackEmpty() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#isStackFull()}.
	 */
	@Test
	public void testIsStackFull() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Memory#memoryDump()}.
	 */
	@Test
	public void testMemoryDump() {
		fail("Not yet implemented");
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
