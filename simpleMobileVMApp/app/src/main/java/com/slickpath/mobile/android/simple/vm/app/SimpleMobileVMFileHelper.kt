package com.slickpath.mobile.android.simple.vm.app

import android.content.Context
import android.util.Log
import com.slickpath.mobile.android.simple.vm.FileHelper
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

internal class SimpleMobileVMFileHelper(
    private val context: Context,
    private val path: String,
    private val instructionsFile: String
) : FileHelper {

    private var instructions: String = ""

    companion object {
        private val LOG_TAG = SimpleMobileVMFileHelper::class.java.simpleName
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

    override val instructionsString: String
        get() = instructions
}