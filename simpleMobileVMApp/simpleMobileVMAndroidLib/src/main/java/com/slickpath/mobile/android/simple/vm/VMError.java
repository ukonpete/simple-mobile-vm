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
     * Creator of this object was lazy and did not set the type
     */
    public static final int VM_ERROR_TYPE_LAZY_UNSET = 0;
    /**
     * An unknown error was found
     */
    public static final int VM_ERROR_TYPE_UNKNOWN = 1;
    /**
     * An action has attempted access to the stack outside of it's limits
     */
    public static final int VM_ERROR_TYPE_STACK_LIMIT = 2;
    /**
     * An action has attempted access to the memory outside of it's limits
     */
    public static final int VM_ERROR_TYPE_MEMORY_LIMIT = 3;
    /**
     * An I/O Exception
     */
    public static final int VM_ERROR_TYPE_IO = 4;
    /**
     * A caller has passed in incorrect parameters
     */
    public static final int VM_ERROR_BAD_PARAMS = 5;
    /**
     * VM has been asked to run an unknown command/instruction
     */
    public static final int VM_ERROR_BAD_UNKNOWN_COMMAND = 6;
    /**
     *
     */
    private static final long serialVersionUID = -5024041809373211169L;
    /**
     * The type of VMError
     */
    private final int _type;

    /**
     * Constructor - Private so that it can not be called
     */
    @SuppressWarnings("unused")
    private VMError() {
        super();
        _type = VM_ERROR_TYPE_LAZY_UNSET;
    }

    /**
     * Constructor
     *
     * @param type
     */
    public VMError(final int type) {
        super();
        _type = type;
    }

    /**
     * Constructor
     *
     * @param detailMessage
     * @param type
     */
    public VMError(final String detailMessage, final int type) {
        super(detailMessage);
        _type = type;
    }

    /**
     * Constructor
     *
     * @param throwable
     * @param type
     */
    public VMError(final Throwable throwable, final int type) {
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
    public VMError(final String detailMessage, final Throwable throwable, final int type) {
        super(detailMessage, throwable);
        _type = type;
    }

    /**
     * @return int VMError type
     */
    public int getType() {
        return _type;
    }

}
