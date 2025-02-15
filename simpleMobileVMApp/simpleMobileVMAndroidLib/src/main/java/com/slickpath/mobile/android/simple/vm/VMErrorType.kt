package com.slickpath.mobile.android.simple.vm

/**
 *
 * @author Pete Procopio
 */
enum class VMErrorType {
    /**
     * Creator of this object was lazy and did not set the type
     */
    VM_ERROR_TYPE_LAZY_UNSET,

    /**
     * An unknown error was found
     */
    VM_ERROR_TYPE_UNKNOWN,

    /**
     * An action has attempted access to an empty stack
     */
    VM_ERROR_TYPE_STACK_EMPTY,

    /**
     * An action has attempted access to the stack that generates a general error
     */
    VM_ERROR_TYPE_STACK_GENERAL,

    /**
     * An action has attempted access to the stack outside of it's min limits
     */
    VM_ERROR_TYPE_STACK_LIMIT_MIN,

    /**
     * An action has attempted access to the stack outside of it's max limits
     */
    VM_ERROR_TYPE_STACK_LIMIT_MAX,

    /**
     * An action has attempted access to the memory outside of it's min limits
     */
    VM_ERROR_TYPE_MEMORY_LIMIT_MIN,

    /**
     * An action has attempted access to the memory outside of it's max limits
     */
    VM_ERROR_TYPE_MEMORY_LIMIT_MAX,

    /**
     * An I/O Exception
     */
    VM_ERROR_TYPE_IO,

    /**
     * A caller has passed in incorrect parameters
     */
    VM_ERROR_TYPE_BAD_PARAMS,

    /**
     * VM has been asked to run an unknown command/instruction
     */
    VM_ERROR_TYPE_BAD_UNKNOWN_COMMAND
}