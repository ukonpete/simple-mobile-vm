@file:Suppress("FunctionName", "KDocUnresolvedReference", "SpellCheckingInspection")

package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.*
import java.io.IOException

/**
 * The Machine.  The calls that are made to execute instructions
 *
 * @author Pete Procopio
 */
open class Machine @JvmOverloads constructor(
    outputListener: OutputListener? = null,
    inputListener: InputListener? = null
) : Kernel() {

    companion object {
        private val LOG_TAG = Machine::class.java.name
    }

    /**
     * Listener where output can be written to
     */
    private val outputListener: OutputListener

    /**
     * Stream where input can be read from
     */
    private val inputListener: InputListener

    /**
     * Constructor
     *
     *
     * Allows caller to pass in streams for both input and output
     *  * output will be added to log is debugVerbose is set
     *  * input will be attempted to be retrieved from the console System.in
     *
     *  * If outputListener is null output will be added to log if debugVerbose is set
     *  * If inputListener is null input will be attempted to be retrieved from the console System.in
     *
     * @param outputListener listener for output events
     * @param inputListener listener to return input on input events
     */
    init {
        if (outputListener == null) {
            this.outputListener = defaultOutputListener
        } else {
            this.outputListener = outputListener
        }
        if (inputListener == null) {
            this.inputListener = DefaultInputListener()
        } else {
            this.inputListener = inputListener
        }
    }

    /**
     * @return default output listener that send output to log if debugVerbose is set
     */
    private val defaultOutputListener: OutputListener
        get() = object : OutputListener {
            override fun charOutput(c: Char) {
                debugVerbose(LOG_TAG, "char of output: $c")
            }

            override fun lineOutput(line: String) {
                debugVerbose(LOG_TAG, "line of output: $line")
            }
        }

    /////////////////////////////////////////////////  Command Category : ARITHMATIC
    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. Add them together
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun ADD() {
        val val1 = pop()
        val val2 = pop()
        val val3 = val1 + val2
        PUSHC(val3)
    }

    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. Subtracts 2nd from 1st
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun SUB() {
        val val1 = pop()
        val val2 = pop()
        val val3 = val1 - val2
        PUSHC(val3)
    }

    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. Multiplies them
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun MUL() {
        val val1 = pop()
        val val2 = pop()
        val val3 = val1 * val2
        PUSHC(val3)
    }

    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. Divides 2nd into 1st  (remainder is truncated)
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun DIV() {
        val val1 = pop()
        val val2 = pop()
        val val3 = val1 / val2
        PUSHC(val3)
    }

    /**
     *
     *  1. Pop 1st value off the top of the stack
     *  1. Negates value
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun NEG() {
        val val1 = pop()
        val val2 = 0 - val1
        PUSHC(val2)
    }
    /////////////////////////////////////////////////  Command Category : BOOLEAN
    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. If equal 1 is pushed to top of stack if not 0 is push to top of stack
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun EQUAL() {
        var equal = PUSHC_YES
        val val1 = pop()
        val val2 = pop()
        if (val1 != val2) {
            equal = PUSHC_NO
        }
        PUSHC(equal)
    }

    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. If not equal 1 is pushed to top of stack if not 0 is push to top of stack
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun NOTEQL() {
        var notEqual = PUSHC_NO
        val val1 = pop()
        val val2 = pop()
        if (val1 != val2) {
            notEqual = PUSHC_YES
        }
        PUSHC(notEqual)
    }

    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. If 1st value is greater than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun GREATER() {
        var greater = PUSHC_NO
        val val1 = pop()
        val val2 = pop()
        if (val1 > val2) {
            greater = PUSHC_YES
        }
        PUSHC(greater)
    }

    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. If 1st value is less than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun LESS() {
        var less = PUSHC_NO
        val val1 = pop()
        val val2 = pop()
        if (val1 < val2) {
            less = PUSHC_YES
        }
        PUSHC(less)
    }

    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. If 1st value is greater than or equal to the 2nd than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun GTREQL() {
        var greaterOrEqual = PUSHC_NO
        val val1 = pop()
        val val2 = pop()
        if (val1 >= val2) {
            greaterOrEqual = PUSHC_YES
        }
        PUSHC(greaterOrEqual)
    }

    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. If 1st value is less than or equal to the 2nd than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun LSSEQL() {
        var lessEqual = PUSHC_NO
        val val1 = pop()
        val val2 = pop()
        if (val1 <= val2) {
            lessEqual = PUSHC_YES
        }
        PUSHC(lessEqual)
    }

    /**
     *
     *  1. Pop 1st value off the top of the stack
     *  1. If value is 0 then 1 is pushed to top of stack if not 0 is push to top of stack
     *  1. Push result to top of stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun NOT() {
        var poppedVal = pop()

        // long-hand for val == 0?1:0
        poppedVal = if (poppedVal == 0) {
            PUSHC_YES
        } else {
            PUSHC_NO
        }
        PUSHC(poppedVal)
    }
    ///////////////////////////////////////////////// Command Category : STACK_MANIPULATION
    /**
     * Push the value at the location in memory to the top of the stack
     *
     * @param location in mem
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun PUSH(location: Int) {
        val valToPush = getValueAt(location)
        PUSHC(valToPush)
    }

    /**
     * Push the value passed in to the top of the stack
     *
     * @param value in mem
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun PUSHC(value: Int) {
        push(value)
    }

    /**
     *
     *  1. Pop 1st two values off the top of the stack
     *  1. save the 2nd value popped off in memory location indicated by the fist
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun POP() {
        val location = pop()
        POPC(location)
    }

    /**
     *
     *  1. Pop 1st value at top of the stack
     *  1. Set the value popped at the location in memory indicated by passed in value.
     *
     * @param location location in memory
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun POPC(location: Int) {
        val value = pop()
        setValueAt(value, location)
    }
    ///////////////////////////////////////////////// Command Category : INPUT/OUTPUT
    /**
     *
     *  1. Read character from Input Stream
     *  1. Push value to top of stack
     *  1. If debug is enabled, write value to log.d
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun RDCHAR() {
        try {
            PUSHC(inputListener.char.code)
        } catch (e: IOException) {
            throw VMError("Exception in RDCHAR : " + e.message, e, VMErrorType.VM_ERROR_TYPE_IO)
        }
    }

    /**
     *
     *  1. Pop character from top of stack
     *  1. Write value to output Stream
     *  1. If debug is enabled, write value to log.d
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun WRCHAR() {
        val value = pop()
        val sVal = value.toChar()
        outputListener.charOutput(sVal)
        debugVerbose(LOG_TAG, "WRCHAR: $sVal")
    }

    /**
     *
     *  1. Read int from Input Stream
     *  1. Push value to top of stack
     *  1. If debug is enabled, write value to log.d
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun RDINT() {
        val rdInt: Int
        try {
            rdInt = inputListener.int
            PUSHC(rdInt)
        } catch (e: IOException) {
            throw VMError("Exception in RDINT : " + e.message, e, VMErrorType.VM_ERROR_TYPE_IO)
        }
    }

    /**
     *
     *  1. Pop int from top of stack
     *  1. Write value to output Stream
     *  1. If debug is enabled, write value to log.d
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun WRINT() {
        val value = pop()
        val out = value.toString()
        outputListener.lineOutput(out + "\n")
        debugVerbose(LOG_TAG, "WRINT $out")
    }
    ////////////////////////////////////////////////////  Command Category : CONTROL
    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that is passed in
     *
     * @param location in mem
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun BRANCH(location: Int) {
        debugVerbose(LOG_TAG, "--BR=$location")
        branch(location)
    }

    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that the top of the stack indicates. The value used is popped from the stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun JUMP() {
        jump()
    }

    /**
     *
     *  1. Pop 1st value at top of the stack
     *  1. Branch to passed in location if value == 0
     *
     * @param location in mem
     * @return boolean - if branch occurred
     * @throws VMError on vm error
     * @see Machine.BRANCH
     */
    @Throws(VMError::class)
    fun BREQL(location: Int): Boolean {
        val poppedVal = pop()
        var bBranched = false
        if (poppedVal == 0) {
            BRANCH(location)
            bBranched = true
        }
        return bBranched
    }

    /**
     *
     *  1. Pop 1st value at top of the stack
     *  1. Branch to passed in location if value < 0
     *
     * @param location in mem
     * @return boolean - if branch occurred
     * @throws VMError on vm error
     * @see Machine.BRANCH
     */
    @Throws(VMError::class)
    fun BRLSS(location: Int): Boolean {
        val poppedValue = pop()
        var bBranched = false
        if (poppedValue < 0) {
            BRANCH(location)
            bBranched = true
        }
        return bBranched
    }

    /**
     *
     *  1. Pop 1st value at top of the stack
     *  1. Branch to passed in location if value > 0
     *
     * @param location in mem
     * @return boolean - if branch occurred
     * @throws VMError on vm error
     * @see Machine.BRANCH
     */
    @Throws(VMError::class)
    fun BRGTR(location: Int): Boolean {
        val poppedValue = pop()
        var bBranched = false
        if (poppedValue > 0) {
            BRANCH(location)
            bBranched = true
        }
        return bBranched
    }
    ///////////////////////////////////////////////////  Command Category : MISC
    /**
     *
     *  1. Pop 1st value at top of the stack
     *  1. Get value at memory location indicated by value that was popped
     *  1. Push the value to stop of the stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun CONTENTS() {
        val location = pop()
        val valAt = getValueAt(location)
        PUSHC(valAt)
    }

    /**
     * Stop program - NO-OP instruction for Machine.java
     *
     *
     * This will send info to Log if debug is enabled
     */
    fun HALT() {
        debug(LOG_TAG, "HALT")
    }

}