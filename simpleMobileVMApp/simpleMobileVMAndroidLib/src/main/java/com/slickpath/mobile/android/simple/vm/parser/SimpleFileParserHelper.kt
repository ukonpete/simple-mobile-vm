package com.slickpath.mobile.android.simple.vm.parser

import android.content.Context
import android.util.Log
import com.slickpath.mobile.android.simple.vm.ParserHelper
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class SimpleFileParserHelper(
    private val context: Context,
    private val path: String,
    private val instructionsFile: String
) : ParserHelper {

    private var instructions: String = ""

    companion object {
        private val LOG_TAG = SimpleFileParserHelper::class.java.simpleName
    }

    init {
        readInstructionsString()
    }

    @get:Throws(IOException::class)
    private val bufferedReader: BufferedReader
        get() {
            val assetFilePlusPath = path + File.separator + instructionsFile
            Log.d(LOG_TAG, "attempting getting file from: $assetFilePlusPath")
            val inputStreamReader = InputStreamReader(context.assets.open(assetFilePlusPath))
            return BufferedReader(inputStreamReader, 8192)
        }

    @Throws(IOException::class)
    private fun readInstructionsString() {
        Log.d(LOG_TAG, "reading instruction string")
        val buffReader = bufferedReader
        val stringBuilder = StringBuilder()
        var lineCount = 0
        var line = buffReader.readLine()
        if (line != null) {
            stringBuilder.append(line).append("\n")
            lineCount++
            while (line != null) {
                line = buffReader.readLine()
                if (line != null) {
                    stringBuilder.append(line).append("\n")
                    lineCount++
                }
            }
        }
        Log.d(LOG_TAG, "Lines read: $lineCount")
        instructions = stringBuilder.toString()
    }

    override fun getInstructionsString(): String {
        return instructions
    }

}