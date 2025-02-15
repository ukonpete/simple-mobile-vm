@file:Suppress("KDocUnresolvedReference")

package com.slickpath.mobile.android.simple.vm.parser

import android.util.Log
import com.slickpath.mobile.android.simple.vm.ParserHelper
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.VMErrorType
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet
import com.slickpath.mobile.android.simple.vm.util.CommandList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.util.*

/**
 * Parses a file for commands, parameters, variables, and symbols.
 *
 * Possible line combinations:
 * - COMMAND
 * - COMMAND PARAMETER
 * - COMMAND VAR
 * - COMMAND SYMBOL
 * - SYMBOL
 * - COMMENT
 *
 * Definitions:
 * - COMMAND: A string representing an operation.
 * - PARAMETER: A string, typically an integer, but can be a VAR or SYMBOL.
 * - VAR: A string starting with 'g', representing a variable memory location reference.
 * - SYMBOL: A string enclosed in square brackets, e.g., "[symbol_name]". If used as a parameter, it refers to an existing SYMBOL, which in turn represents the line it is defined on. It MUST be defined on a line by itself.
 * - COMMENT: A line ignored during parsing, indicated by "//".
 *
 * Examples:
 * - COMMAND: ADD (Adds the top two items on the stack and places the result on the stack)
 * - COMMAND PARAMETER: PUSHC 100 (Pushes the value 100 onto the top of the stack)
 * - COMMAND VAR: POPC g1 (Pops the top value of the stack and places it into the memory location represented by g1)
 * - COMMAND SYMBOL: JUMP [LOOP2] (Jumps to the line number represented by [LOOP2])
 * - SYMBOL: [LOOP2] (Assigns the next instruction's line number to the symbol [LOOP2])
 * - COMMENT: // This code rocks
 *
 * @author Pete Procopio
 *
 * @see [Simple VM Wiki on Github](https://github.com/ukonpete/simple-mobile-vm/wiki)
 */
class SimpleParser(private val parserHelper: ParserHelper) : AsyncParser() {

    private val symbols: MutableMap<String, Int> = Hashtable()
    private val addresses: MutableMap<String, Int> = Hashtable()
    private val commands = CommandList()
    private var freeMemoryLoc = 0
    private var parserDebug = false

    companion object {
        private val LOG_TAG = SimpleParser::class.java.name
    }

    /**
     * Parses instructions asynchronously.
     *
     * Subsequent calls will be queued until the previous call is finished.
     */
    override suspend fun parseInstructions(): ParseResult {
        return withContext(Dispatchers.IO) {
            var vmError: VMError? = null
            try {
                doParse()
            } catch (e: VMError) {
                vmError = e
            }
            ParseResult(vmError, commands)
        }
    }

    /**
     * Parse file for all commands, parameters, variables and symbols
     *
     * @throws VMError when unable to parse for the reason given in the VMError
     */
    @Throws(VMError::class)
    private fun doParse() {
        debug("********** PARSE START **********")
        val instructionsString = parserHelper.getInstructionsString()
        val inputStream = ByteArrayInputStream(instructionsString.toByteArray())

        try {
            debug("********** SYMBOLS START **********")
            getSymbols(inputStream)
            debug("********** SYMBOLS END**********")

            inputStream.reset() // Reset the stream to the beginning

            val buffReader = getBufferedReader(inputStream)
            var line: String? = buffReader.readLine()
            var lineCount = 0
            debug("********** PARSE LINE START **********")
            while (line != null) {
                debug("LINE $lineCount : $line")
                lineCount++
                line = line.trim() // Trim leading/trailing whitespace

                // If line is not a comment or empty
                if (line.isNotEmpty() && !line.startsWith("//")) {
                    parseLine(line)
                }
                line = buffReader.readLine()
            }
            debug("********** PARSE LINE END **********")

        } catch (e: Exception) {
            val vmErrorType = when (e) {
                is IOException -> VMErrorType.VM_ERROR_TYPE_IO
                is IllegalStateException -> VMErrorType.VM_ERROR_TYPE_BAD_UNKNOWN_COMMAND
                else -> VMErrorType.VM_ERROR_TYPE_UNKNOWN
            }

            val additionalExceptionInfo = "[runInstructions] "
            throw VMError(
                additionalExceptionInfo + e.message,
                e,
                vmErrorType
            )
        } finally {
            debug("********** PARSE END **********")
            inputStream.close() // Ensure proper stream closure.
        }
    }

