/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.test;


import java.security.InvalidParameterException;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.VMError;

/**
 * @author Pete Procopio
 *
 */
public class VMErrorTest extends AndroidTestCase {

	private VMError _vmError = null;
	private final String TEST_STR = "THis is an ErRoR";

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		_vmError = null;
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(int)}.
	 */
	public void testVMErrorInt() {
		_vmError = new VMError(VMError.VM_ERROR_BAD_PARAMS);

		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
		assertEquals(null, _vmError.getMessage());
		assertEquals(null, _vmError.getCause());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.String, int)}.
	 */
	public void testVMErrorStringInt() {
		_vmError = new VMError(TEST_STR, VMError.VM_ERROR_BAD_PARAMS);

		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
		assertEquals(TEST_STR, _vmError.getMessage());
		assertEquals(null, _vmError.getCause());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.Throwable, int)}.
	 */
	public void testVMErrorThrowableInt() {
		_vmError = new VMError(new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
		assertNotNull(_vmError.getMessage());
		assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.String, java.lang.Throwable, int)}.
	 */
	public void testVMErrorStringThrowableInt() {
		_vmError = new VMError(TEST_STR, new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
		assertEquals(TEST_STR, _vmError.getMessage());
		assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#getType()}.
	 */
	public void testGetType() {
		_vmError = new VMError(TEST_STR, new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(VMError.VM_ERROR_BAD_PARAMS, _vmError.getType());
	}

	/**
	 * Test method for {@link java.lang.Throwable#getMessage()}.
	 */
	public void testGetMessage() {
		_vmError = new VMError(TEST_STR, new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(TEST_STR, _vmError.getMessage());
	}

	/**
	 * Test method for {@link java.lang.Throwable#getCause()}.
	 */
	public void testGetCause() {
		_vmError = new VMError(TEST_STR, new InvalidParameterException(), VMError.VM_ERROR_BAD_PARAMS);
		assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
	}

}
