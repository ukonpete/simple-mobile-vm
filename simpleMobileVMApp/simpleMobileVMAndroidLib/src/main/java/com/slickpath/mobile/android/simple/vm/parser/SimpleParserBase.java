package com.slickpath.mobile.android.simple.vm.parser;

import android.support.annotation.NonNull;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.FileHelper;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Parses file for commands, parameters, variables and symbols
 * <p>
 * Possible line combinations
 * COMMAND
 * COMMAND PARAMETER
 * COMMAND VAR
 * COMMAND SYMBOL
 * SYMBOL
 * COMMENT
 * <p>
 * Definitions:
 * COMMAND - string
 * PARAMETER - string (typically an integer, but can be VAR OR SYMBOL)
 * VAR - String starting with 'g'  - Variable memory location reference
 * SYMBOL - [string] - note if a SYMBOL is used in a parameter it is a reference to an existing SYMBOL, that SYMBOL represents the line it is defined on. It MUST be defined somewhere on a line by itself
 * COMMENT - Ignore this line during parsing - user wants to leave a compiler ignored note at this line
 * <p>
 * Examples:
 * COMMAND - ADD  (Adds the top two items on the stack and places result on the stack)
 * COMMAND PARAMETER - PUSHC 100 (Pushes the value 100 onto the top of the stack)
 * COMMAND VAR - POPC g1 - (Pop the top value of the stack and place into the memory location represented by g1
 * COMMAND SYMBOL - JUMP [LOOP2] - jump to the line number represented by [LOOP2]
 * SYMBOL - [LOOP2] - assign this line number (next instruction in code) to the symbol [LOOP2]
 * COMMENT - // This code rocks
 * <p>
 * See "Simple VM 1.0 User Manual.doc" for more details
 *
 * @author Pete Procopio
 */
abstract class SimpleParserBase {

    private static final String LOG_TAG = SimpleParserBase.class.getName();

    private static final ThreadPoolExecutor executorPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    private final Map<String, Integer> _addresses = new Hashtable<>();
    private final CommandList commands = new CommandList();
    private int freeMemoryLoc = 0;
    private boolean parserDebug = false;
    private Map<String, Integer> _symbols = new Hashtable<>();
    private final IParserListener _parserListener;
    private FileHelper fileHelper;


    SimpleParserBase(@NonNull final FileHelper helper, final IParserListener listener) {
        fileHelper = helper;
        _parserListener = listener;
    }

    FileHelper getFileHelper() {
        return fileHelper;
    }

    /**
     * arse file for all commands, parameters, variables and symbols when finished calls completedParse
     * on the listener which returns any error information and the CommandList
     *
     * Subsequent calls will be queued until the previous call is finished.
     */
    public void parse() {
        executorPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    VMError vmError = null;
                    boolean done = true;
                    try {
                        done = doParse();
                    } catch (@NonNull final VMError e) {
                        vmError = e;
                    }
                    if (_parserListener != null && done) {
                        _parserListener.completedParse(vmError, getCommands());
                    }
                }
            }
        });
    }

    public IParserListener getParserListener() {
        return _parserListener;
    }

    /**
     * Parse file for all commands, parameters, variables and symbols
     *
     * @throws VMError when unable to parse for the reason given in the VMError
     */
    protected abstract boolean doParse() throws VMError;

    void doParseLine(String line, ArrayList<Integer> emptyList) {
        debug("LINE : " + line);
        // If line is not a comment
        if (!line.startsWith("//")) {
            debug("-Line " + line);
            final String[] lineWords = line.split(" ");
            final String instructionWord = lineWords[0];

            debug("-RAW " + instructionWord);
            // If the line does not start with a symbol
            if (!(instructionWord.startsWith("[") && instructionWord.contains("]"))) {
                final int commandVal = BaseInstructionSet.INSTRUCTION_SET_HT.get(instructionWord);
                debug("INST : " + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(commandVal) + "(" + commandVal + ")");

                List<Integer> params;
                if (lineWords.length > 1) {
                    params = parseParameters(lineWords);
                } else {
                    // All Empty params point to the same empty List
                    params = emptyList;
                }
                commands.add(commandVal, params);
            }
        }
    }

    void setSymbols(@NonNull Map<String, Integer> symbols) {
        _symbols = symbols;
    }

    protected CommandList getCommands() {
        return commands;
    }
    /**
     * Parse for all the parameters for a particular command
     *
     * @param lineWords words on one line
     * @return parameters as list
     */
    @NonNull
    private List<Integer> parseParameters(final String[] lineWords) {
        final List<Integer> parameters = new ArrayList<>();
        final String params = lineWords[1];
        final String[] InstrParams = params.split(",");
        int paramVal;

        for (final String paramTemp : InstrParams) {
            final String param = paramTemp.trim();

            // We found a symbol parameter, replace with line number from symbol table
            if (param.startsWith("[") && param.contains("]")) {
                final int locEnd = param.indexOf(']');
                final String symbol = param.substring(1, locEnd);

                paramVal = _symbols.get(symbol);
                debug("  SYMBOL : " + symbol + "(" + paramVal + ")");
            } else if (param.startsWith("g")) {
                // Handle Variable
                int memLoc = freeMemoryLoc;
                if (_addresses.containsKey(param)) {
                    memLoc = _addresses.get(param);
                } else {
                    _addresses.put(param, memLoc);
                    freeMemoryLoc++;
                }
                paramVal = memLoc;
                debug("  G-PARAM : " + param + "(" + paramVal + ")");
            } else {
                paramVal = Integer.parseInt(param);
                debug("  PARAM : " + param);
            }
            parameters.add(paramVal);
        }
        return parameters;
    }

    int doParseSymbolLine(int lineNum, Map<String, Integer> symbols, String line) {
        String symbol;
        if (!line.startsWith("//")) {
            if (line.startsWith("[") && line.contains("]")) {
                final int locEnd = line.indexOf(']');
                symbol = line.substring(1, locEnd);
                debug("--NEW SYM : " + symbol + "(" + line + ")");
                symbols.put(symbol, lineNum);
            } else {
                lineNum++;
            }
        }
        return lineNum;
    }

    @SuppressWarnings("unused")
    public void setParserDebug(boolean debug) {
         parserDebug = debug;
    }

    /**
     * Central location for logging / debugging statements
     *
     * @param text text to log
     */
    private void debug(final String text) {
        if (parserDebug) {
            Log.d(LOG_TAG, text);
        }
    }
}
