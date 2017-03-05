package com.slickpath.mobile.android.simple.vm;

/**
 * VMError extends Exception.  If is an Error holder so that callbacks can pass back errors and any Exception errors
 * <p>
 * VMError contains 3 things
 * 1.  An error type for general classification and switching
 * 2.  A description - brief description of the problem seen
 * 3.  The cause - If this VMError was caused by an Exception, the VMError will contain the Throwable object for additional information
 * <p>
 * NOTE: Cause can be null.  This most likely means an Error determined by the library.
 *
 * @author Pete Procopio
 */
public class VMError extends Exception {

    /**
     * The type of VMError
     */
    private final VMErrorType _type;

    /**
     * Constructor - Private so that it can not be called
     */
    @SuppressWarnings("unused")
    private VMError() {
        super();
        _type = VMErrorType.VM_ERROR_TYPE_LAZY_UNSET;
    }

    /**
     * Constructor
     *
     * @param type VM error type
     */
    public VMError(final VMErrorType type) {
        super();
        _type = type;
    }

    /**
     * Constructor
     *
     * @param detailMessage
     * @param type
     */
    public VMError(final String detailMessage, final VMErrorType type) {
        super(detailMessage);
        _type = type;
    }

    /**
     * Constructor
     *
     * @param throwable
     * @param type
     */
    public VMError(final Throwable throwable, final VMErrorType type) {
        super(throwable);
        _type = type;
    }

    /**
     * Constructor
     *
     * @param detailMessage
     * @param throwable
     * @param type
     */
    public VMError(final String detailMessage, final Throwable throwable, final VMErrorType type) {
        super(detailMessage, throwable);
        _type = type;
    }

    /**
     * @return int VMError type
     */
    public VMErrorType getType() {
        return _type;
    }

}
