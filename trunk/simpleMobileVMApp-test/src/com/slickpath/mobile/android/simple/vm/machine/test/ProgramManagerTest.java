/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.machine.test;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.machine.Memory;
import com.slickpath.mobile.android.simple.vm.machine.ProgramManager;

/**
 * @author PJ
 *
 */
public class ProgramManagerTest extends AndroidTestCase {

	private ProgramManager _programManager = null;
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
		_programManager = new ProgramManager();

	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#setProgramCounter(int)}.
	 */
	@Test
	public void testSetProgramCounter() {
		assertEquals(Memory.EMPTY_LOC, _programManager.getProgramCounter());
		_programManager.setProgramCounter(100);
		assertEquals(100, _programManager.getProgramCounter());
		_programManager.setProgramCounter(10);
		assertEquals(10, _programManager.getProgramCounter());
		_programManager.setProgramCounter(249);
		assertEquals(249, _programManager.getProgramCounter());
		_programManager.setProgramCounter(Memory.EMPTY_LOC);
		assertEquals(Memory.EMPTY_LOC, _programManager.getProgramCounter());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#getProgramCounter()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#incProgramCounter()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#decProgramCounter()}.
	 */
	@Test
	public void testGetProgramCounter() {
		assertEquals(Memory.EMPTY_LOC, _programManager.getProgramCounter());
		for(int i = 0; i < 100; i++)
		{
			_programManager.incProgramCounter();
		}
		assertEquals(99, _programManager.getProgramCounter());
		for(int i = 0; i < 50; i++)
		{
			_programManager.decProgramCounter();
		}
		assertEquals(49, _programManager.getProgramCounter());
		for(int i = 0; i < 50; i++)
		{
			_programManager.decProgramCounter();
		}
		assertEquals(Memory.EMPTY_LOC, _programManager.getProgramCounter());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#resetProgramCounter()}.
	 */
	@Test
	public void testResetProgramCounter() {
		assertEquals(Memory.EMPTY_LOC, _programManager.getProgramCounter());
		_programManager.setProgramCounter(100);
		assertEquals(100, _programManager.getProgramCounter());
		_programManager.resetProgramCounter();
		assertEquals(Memory.EMPTY_LOC, _programManager.getProgramCounter());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#getProgramWriterPtr()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#incProgramWriter()}.
	 */
	@Test
	public void testGetProgramWriterPtr() {
		assertEquals(Memory.EMPTY_LOC, _programManager.getProgramWriterPtr());
		for(int i = 0; i < 100; i++)
		{
			_programManager.incProgramWriter();
		}
		assertEquals(99, _programManager.getProgramWriterPtr());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#resetProgramWriter()}.
	 */
	@Test
	public void testResetProgramWriter() {
		assertEquals(Memory.EMPTY_LOC, _programManager.getProgramWriterPtr());
		for(int i = 0; i < 100; i++)
		{
			_programManager.incProgramWriter();
		}
		assertEquals(99, _programManager.getProgramWriterPtr());
		_programManager.resetProgramWriter();
		assertEquals(Memory.EMPTY_LOC, _programManager.getProgramWriterPtr());
	}

}
