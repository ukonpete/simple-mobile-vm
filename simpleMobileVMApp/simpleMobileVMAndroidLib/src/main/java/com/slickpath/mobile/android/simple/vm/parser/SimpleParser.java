package com.slickpath.mobile.android.simple.vm.parser;

import androidx.annotation.NonNull;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.FileHelper;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.VMErrorType;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Parses file for commands, parameters, variables and symbols
 * <p>
 * Possible line combinations:
 * <ul>
 * <li>COMMAND
 * <li>COMMAND PARAMETER
 * <li>COMMAND VAR
 * <li>COMMAND SYMBOL
 * <li>SYMBOL
 * <li>COMMENT
 * </ul>
 * <p>
 * Definitions:
 * <ul>
 * <li>COMMAND - string
 * <li>PARAMETER - string (typically an integer, but can be VAR OR SYMBOL)
 * <li>VAR - String starting with 'g'  - Variable memory location reference
 * <li>SYMBOL - [string] - NOTE: if a SYMBOL is used in a parameter it is a reference to an existing SYMBOL, that SYMBOL represents the line it is defined on. It MUST be defined somewhere on a line by itself
 * <li>COMMENT - Ignore this line during parsing - user wants to leave a compiler ignored note at this line
 * </ul>
 * <p>
 * Examples:
 * <ul>
 * <li>COMMAND - ADD  (Adds the top two items on the stack and places result on the stack)
 * <li>COMMAND PARAMETER - PUSHC 100 (Pushes the value 100 onto the top of the stack)
 * <li>COMMAND VAR - POPC g1 - (Pop the top value of the stack and place into the memory location represented by g1
 * <li>COMMAND SYMBOL - JUMP [LOOP2] - jump to the line number represented by [LOOP2]
 * <li>SYMBOL - [LOOP2] - assign the next instructions line number to the symbol [LOOP2]
 * <li>COMMENT - // This code rocks
 * </ul>
 * <p>
 * @author Pete Procopio
 * @see <a href="https://github.com/ukonpete/simple-mobile-vm/wiki" target="_blank">Simple VM Wiki on Github</a>
 */
public class SimpleParser {

    private static final String LOG_TAG = SimpleParser.class.getName();

    private static final ThreadPoolExecutor executorPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    private final IParserListener _parserListener;

    private final String instructions;
    private final Map<String, Integer> _symbols = new Hashtable<>();
    private final Map<String, Integer> _addresses = new Hashtable<>();
    private final CommandList commands = new CommandList();
    private int freeMemoryLoc = 0;
    private boolean parserDebug = false;

    public SimpleParser(@NonNull final FileHelper fileHelper, final IParserListener listener) {
        instructions = fileHelper.getInstructionsString();
        _parserListener = listener;
    }

    /**
     * arse file for all commands, parameters, variables and symbols when finished calls completedParse
     * on the listener which returns any error information and the CommandList
     * <p></p>
     * Subsequent calls will be queued until the previous call is finished.
     */
    public void parse() {
        executorPool.execute(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    VMError vmError = null;
                    try {
                        doParse();
                    } catch (@NonNull final VMError e) {
                        vmError = e;
                    }
                    if (_parserListener != null) {
                        _parserListener.completedParse(vmError, commands);
                    }
                }
            }
        });
    }

    /**
     * Parse file for all commands, parameters, variables and symbols
     *
     * @throws VMError when unable to parse for the reason given in the VMError
     */
    private void doParse() throws VMError {
        InputStream stream = null;
        Exception thrownException = null;
        VMErrorType vmErrorType = VMErrorType.VM_ERROR_TYPE_LAZY_UNSET;
        String additionalExceptionInfo = "[runInstructions] ";

        try {
            stream = new ByteArrayInputStream(instructions.getBytes());
            getSymbols(stream);
            stream.close();
            stream = new ByteArrayInputStream(instructions.getBytes());

            final BufferedReader buffReader = getBufferedReader(stream);

            String line = buffReader.readLine();

            final ArrayList<Integer> emptyList = new ArrayList<>(0);

            while (line != null) {
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
                line = buffReader.readLine();
            }
        } catch (@NonNull final Exception e) {
            thrownException = e;
            vmErrorType = VMErrorType.VM_ERROR_TYPE_UNKNOWN;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (@NonNull final IOException e) {
                    thrownException = e;
                    vmErrorType = VMErrorType.VM_ERROR_TYPE_IO;
                    additionalExceptionInfo += "stream close ";
                }
            }
        }
        if(thrownException!=null) {
            throw new VMError(additionalExceptionInfo + thrownException.getMessage(), thrownException, vmErrorType);
        }
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

    /**
     * Look for lines with symbol definitions (These are lines with "[XYZ]" where XYZ is a String).<p>
     * If its found save the XYZ String and the line number for later use.<p>
     * If when parsing a command during parse, we see a symbol (Parameter with "[XYZ]" where XYZ is a String)
     * we replace that symbol with the line number associated with the matching symbol in the symbol table.
     *
     * @param fis - FileInputStream
     */
    private void getSymbols(@NonNull final InputStream fis) {
        int lineNum = 0;

        final BufferedReader buffReader = getBufferedReader(fis);

        String line;
        String symbol = "";
        try {
            line = buffReader.readLine();
            while (line != null) {
                // If line is not a comment
                if (!line.startsWith("//")) {
                    if (line.startsWith("[") && line.contains("]")) {
                        final int locEnd = line.indexOf(']');
                        symbol = line.substring(1, locEnd);
                        debug("--NEW SYM : " + symbol + "(" + line + ")");
                        _symbols.put(symbol, lineNum);
                    } else {
                        lineNum++;
                    }
                }
                line = buffReader.readLine();
            }

        } catch (@NonNull final IOException e) {
            debug("[getSymbols] IOException " + symbol + "(" + lineNum + ") " + e.getMessage());
        }

    }

    /**
     * @param is input stream
     * @return a BufferedReader
     */
    @NonNull
    private BufferedReader getBufferedReader(@NonNull final InputStream is) {
        final DataInputStream inStream = new DataInputStream(is);
        return new BufferedReader(new InputStreamReader(inStream), 8192);
    }

    @SuppressWarnings("unused")
    public void setParserDebug(boolean debug) {
         parserDebug = debug;
    }
    /**
     * Central location for logging/debugging statements
     *
     * @param text text to log
     */
    private void debug(final String text) {
        if (parserDebug) {
            Log.d(LOG_TAG, text);
        }
    }
}
