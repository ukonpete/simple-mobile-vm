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

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.machine.Kernel;
import com.slickpath.mobile.android.simple.vm.machine.Memory;

/**
 * @author PJ
 *
 */
public class KernelTest extends AndroidTestCase {

	private Kernel _kernel = null;
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
		_kernel = new Kernel();
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
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#getValueAt(int)}.
	 */
	@Test
	public void testGetValueAt() {
		try {
			_kernel.setValueAt(0,0);
			_kernel.setValueAt(1,1);
			_kernel.setValueAt(2,2);
			_kernel.setValueAt(3,55);
			_kernel.setValueAt(4,100);

			assertEquals(0, _kernel.getValueAt(0));
			assertEquals(1, _kernel.getValueAt(1));
			assertEquals(2, _kernel.getValueAt(2));
			assertEquals(3, _kernel.getValueAt(55));
			assertEquals(4, _kernel.getValueAt(100));

			for(int i = 0; i < Memory.MAX_MEMORY; i++)
			{
				if ( (i!= 0) && (i != 1) && (i != 2) && (i!= 55) && (i != 100) )
				{
					assertEquals(Memory.EMPTY_MEMORY_VALUE , _kernel.getValueAt(i));
				}
			}
		} catch (final VMError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#setValueAt(int, int)}.
	 */
	@Test
	public void testSetValueAt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#pop()}.
	 */
	@Test
	public void testPop() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#push(int)}.
	 */
	@Test
	public void testPush() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#branch(int)}.
	 */
	@Test
	public void testBranch() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#jump()}.
	 */
	@Test
	public void testJump() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#debug(java.lang.String)}.
	 */
	@Test
	public void testDebugString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#debug(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDebugStringString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#getProgramCounter()}.
	 */
	@Test
	public void testGetProgramCounter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#incProgramCounter()}.
	 */
	@Test
	public void testIncProgramCounter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#decProgramCounter()}.
	 */
	@Test
	public void testDecProgramCounter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#resetProgramCounter()}.
	 */
	@Test
	public void testResetProgramCounter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#getProgramWriterPtr()}.
	 */
	@Test
	public void testGetProgramWriterPtr() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#incProgramWriter()}.
	 */
	@Test
	public void testIncProgramWriter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#resetProgramWriter()}.
	 */
	@Test
	public void testResetProgramWriter() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#getStackPointer()}.
	 */
	@Test
	public void testGetStackPointer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#incStackPtr()}.
	 */
	@Test
	public void testIncStackPtr() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#decStackPtr()}.
	 */
	@Test
	public void testDecStackPtr() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Kernel#resetStackPointer()}.
	 */
	@Test
	public void testResetStackPointer() {
		fail("Not yet implemented");
	}

}
