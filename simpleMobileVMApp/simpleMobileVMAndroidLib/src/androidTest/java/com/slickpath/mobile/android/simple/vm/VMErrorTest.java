package com.slickpath.mobile.android.simple.vm;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.InvalidParameterException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4.class)
public class VMErrorTest {

    private final String TEST_STR = "THis is an ErRoR";
    private VMError _vmError = null;

    @After
    public void AFTER() {
        _vmError = null;
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(String, VMErrorType)}.
     */
    @Test
    public void testVMErrorStringInt() {
        _vmError = new VMError(TEST_STR, VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);

        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError.getType());
        assertEquals(TEST_STR, _vmError.getMessage());
        assertNull(_vmError.getCause());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.Throwable, VMErrorType)}.
     */
    @Test
    public void testVMErrorThrowableInt() {
        _vmError = new VMError(new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError.getType());
        assertNotNull(_vmError.getMessage());
        assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#VMError(java.lang.String, java.lang.Throwable, VMErrorType)}.
     */
    @Test
    public void testVMErrorStringThrowableInt() {
        _vmError = new VMError(TEST_STR, new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError.getType());
        assertEquals(TEST_STR, _vmError.getMessage());
        assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.VMError#getType()}.
     */
    @Test
    public void testGetType() {
        _vmError = new VMError(TEST_STR, new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError.getType());
    }

    /**
     * Test method for {@link java.lang.Throwable#getMessage()}.
     */
    @Test
    public void testGetMessage() {
        _vmError = new VMError(TEST_STR, new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(TEST_STR, _vmError.getMessage());
    }

    /**
     * Test method for {@link java.lang.Throwable#getCause()}.
     */
    @Test
    public void testGetCause() {
        _vmError = new VMError(TEST_STR, new InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS);
        assertEquals(InvalidParameterException.class.toString(), _vmError.getCause().getClass().toString());
    }

}
