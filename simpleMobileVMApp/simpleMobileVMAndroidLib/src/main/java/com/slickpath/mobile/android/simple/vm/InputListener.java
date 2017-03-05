package com.slickpath.mobile.android.simple.vm;

import java.io.IOException;

/**
 * Listener for VM to retrieve input from
 *
 * @author Pete Procopio
 */
public interface InputListener {

    /**
     * @return one int for input into VM
     */
    int getInt() throws IOException;

    /**
     * @return one char for input into VM
     */
    char getChar() throws IOException;
}