    /**
     * Parse a single line for all the commands, and parameters
     * @param line the line string to be parsed
     */
    private fun parseLine(line: String) {
        val lineWords = line.split("\\s+".toRegex()).filter { it.isNotEmpty() }
        val instructionWord = lineWords[0]

        // If the line does not start with a symbol
        if (!(instructionWord.startsWith("[") && instructionWord.contains("]"))) {
            val commandVal = BaseInstructionSet.INSTRUCTION_SET[instructionWord]
            commandVal?.let {
                debug("  CMD VAL : $commandVal")
                val params = if (lineWords.size > 1) {
                    parseParameters(lineWords.drop(1)) // Pass only parameters
                } else {
                    emptyList()
                }
                commands.add(commandVal, params)
            } ?: run {
                throw IllegalStateException("Instruction $instructionWord is not recognized for line: $line")
            }
        }
    }

    /**
     * Parse for all the parameters for a particular command
     *
     * @param lineWords words on one line
     * @return parameters as list
     */
    private fun parseParameters(lineWords: List<String>): List<Int> {
        val parameters = mutableListOf<Int>()
        for (paramTemp in lineWords) {
            val instrParams =
                paramTemp.split(",".toRegex()).filter { it.isNotEmpty() } // Handle multiple spaces
            for (param in instrParams) {
                val paramVal: Int = when {
                    param.startsWith("[") && param.contains("]") -> {
                        val symbol = param.substring(1, param.indexOf(']'))
                        symbols[symbol]
                            ?.also { debug("  SYMBOL : $symbol($it)") }
                            ?: throw IllegalStateException("Unrecognized symbol $symbol")
                    }

                    param.startsWith("g") -> {
                        val memLoc = addresses.getOrPut(param) { freeMemoryLoc++ }
                        debug("  G-PARAM : $param($memLoc)")
                        memLoc
                    }

                    else -> {
                        debug("  PARAM : $param")
                        param.toInt()
                    }
                }
                parameters.add(paramVal)
            }
        }

        return parameters
    }

    /**
     * Look for lines with symbol definitions (These are lines with "[XYZ]" where XYZ is a String).
     *
     * If its found save the XYZ String and the line number for later use.
     *
     * If when parsing a command during parse, we see a symbol (Parameter with "[XYZ]" where XYZ is a String)
     * we replace that symbol with the line number associated with the matching symbol in the symbol table.
     *
     * @param inputStream the input stream
     */
    private fun getSymbols(inputStream: InputStream) {
        val buffReader = getBufferedReader(inputStream)
        var lineNum = 0
        buffReader.useLines { lines ->
            lines.forEach { line ->
                val trimmedLine = line.trim()
                if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("//")) {
                    if (trimmedLine.startsWith("[") && trimmedLine.contains("]")) {
                        val symbol = trimmedLine.substring(1, trimmedLine.indexOf(']'))
                        debug("--NEW SYM : $symbol($lineNum)")
                        symbols[symbol] = lineNum
                    } else {
                        lineNum++
                    }
                }
            }
        }
    }

    /**
     * @param is input stream
     * @return a BufferedReader
     */
    private fun getBufferedReader(inputStream: InputStream): BufferedReader {
        val inStream = DataInputStream(inputStream)
        return BufferedReader(InputStreamReader(inStream), 8192)
    }

    @Suppress("unused")
    fun setParserDebug(debug: Boolean) {
        parserDebug = debug
    }

    /**
     * Central location for logging/debugging statements
     *
     * @param text text to log
     */
    private fun debug(text: String) {
        if (parserDebug) {
            Log.d(LOG_TAG, text)
        }
    }

}