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
		assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
		_programManager.setProgramCounter(100);
		assertEquals(100, _programManager.getProgramCounter());
		_programManager.setProgramCounter(10);
		assertEquals(10, _programManager.getProgramCounter());
		_programManager.setProgramCounter(249);
		assertEquals(249, _programManager.getProgramCounter());
		_programManager.setProgramCounter(Memory.START_LOC);
		assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#getProgramCounter()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#incProgramCounter()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#decProgramCounter()}.
	 */
	@Test
	public void testGetProgramCounter() {
		assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
		for(int i = 0; i < 100; i++)
		{
			_programManager.incProgramCounter();
		}
		assertEquals(100, _programManager.getProgramCounter());
		for(int i = 0; i < 50; i++)
		{
			_programManager.decProgramCounter();
		}
		assertEquals(50, _programManager.getProgramCounter());
		for(int i = 0; i < 50; i++)
		{
			_programManager.decProgramCounter();
		}
		assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#resetProgramCounter()}.
	 */
	@Test
	public void testResetProgramCounter() {
		assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
		_programManager.setProgramCounter(100);
		assertEquals(100, _programManager.getProgramCounter());
		_programManager.resetProgramCounter();
		assertEquals(Memory.START_LOC, _programManager.getProgramCounter());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#getProgramWriterPtr()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#incProgramWriter()}.
	 */
	@Test
	public void testGetProgramWriterPtr() {
		assertEquals(Memory.START_LOC, _programManager.getProgramWriterPtr());
		for(int i = 0; i < 100; i++)
		{
			_programManager.incProgramWriter();
		}
		assertEquals(100, _programManager.getProgramWriterPtr());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#getInstructionAt()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#setInstructionAt()}.
	 */
	@Test
	public void testGetInstructionAt()
	{
		final int [] instruction = new int[]{11,22,35,46,88,99};
		final int [] location = new int[]{0,1,2,64,101,499};

		for(int i = 0; i < instruction.length; i ++)
		{
			_programManager.setInstructionAt(location[i], instruction[i] );
		}

		final List<Integer> programDump = _programManager.dumpProgramStore();
		for(int i = 0; i < instruction.length; i ++)
		{
			final int inst = _programManager.getInstructionAt(location[i]);
			assertEquals(instruction[i], inst);
			assertEquals(instruction[i], programDump.get(location[i]).intValue());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#getParametersAt()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#setParametersAt()}.
	 */
	@Test
	public void testGetParametersAt()
	{
		final int [] parameters = new int[]{15,27,53,64,60,101};
		final int [] location = new int[]{0,1,2,64,101,499};

		for(int i = 0; i < parameters.length; i ++)
		{
			_programManager.setParametersAt(location[i], parameters[i] );
		}

		final List<Integer> paramsDump = _programManager.dumpParameterStore();
		for(int i = 0; i < parameters.length; i ++)
		{
			final int inst = _programManager.getParametersAt(location[i]);
			assertEquals(parameters[i], inst);
			assertEquals(parameters[i], paramsDump.get(location[i]).intValue());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.ProgramManager#resetProgramWriter()}.
	 */
	@Test
	public void testResetProgramWriter() {
		assertEquals(Memory.START_LOC, _programManager.getProgramWriterPtr());
		for(int i = 0; i < 100; i++)
		{
			_programManager.incProgramWriter();
		}
		assertEquals(100, _programManager.getProgramWriterPtr());
		_programManager.resetProgramWriter();
		assertEquals(Memory.START_LOC, _programManager.getProgramWriterPtr());
	}
}
