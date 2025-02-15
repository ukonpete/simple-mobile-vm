package com.slickpath.mobile.android.simple.vm

/**
 * VMError extends Exception.  If is an Error holder so that callbacks can pass back errors and any Exception errors
 *
 *
 * VMError contains 3 things:
 *
 *
 *  * An error type for general classification and switching
 *  * A description - brief description of the problem seen
 *  * The cause - If this VMError was caused by an Exception, the VMError will contain the Throwable object for additional information
 *
 *
 *
 * NOTE: Cause can be null.  This most likely means an Error determined by the library.
 *
 * @author Pete Procopio
 */
class VMError : Exception {

    /**
     * @return int VMError type - The type of VMError
     */
    val type: VMErrorType

    /**
     * Constructor - Private so that it can not be called
     */
    private constructor() : super() {
        type = VMErrorType.VM_ERROR_TYPE_LAZY_UNSET
    }

    /**
     * Constructor
     *
     * @param detailMessage message to add to this VMError
     * @param type VMErrorType
     */
    constructor(detailMessage: String?, type: VMErrorType) : super(detailMessage) {
        this.type = type
    }

    /**
     * Constructor
     *
     * @param cause throwable that was the cause of this VMError
     * @param type VMErrorType
     */
    constructor(cause: Throwable?, type: VMErrorType) : super(cause) {
        this.type = type
    }

    /**
     * Constructor
     *
     * @param detailMessage message to add to this VMError
     * @param cause throwable that was the cause of this VMError
     * @param type VMErrorType
     */
    constructor(detailMessage: String?, cause: Throwable?, type: VMErrorType) : super(
        detailMessage,
        cause
    ) {
        this.type = type
    }
}