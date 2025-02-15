package com.slickpath.mobile.android.simple.vm

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import java.security.InvalidParameterException

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class VMErrorTest {

    companion object {
        private const val TEST_STR = "THis is an ErRoR"
    }

    private var _vmError: VMError? = null

    @After
    fun after() {
        _vmError = null
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.VMError].
     */
    @Test
    fun testVMErrorStringInt() {
        _vmError = VMError(TEST_STR, VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError?.type)
        assertEquals(TEST_STR, _vmError!!.message)
        assertNull(_vmError!!.cause)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.VMError].
     */
    @Test
    fun testVMErrorThrowableInt() {
        _vmError = VMError(InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError?.type)
        assertNotNull(_vmError!!.message)
        assertEquals(
            InvalidParameterException::class.java.toString(),
            _vmError!!.cause!!.javaClass.toString()
        )
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.VMError].
     */
    @Test
    fun testVMErrorStringThrowableInt() {
        _vmError =
            VMError(TEST_STR, InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError?.type)
        assertEquals(TEST_STR, _vmError!!.message)
        assertEquals(
            InvalidParameterException::class.java.toString(),
            _vmError!!.cause!!.javaClass.toString()
        )
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.VMError.type].
     */
    @Test
    fun testGetType() {
        _vmError =
            VMError(TEST_STR, InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
        assertEquals(VMErrorType.VM_ERROR_TYPE_BAD_PARAMS, _vmError?.type)
    }

    /**
     * Test method for [java.lang.Throwable.getMessage].
     */
    @Test
    fun testGetMessage() {
        _vmError =
            VMError(TEST_STR, InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
        assertEquals(TEST_STR, _vmError?.message)
    }

    /**
     * Test method for [java.lang.Throwable.getCause].
     */
    @Test
    fun testGetCause() {
        _vmError =
            VMError(TEST_STR, InvalidParameterException(), VMErrorType.VM_ERROR_TYPE_BAD_PARAMS)
        assertEquals(
            InvalidParameterException::class.java.toString(),
            _vmError?.cause?.javaClass.toString()
        )
    }
}