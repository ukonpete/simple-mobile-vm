@file:Suppress("FunctionName", "KDocUnresolvedReference", "SpellCheckingInspection")

package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.*
import com.slickpath.mobile.android.simple.vm.util.Command
import java.io.IOException

/**
 * The Machine class represents a virtual machine that executes a set of instructions.
 * It provides methods for performing arithmetic, logical, stack manipulation, input/output,
 * and control flow operations. The machine utilizes a stack-based architecture for
 * instruction execution.
 *
 * The Machine interacts with the external world via `OutputListener` and `InputListener`.
 *
 * @param outputListener An optional listener for output operations. If provided,
 *                       the machine will use it to write output.
 *                       If null output operations will not occur.
 * @param inputListener  An optional listener for input operations. If provided,
 *                       the machine will use it to read input. If null, a
 *                       `DefaultInputListener` is used.
 *
 * @author Pete Procopio
 */
open class Machine @JvmOverloads constructor(
    private val outputListener: OutputListener? = null,
    inputListener: InputListener? = null,
    private val kernel: Kernel,
) : IKernel, IDebugVerboseLogger {

    companion object {
        private val LOG_TAG = Machine::class.java.simpleName
        const val PUSHC_YES = 1
        const val PUSHC_NO = 0
    }

    private var _inputListener: InputListener = inputListener ?: DefaultInputListener()

    override val programCounter: Int
        get() = kernel.programCounter

    override var debugDump: Boolean
        get() = kernel.debugDump
        set(value) {
            kernel.debugDump = value
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
        PUSHC(val1 + val2)
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
        PUSHC(val1 - val2)
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
        PUSHC(val1 * val2)
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
        PUSHC(val1 / val2)
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
        val val1 = kernel.pop()
        PUSHC(-val1)
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
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
        val val1 = kernel.pop()
        val val2 = kernel.pop()
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
        var poppedVal = kernel.pop()

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
        kernel.push(value)
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
        val location = kernel.pop()
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
        val value = kernel.pop()
        kernel.setValueAt(value, location)
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
            PUSHC(_inputListener.char.code)
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
        val value = kernel.pop()
        val sVal = value.toChar()
        outputListener?.charOutput(sVal)
        kernel.debugVerbose(LOG_TAG, "WRCHAR: $sVal")
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
            rdInt = _inputListener.int
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
        val value = kernel.pop()
        val out = value.toString()
        outputListener?.lineOutput(out + "\n")
        kernel.debugVerbose(LOG_TAG, "WRINT $out")
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
        kernel.debugVerbose(LOG_TAG, "--BR=$location")
        kernel.branch(location)
    }

    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that the top of the stack indicates. The value used is popped from the stack
     *
     * @throws VMError on vm error
     */
    @Throws(VMError::class)
    fun JUMP() {
        kernel.jump()
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
        val poppedVal = kernel.pop()
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
        val poppedValue = kernel.pop()
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
        val poppedValue = kernel.pop()
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
        val location = kernel.pop()
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
        kernel.debug(LOG_TAG, "HALT")
    }

    override fun addCommand(command: Command) {
        kernel.addCommand(command)
    }

    override fun resetProgramWriter() {
        kernel.resetProgramCounter()
    }

    override fun resetStack() {
        kernel.resetStack()
    }

    override fun getValueAt(location: Int): Int {
        return kernel.getValueAt(location)
    }

    override fun incrementProgramCounter() {
        kernel.incrementProgramCounter()
    }

    override fun decrementProgramCounter() {
        kernel.decrementProgramCounter()
    }

    override fun resetProgramCounter() {
        kernel.resetProgramCounter()
    }

    override fun getCommandAt(location: Int): Command {
        return kernel.getCommandAt(location)
    }

    override fun dumpStack(): List<Int> {
        return kernel.dumpStack()
    }

    override fun dumpMemory(): List<Int> {
        return kernel.dumpMemory()
    }

    override fun reset() {
        kernel.reset()
    }

    override fun debugVerbose(tag: String, text: String) {
        kernel.debugVerbose(tag, text)
    }

    override fun debugVerbose(tag: String, text: () -> String) {
        kernel.debugVerbose(tag, text)
    }

    override fun debug(tag: String, text: String) {
        kernel.debug(tag, text)
    }

    override fun debug(tag: String, text: () -> String) {
        kernel.debug(tag, text)
    }

}