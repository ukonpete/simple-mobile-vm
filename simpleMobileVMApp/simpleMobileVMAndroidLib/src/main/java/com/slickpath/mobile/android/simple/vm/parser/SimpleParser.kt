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
     * arse file for all commands, parameters, variables and symbols when finished calls completedParse
     * on the listener which returns any error information and the CommandList
     *
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
        var stream: InputStream? = null
        var thrownException: Exception? = null
        var vmErrorType = VMErrorType.VM_ERROR_TYPE_LAZY_UNSET
        var additionalExceptionInfo = "[runInstructions] "
        try {
            stream = ByteArrayInputStream(parserHelper.getInstructionsString().toByteArray())
            debug("********** SYMBOLS START **********")
            getSymbols(stream)
            debug("********** SYMBOLS END**********")
            stream.close()
            stream = ByteArrayInputStream(parserHelper.getInstructionsString().toByteArray())
            val buffReader = getBufferedReader(stream)
            var line = buffReader.readLine()
            val emptyList = ArrayList<Int>(0)
            var lineCount = 0
            debug("********** PARSE LINE START **********")
            while (line != null) {
                debug("LINE $lineCount : $line")
                lineCount++
                // If line is not a comment
                if (!line.startsWith("//")) {
                    val lineWords = line.split(" ".toRegex()).toTypedArray()
                    val instructionWord = lineWords[0]
                    // If the line does not start with a symbol
                    if (!(instructionWord.startsWith("[") && instructionWord.contains("]"))) {
                        val commandVal = BaseInstructionSet.INSTRUCTION_SET[instructionWord]
                        commandVal?.let {
                            debug("  CMD VAL : $commandVal")
                            val params: List<Int> = if (lineWords.size > 1) {
                                parseParameters(lineWords)
                            } else {
                                // All Empty params point to the same empty List
                                emptyList
                            }
                            commands.add(commandVal, params)
                        } ?: run {
                            throw IllegalStateException("Instruction $instructionWord is not recognized for line: $line")
                        }
                    }
                }
                line = buffReader.readLine()
            }
        } catch (e: Exception) {
            thrownException = e
            vmErrorType = VMErrorType.VM_ERROR_TYPE_UNKNOWN
        } finally {
            debug("********** PARSE LINE END **********")
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    thrownException = e
                    vmErrorType = VMErrorType.VM_ERROR_TYPE_IO
                    additionalExceptionInfo += "stream close "
                }
            }
        }

        if (thrownException != null) {
            debug("********** PARSE END WITH EXCEPTION **********")
            throw VMError(
                additionalExceptionInfo + thrownException.message,
                thrownException,
                vmErrorType
            )
        } else {
            debug("********** PARSE END **********")
        }
    }

    /**
     * Parse for all the parameters for a particular command
     *
     * @param lineWords words on one line
     * @return parameters as list
     */
    private fun parseParameters(lineWords: Array<String>): List<Int> {
        val parameters: MutableList<Int> = ArrayList()
        val params = lineWords[1]
        val instrParams = params.split(",".toRegex()).toTypedArray()
        var paramVal: Int?
        for (paramTemp in instrParams) {
            val param = paramTemp.trim { it <= ' ' }

            // We found a symbol parameter, replace with line number from symbol table
            if (param.startsWith("[") && param.contains("]")) {
                val locEnd = param.indexOf(']')
                val symbol = param.substring(1, locEnd)
                paramVal = symbols[symbol]
                paramVal?.let {
                    debug("  SYMBOL : $symbol($paramVal)")
                } ?: throw IllegalStateException("Unrecognized symbol $symbol")

            } else if (param.startsWith("g")) {
                // Handle Variable
                var memLoc = freeMemoryLoc
                val addressMemLoc = addresses[param]
                if (addressMemLoc != null) {
                    memLoc = addressMemLoc
                } else {
                    addresses[param] = memLoc
                    freeMemoryLoc++
                }
                paramVal = memLoc
                debug("  G-PARAM : $param($paramVal)")
            } else {
                paramVal = param.toInt()
                debug("  PARAM : $param")
            }
            parameters.add(paramVal)
        }
        return parameters
    }

    /**
     * Look for lines with symbol definitions (These are lines with "[XYZ]" where XYZ is a String).
     *
     *
     * If its found save the XYZ String and the line number for later use.
     *
     *
     * If when parsing a command during parse, we see a symbol (Parameter with "[XYZ]" where XYZ is a String)
     * we replace that symbol with the line number associated with the matching symbol in the symbol table.
     *
     * @param fis - FileInputStream
     */
    private fun getSymbols(fis: InputStream) {
        var lineNum = 0
        val buffReader = getBufferedReader(fis)
        var line: String?
        var symbol = ""
        try {
            line = buffReader.readLine()
            while (line != null) {
                // If line is not a comment
                if (!line.startsWith("//")) {
                    if (line.startsWith("[") && line.contains("]")) {
                        val locEnd = line.indexOf(']')
                        symbol = line.substring(1, locEnd)
                        debug("--NEW SYM : $symbol($line)")
                        symbols[symbol] = lineNum
                    } else {
                        lineNum++
                    }
                }
                line = buffReader.readLine()
            }
        } catch (e: IOException) {
            debug("[getSymbols] IOException " + symbol + "(" + lineNum + ") " + e.message)
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