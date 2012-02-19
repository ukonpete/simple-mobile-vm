/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.test;


import java.security.InvalidParameterException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.VMError;

/**
 * @author Pete Procopio
 *
 */
public class VMErrorTest extends AndroidTestCase {

	private VMError _vmError = null;
	private final String TEST_STR = "THis is an ErRoR";
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
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
		_vmError = null;
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(int)}.
	 */
	@Test
	public void testVMErrorInt() {
		_vmError = new VMError(VMError.VM_ERROR_BAD_PARAMS);

		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
		assertEquals(null, _vmError.getMessage());
		assertEquals(null, _vmError.getCause());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.String, int)}.
	 */
	@Test
	public void testVMErrorStringInt() {
		_vmError = new VMError(TEST_STR, VMError.VM_ERROR_BAD_PARAMS);

		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
		assertEquals(TEST_STR, _vmError.getMessage());
		assertEquals(null, _vmError.getCause());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.Throwable, int)}.
	 */
	@Test
	public void testVMErrorThrowableInt() {
		_vmError = new VMError(new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
		assertNotNull(_vmError.getMessage());
		assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.String, java.lang.Throwable, int)}.
	 */
	@Test
	public void testVMErrorStringThrowableInt() {
		_vmError = new VMError(TEST_STR, new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
		assertEquals(TEST_STR, _vmError.getMessage());
		assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#getType()}.
	 */
	@Test
	public void testGetType() {
		_vmError = new VMError(TEST_STR, new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
	}

	/**
	 * Test method for {@link java.lang.Throwable#getMessage()}.
	 */
	@Test
	public void testGetMessage() {
		_vmError = new VMError(TEST_STR, new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(TEST_STR, _vmError.getMessage());
	}

	/**
	 * Test method for {@link java.lang.Throwable#getCause()}.
	 */
	@Test
	public void testGetCause() {
		_vmError = new VMError(TEST_STR, new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
	}

}
