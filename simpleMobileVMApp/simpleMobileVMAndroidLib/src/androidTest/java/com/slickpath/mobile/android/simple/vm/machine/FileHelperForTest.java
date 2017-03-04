package com.slickpath.mobile.android.simple.vm.machine;

import com.slickpath.mobile.android.simple.vm.FileHelper;

public class FileHelperForTest implements FileHelper {

    private String instructions;

    public FileHelperForTest(String instructions) {
        this.instructions = instructions;
    }
    @Override
    public String getInstructionsString() {
        return instructions;
    }
}