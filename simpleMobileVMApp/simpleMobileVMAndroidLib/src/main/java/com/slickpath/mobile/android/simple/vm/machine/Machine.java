package com.slickpath.mobile.android.simple.vm.machine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.slickpath.mobile.android.simple.vm.DefaultInputListener;
import com.slickpath.mobile.android.simple.vm.InputListener;
import com.slickpath.mobile.android.simple.vm.OutputListener;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.VMErrorType;

import java.io.IOException;

/**
 * The Machine.  The calls that are made to execute instructions
 *
 * @author Pete Procopio
 */
class Machine extends Kernel {

    private static final String LOG_TAG = Machine.class.getName();

    /**
     * Listener where output can be written to
     */
    @NonNull
    private final OutputListener outputListener;
    /**
     * Stream where input can be read from
     */
    @NonNull
    private final InputListener inputListener;

    /**
     * Constructor
     * <p><ul>
     * <li>output will be added to log is debugVerbose is set
     * <li>input will be attempted to be retrieved from the console System.in
     * </ul>
     */
    Machine() {
        this(null, null);
    }

    /**
     * Constructor
     * <p>
     * Allows caller to pass in streams for both input and output
     * <p><ul>
     * <li>If outputListener is null output will be added to log if debugVerbose is set
     * <li>If inputListener is null input will be attempted to be retrieved from the console System.in
     * </ul>
     *
     * @param outputListener listener for output events
     * @param inputListener listener to return input on input events
     */
    Machine(@Nullable OutputListener outputListener, @Nullable InputListener inputListener) {
        super();
        if(outputListener== null) {
            this.outputListener = getDefaultOutputListener();
        } else {
            this.outputListener = outputListener;
        }
        if(inputListener==null) {
            this.inputListener = new DefaultInputListener();
        } else {
            this.inputListener = inputListener;
        }
    }

    /**
     * @return default output listener that send output to log if debugVerbose is set
     */
    private OutputListener getDefaultOutputListener() {
        return new OutputListener() {
            @Override
            public void charOutput(char c) {
                debugVerbose(LOG_TAG, "char of output: " + c);
            }
            @Override
            public void lineOutput(String line) {
                debugVerbose(LOG_TAG, "line of output: " + line);
            }
        };
    }

    /////////////////////////////////////////////////  Command Category : ARITHMATIC

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>Add them together
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void ADD() throws VMError {
        final int val1 = pop();
        final int val2 = pop();
        final int val3 = val1 + val2;
        PUSHC(val3);
    }

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>Subtracts 2nd from 1st
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void SUB() throws VMError {
        final int val1 = pop();
        final int val2 = pop();
        final int val3 = val1 - val2;
        PUSHC(val3);
    }

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>Multiplies them
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void MUL() throws VMError {
        final int val1 = pop();
        final int val2 = pop();
        final int val3 = val1 * val2;
        PUSHC(val3);
    }

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>Divides 2nd into 1st  (remainder is truncated)
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void DIV() throws VMError {
        final int val1 = pop();
        final int val2 = pop();
        final int val3 = val1 / val2;
        PUSHC(val3);
    }

    /**
     * <ol>
     * <li>Pop 1st value off the top of the stack
     * <li>Negates value
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void NEG() throws VMError {
        final int val1 = pop();
        final int val2 = 0 - val1;
        PUSHC(val2);
    }

    /////////////////////////////////////////////////  Command Category : BOOLEAN

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>If equal 1 is pushed to top of stack if not 0 is push to top of stack
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void EQUAL() throws VMError {
        int equal = PUSHC_YES;
        final int val1 = pop();
        final int val2 = pop();

        if (val1 != val2) {
            equal = PUSHC_NO;
        }
        PUSHC(equal);
    }

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>If not equal 1 is pushed to top of stack if not 0 is push to top of stack
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void NOTEQL() throws VMError {
        int notEqual = PUSHC_NO;
        final int val1 = pop();
        final int val2 = pop();

        if (val1 != val2) {
            notEqual = PUSHC_YES;
        }
        PUSHC(notEqual);
    }

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>If 1st value is greater than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void GREATER() throws VMError {
        int greater = PUSHC_NO;
        final int val1 = pop();
        final int val2 = pop();

        if (val1 > val2) {
            greater = PUSHC_YES;
        }
        PUSHC(greater);
    }

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>If 1st value is less than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void LESS() throws VMError {
        int less = PUSHC_NO;
        final int val1 = pop();
        final int val2 = pop();

        if (val1 < val2) {
            less = PUSHC_YES;
        }
        PUSHC(less);
    }

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>If 1st value is greater than or equal to the 2nd than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void GTREQL() throws VMError {
        int greaterOrEqual = PUSHC_NO;
        final int val1 = pop();
        final int val2 = pop();

        if (val1 >= val2) {
            greaterOrEqual = PUSHC_YES;
        }
        PUSHC(greaterOrEqual);
    }

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>If 1st value is less than or equal to the 2nd than the 2nd value 1 is pushed to top of stack if not 0 is push to top of stack
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void LSSEQL() throws VMError {
        int lessEqual = PUSHC_NO;
        final int val1 = pop();
        final int val2 = pop();

        if (val1 <= val2) {
            lessEqual = PUSHC_YES;
        }
        PUSHC(lessEqual);
    }

    /**
     * <ol>
     * <li>Pop 1st value off the top of the stack
     * <li>If value is 0 then 1 is pushed to top of stack if not 0 is push to top of stack
     * <li>Push result to top of stack
     *
     * @throws VMError on vm error
     */
    void NOT() throws VMError {
        int val = pop();

        // long-hand for val == 0?1:0
        if (val == 0) {
            val = PUSHC_YES;
        } else {
            val = PUSHC_NO;
        }

        PUSHC(val);
    }

