/**
 * 
 */
package com.slickpath.mobile.android.simple.vm;

/**
 * @author PJ
 *
 */
public class VMError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5024041809373211169L;

	public static final int VM_ERROR_TYPE_LAZY_UNSET = 0;
	
	public static final int VM_ERROR_TYPE_UNKOWN = 1;
	
	public static final int VM_ERROR_TYPE_STACK_LIMIT = 2;
	
	public static final int VM_ERROR_TYPE_MAX_MEMORY = 3;

	public static final int VM_ERROR_TYPE_STACK_PTR = 4;
	
	public static final int VM_ERROR_TYPE_LOC_MAX_MEMORY = 5;
	
	public static final int VM_ERROR_TYPE_IO = 6;

	public static final int VM_ERROR_BAD_PARAMS = 7;
	
	public static final int VM_ERROR_BAD_UNKNOWN_COMMAND = 8;

	private final int _type;
	
	/**
	 * 
	 */
	public VMError() {
		_type = VM_ERROR_TYPE_LAZY_UNSET;
	}
	
	/**
	 * 
	 */
	public VMError(int type) {
		_type = type;
	}

	/**
	 * @param detailMessage
	 */
	public VMError(String detailMessage, int type) {
		super(detailMessage);
		_type = type;
	}

	/**
	 * @param throwable
	 */
	public VMError(Throwable throwable, int type) {
		super(throwable);
		_type = type;
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public VMError(String detailMessage, Throwable throwable, int type) {
		super(detailMessage, throwable);
		_type = type;
	}

	public int getType() {
		return _type;
	}

}
