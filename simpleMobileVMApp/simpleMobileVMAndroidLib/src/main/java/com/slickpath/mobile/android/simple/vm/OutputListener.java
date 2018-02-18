package com.slickpath.mobile.android.simple.vm;

/**
 * Listener for VM to return output to
 *
 * @author Pete Procopio
 */
public interface OutputListener {

    /**
     * Output of a char from vm
     *
     * @param c char that is outputted
     */
    void charOutput(char c);

    /**
     * Output of String line - always string representation of an int
     *
     * @param line string that is outputted
     */
    void lineOutput(String line);
}
