package com.slickpath.mobile.android.simple.vm.machine

import com.slickpath.mobile.android.simple.vm.ParserHelper

class FileHelperForTest(private val instructionSetString: String) : ParserHelper {

    override fun getInstructionsString(): String {
        return instructionSetString
    }
}