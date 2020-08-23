package com.slickpath.mobile.android.simple.vm;

import androidx.annotation.Nullable;

/**
 * Helper to return string of the all instructions a program will run
 *
 * @author Pete Procopio
 */
public interface FileHelper {

    /**
     * Returns string of the all instructions a program will run
     *
     * @return instructions a program will run
     */
    @Nullable
    String getInstructionsString();
}
