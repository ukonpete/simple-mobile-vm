package com.slickpath.mobile.android.simple.vm;

import com.slickpath.mobile.android.simple.vm.machine.Machine;

/**
 * Listener for VM to return output to
 *
 * @author Pete Procopio
 */
public interface OutputListener {

    /**
     * Output of a char from vm
     *
     * @see Machine#WRCHAR()
     *
     * @param c char that is outputted
     */
    void charOutput(char c);

    /**
     * Output of String line - always string representation of an int
     *
     * NOTE: '\n' is not added
     *
     * @see Machine#WRINT()
     *
     * @param line string that is outputted
     */
    void lineOutput(String line);
}
