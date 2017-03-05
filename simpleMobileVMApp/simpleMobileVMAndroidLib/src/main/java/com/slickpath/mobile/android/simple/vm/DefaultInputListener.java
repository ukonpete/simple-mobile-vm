package com.slickpath.mobile.android.simple.vm;

import java.io.IOException;

/**
 * DefaultInputListener that uses the console input via System.in.read
 *
 * @author Pete Procopio
 */
public class DefaultInputListener implements InputListener {

    @Override
    public int getInt() throws IOException {
        return System.in.read();
    }

    @Override
    public char getChar() throws IOException {
        return (char) System.in.read();
    }
}