    ///////////////////////////////////////////////// Command Category : STACK_MANIPULATION

    /**
     * Push the value at the location in memory to the top of the stack
     *
     * @param location in mem
     * @throws VMError on vm error
     */
    void PUSH(final int location) throws VMError {
        final int val = getValueAt(location);
        PUSHC(val);
    }

    /**
     * Push the value passed in to the top of the stack
     *
     * @param value in mem
     * @throws VMError on vm error
     */
    void PUSHC(final int value) throws VMError {
        push(value);
    }

    /**
     * <ol>
     * <li>Pop 1st two values off the top of the stack
     * <li>save the 2nd value popped off in memory location indicated by the fist
     *
     * @throws VMError on vm error
     */
    void POP() throws VMError {
        final int location = pop();
        POPC(location);
    }

    /**
     * <ol>
     * <li>Pop 1st value at top of the stack
     * <li>Set the value popped at the location in memory indicated by passed in value.
     *
     * @param location location in memory
     * @throws VMError on vm error
     */
    void POPC(final int location) throws VMError {
        final int value = pop();
        setValueAt(value, location);
    }

    ///////////////////////////////////////////////// Command Category : INPUT/OUTPUT

    /**
     * <ol>
     * <li>Read character from Input Stream
     * <li>Push value to top of stack
     * <li>If debug is enabled, write value to log.d
     *
     * @throws VMError on vm error
     */
    void RDCHAR() throws VMError {
        try {
            PUSHC(inputListener.getChar());
        } catch (IOException e) {
            throw new VMError("Exception in RDCHAR : " + e.getMessage(), e, VMErrorType.VM_ERROR_TYPE_IO);
        }
    }

    /**
     * <ol>
     * <li>Pop character from top of stack
     * <li>Write value to output Stream
     * <li>If debug is enabled, write value to log.d
     *
     * @throws VMError on vm error
     */
    void WRCHAR() throws VMError {
        final int value = pop();
        final char sVal = (char) value;
        outputListener.charOutput(sVal);
        debugVerbose(LOG_TAG, "WRCHAR: " + sVal);
    }

    /**
     * <ol>
     * <li>Read int from Input Stream
     * <li>Push value to top of stack
     * <li>If debug is enabled, write value to log.d
     *
     * @throws VMError on vm error
     */
    void RDINT() throws VMError {
        final int val;
        try {
            val = inputListener.getInt();
            PUSHC(val);
        } catch (IOException e) {
            throw new VMError("Exception in RDINT : " + e.getMessage(), e, VMErrorType.VM_ERROR_TYPE_IO);
        }
    }

    /**
     * <ol>
     * <li>Pop int from top of stack
     * <li>Write value to output Stream
     * <li>If debug is enabled, write value to log.d
     *
     * @throws VMError on vm error
     */
    void WRINT() throws VMError {
        final int value = pop();
        final String out = Integer.toString(value);
        outputListener.lineOutput(out + "\n");
        debugVerbose(LOG_TAG, "WRINT " + out);
    }

    ////////////////////////////////////////////////////  Command Category : CONTROL

    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that is passed in
     *
     * @param location in mem
     * @throws VMError on vm error
     */
    void BRANCH(final int location) throws VMError {
        debugVerbose(LOG_TAG, "--BR=" + location);
        branch(location);
    }

    /**
     * Set the program counter (the pointer to the next instruction to be run) to the location that the top of the stack indicates. The value used is popped from the stack
     *
     * @throws VMError on vm error
     */
    void JUMP() throws VMError {
        jump();
    }

    /**
     * <ol>
     * <li>Pop 1st value at top of the stack
     * <li>Branch to passed in location if value == 0
     *
     * @param location in mem
     * @return boolean - if branch occurred
     * @throws VMError on vm error
     * @see Machine#BRANCH(int)
     */
    boolean BREQL(final int location) throws VMError {
        final int val = pop();
        boolean bBranched = false;
        if (val == 0) {
            BRANCH(location);
            bBranched = true;
        }
        return bBranched;
    }

    /**
     * <ol>
     * <li>Pop 1st value at top of the stack
     * <li>Branch to passed in location if value < 0
     *
     * @param location in mem
     * @return boolean - if branch occurred
     * @throws VMError on vm error
     * @see Machine#BRANCH(int)
     */
    boolean BRLSS(final int location) throws VMError {
        final int val = pop();
        boolean bBranched = false;
        if (val < 0) {
            BRANCH(location);
            bBranched = true;
        }
        return bBranched;
    }

    /**
     * <ol>
     * <li>Pop 1st value at top of the stack
     * <li>Branch to passed in location if value > 0
     *
     * @param location in mem
     * @return boolean - if branch occurred
     * @throws VMError on vm error
     * @see Machine#BRANCH(int)
     */
    boolean BRGTR(final int location) throws VMError {
        final int val = pop();
        boolean bBranched = false;
        if (val > 0) {
            BRANCH(location);
            bBranched = true;
        }
        return bBranched;
    }

    ///////////////////////////////////////////////////  Command Category : MISC

    /**
     * <ol>
     * <li>Pop 1st value at top of the stack
     * <li>Get value at memory location indicated by value that was popped
     * <li>Push the value to stop of the stack
     *
     * @throws VMError on vm error
     */
    void CONTENTS() throws VMError {
        final int location = pop();
        final int val = getValueAt(location);
        PUSHC(val);
    }

    /**
     * Stop program - NO-OP instruction for Machine.java
     * <p>
     * This will send info to Log if debug is enabled
     */
    void HALT() {
        debug(LOG_TAG, "HALT");
    }
}
