@file:Suppress("KDocUnresolvedReference")

package com.slickpath.mobile.android.simple.vm.parser

import android.util.Log
import com.slickpath.mobile.android.simple.vm.FileHelper
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.VMErrorType
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet
import com.slickpath.mobile.android.simple.vm.util.CommandList
import java.io.*
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

/**
 * Parses file for commands, parameters, variables and symbols
 *
 *
 * Possible line combinations:
 *
 *  * COMMAND
 *  * COMMAND PARAMETER
 *  * COMMAND VAR
 *  * COMMAND SYMBOL
 *  * SYMBOL
 *  * COMMENT
 *
 *
 *
 * Definitions:
 *
 *  * COMMAND - string
 *  * PARAMETER - string (typically an integer, but can be VAR OR SYMBOL)
 *  * VAR - String starting with 'g'  - Variable memory location reference
 *  * SYMBOL - [string] - NOTE: if a SYMBOL is used in a parameter it is a reference to an existing SYMBOL, that SYMBOL represents the line it is defined on. It MUST be defined somewhere on a line by itself
 *  * COMMENT - Ignore this line during parsing - user wants to leave a compiler ignored note at this line
 *
 *
 *
 * Examples:
 *
 *  * COMMAND - ADD  (Adds the top two items on the stack and places result on the stack)
 *  * COMMAND PARAMETER - PUSHC 100 (Pushes the value 100 onto the top of the stack)
 *  * COMMAND VAR - POPC g1 - (Pop the top value of the stack and place into the memory location represented by g1
 *  * COMMAND SYMBOL - JUMP [LOOP2] - jump to the line number represented by [LOOP2]
 *  * SYMBOL - [LOOP2] - assign the next instructions line number to the symbol [LOOP2]
 *  * COMMENT - // This code rocks
 *
 *
 *
 * @author Pete Procopio
 * @see [Simple VM Wiki on Github](https://github.com/ukonpete/simple-mobile-vm/wiki)
 */
class SimpleParser(fileHelper: FileHelper, private val listener: ParserListener?) {
    private val instructions: String = fileHelper.instructionsString
    private val symbols: MutableMap<String, Int> = Hashtable()
    private val addresses: MutableMap<String, Int> = Hashtable()
    private val commands = CommandList()
    private var freeMemoryLoc = 0
    private var parserDebug = false

    companion object {
        private val LOG_TAG = SimpleParser::class.java.name
        private val executorPool = Executors.newCachedThreadPool() as ThreadPoolExecutor
    }

    /**
     * arse file for all commands, parameters, variables and symbols when finished calls completedParse
     * on the listener which returns any error information and the CommandList
     *
     *
     * Subsequent calls will be queued until the previous call is finished.
     */
    fun parse() {
        executorPool.execute(object : Runnable {
            override fun run() {
                synchronized(this) {
                    var vmError: VMError? = null
                    try {
                        doParse()
                    } catch (e: VMError) {
                        vmError = e
                    }
                    listener?.completedParse(vmError, commands)
                }
            }
        })
    }

    /**
     * Parse file for all commands, parameters, variables and symbols
     *
     * @throws VMError when unable to parse for the reason given in the VMError
     */
    @Throws(VMError::class)
    private fun doParse() {
        var stream: InputStream? = null
        var thrownException: Exception? = null
        var vmErrorType = VMErrorType.VM_ERROR_TYPE_LAZY_UNSET
        var additionalExceptionInfo = "[runInstructions] "
        try {
            stream = ByteArrayInputStream(instructions.toByteArray())
            getSymbols(stream)
            stream.close()
            stream = ByteArrayInputStream(instructions.toByteArray())
            val buffReader = getBufferedReader(stream)
            var line = buffReader.readLine()
            val emptyList = ArrayList<Int>(0)
            while (line != null) {
                debug("LINE : $line")

                // If line is not a comment
                if (!line.startsWith("//")) {
                    debug("-Line $line")
                    val lineWords = line.split(" ".toRegex()).toTypedArray()
                    val instructionWord = lineWords[0]
                    debug("-RAW $instructionWord")
                    // If the line does not start with a symbol
                    if (!(instructionWord.startsWith("[") && instructionWord.contains("]"))) {
                        val commandVal = BaseInstructionSet.INSTRUCTION_SET[instructionWord]
                        commandVal?.let {
                            debug("INST : " + BaseInstructionSet.INSTRUCTION_SET_CONV[commandVal] + "(" + commandVal + ")")
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
            throw VMError(additionalExceptionInfo + thrownException.message, thrownException, vmErrorType)
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
                } ?: throw IllegalStateException("Unrecognized symbol $symbol"  )

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
    private fun getBufferedReader(`is`: InputStream): BufferedReader {
        val inStream = DataInputStream(`is`)
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