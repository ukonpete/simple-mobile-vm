package com.slickpath.mobile.android.simple.vm.machine;

import com.slickpath.mobile.android.simple.vm.StringFileHelper;

public class FileHelperForTest implements StringFileHelper {

    private final String instructions;

    public FileHelperForTest(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String getInstructions() {
        return instructions;
    }
}
