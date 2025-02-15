package com.slickpath.mobile.android.simple.vm.parser

import android.content.Context
import android.util.Log
import com.slickpath.mobile.android.simple.vm.ParserHelper
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

/**
 * A helper class for parsing instructions from a text file located in the assets directory.
 *
 * This class reads the contents of a specified file in the assets directory and stores
 * it as a string. It provides methods to retrieve the parsed instruction string.
 *
 * @property context The application context. Used to access the assets directory.
 * @property instructionsFilePath The path within the assets directory where the instructions file is located.
 * @property instructionsFileName The name of the instructions file.
 * @constructor Creates a new SimpleFileParserHelper instance, which reads the instructions from the specified file upon initialization.
 * @throws IOException if there's an error reading the file during initialization.
 */
class SimpleFileParserHelper(
    private val context: Context,
    private val instructionsFilePath: String,
    private val instructionsFileName: String
) : ParserHelper {

    private var instructions: String = ""

    companion object {
        private val LOG_TAG = SimpleFileParserHelper::class.java.simpleName
        private const val BUFFER_SIZE = 8192
    }

    init {
        readInstructions()
    }

    /**
     * Reads the instructions from the specified file.
     *
     * @throws IOException if there's an error reading the file.
     */
    @Throws(IOException::class)
    private fun readInstructions() {
        val filePath = getInstructionsFilePath()
        Log.d(LOG_TAG, "Reading instruction string from $filePath")
        context.assets.open(filePath).use { inputStream -> // Use resources with `use` for proper closing.
            InputStreamReader(inputStream).use { inputStreamReader ->
                BufferedReader(inputStreamReader, BUFFER_SIZE).use { bufferedReader ->
                    instructions = bufferedReader.readText() // Use readText for efficient reading
                }
            }
        }
        val instructionsSplit = instructions.split("\n")
        Log.d(LOG_TAG, "Successfully read instructions. Lines detected: ${instructionsSplit.size}")
    }

    /**
     * Constructs the full file path within the assets directory.
     *
     * @return the full path to the file.
     */
    private fun getInstructionsFilePath(): String {
        return "$instructionsFilePath${File.separator}$instructionsFileName"
    }

    override fun getInstructionsString(): String {
        return instructions
    }

}