package com.slickpath.mobile.android.simple.vm;


import android.support.annotation.Nullable;
import android.test.AndroidTestCase;

import java.security.InvalidParameterException;

/**
 * @author Pete Procopio
 */
public class VMErrorTest extends AndroidTestCase {

    private final String TEST_STR = "THis is an ErRoR";
    @Nullable
    private VMError _vmError = null;

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        _vmError = null;
    }

    /**
     * Test method for {@link VMError#VMError()}.
     */
    public void testVMErrorInt() {
        _vmError = new VMError(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);

        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError.getType());
        assertEquals(null, _vmError.getMessage());
        assertEquals(null, _vmError.getCause());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(String, VMErrorType)}.
     */
    public void testVMErrorStringInt() {
        _vmError = new VMError(TEST_STR, VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);

        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError.getType());
        assertEquals(TEST_STR, _vmError.getMessage());
        assertEquals(null, _vmError.getCause());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.Throwable, VMErrorType)}.
     */
    public void testVMErrorThrowableInt() {
        _vmError = new VMError(new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError.getType());
        assertNotNull(_vmError.getMessage());
        assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.String, java.lang.Throwable, VMErrorType)}.
     */
    public void testVMErrorStringThrowableInt() {
        _vmError = new VMError(TEST_STR, new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError.getType());
        assertEquals(TEST_STR, _vmError.getMessage());
        assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#getType()}.
     */
    public void testGetType() {
        _vmError = new VMError(TEST_STR, new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError.getType());
    }

    /**
     * Test method for {@link java.lang.Throwable#getMessage()}.
     */
    public void testGetMessage() {
        _vmError = new VMError(TEST_STR, new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(TEST_STR, _vmError.getMessage());
    }

    /**
     * Test method for {@link java.lang.Throwable#getCause()}.
     */
    public void testGetCause() {
        _vmError = new VMError(TEST_STR, new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
    }

}
