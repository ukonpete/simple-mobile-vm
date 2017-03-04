package com.slickpath.mobile.android.simple.vm.parser;

import android.support.annotation.NonNull;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.BuildConfig;
import com.slickpath.mobile.android.simple.vm.FileHelper;
import com.slickpath.mobile.android.simple.vm.VMError;
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
 * VAR - string starting with 'g'  - Variable memory location reference
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
public class SimpleParser {

    private static final String TAG = SimpleParser.class.getName();

    private final IParserListener _parserListener;

    private final String instructions;
    private final Map<String, Integer> _symbols = new Hashtable<>();
    private final Map<String, Integer> _addresses = new Hashtable<>();
    private final CommandList _commands = new CommandList();
    private int _freeMemoryLoc = 0;

    public SimpleParser(@NonNull final FileHelper fileHelper, final IParserListener listener) {
        instructions = fileHelper.getInstructionsString();
        _parserListener = listener;
    }

    /**
     * Launches thread to Parse file for all commands, parameters, variables and symbols
     * When finished calls completedParse on the listener which returns any error information and the CommandList
     * Synchronized
     */
    public void parse() {
        new Thread(new Runnable() {
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
                        _parserListener.completedParse(vmError, _commands);
                    }
                }
            }
        }).start();
    }

    /**
     * Parse file for all commands, parameters, variables and symbols
     *
     * @throws VMError when unable to parse for the reason given in the VMError
     */
    private void doParse() throws VMError {
        InputStream stream = null;
        try {
            stream = new ByteArrayInputStream(instructions.getBytes());
            getSymbols(stream);
            stream.close();
            stream = new ByteArrayInputStream(instructions.getBytes());

            final BufferedReader buffReader = getBufferedReader(stream);

            String sLine = buffReader.readLine();

            final ArrayList<Integer> emptyList = new ArrayList<>(0);

            while (sLine != null) {
                debug("LINE : " + sLine);

                // If line is not a comment
                if (!sLine.startsWith("//")) {
                    debug("-Line " + sLine);
                    final String[] lineWords = sLine.split(" ");
                    final String sInstruction = lineWords[0];

                    debug("-RAW " + sInstruction);
                    // If the line does not start with a symbol
                    if (!(sInstruction.startsWith("[") && sInstruction.contains("]"))) {
                        final int commandVal = BaseInstructionSet.INSTRUCTION_SET_HT.get(sInstruction);
                        debug("INST : " + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(commandVal) + "(" + commandVal + ")");

                        List<Integer> params;
                        if (lineWords.length > 1) {
                            params = parseParameters(lineWords);
                        } else {
                            // All Empty params point to the same empty List
                            params = emptyList;
                        }
                        _commands.add(commandVal, params);
                    }
                }
                sLine = buffReader.readLine();
            }
        } catch (@NonNull final Exception e) {
            throw new VMError("[runInstructions]" + e.getMessage(), e, VMError.VM_ERROR_TYPE_UNKOWN);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (@NonNull final IOException e) {
                    throw new VMError("[runInstructions] finally " + e.getMessage(), e, VMError.VM_ERROR_TYPE_IO);
                }
            }
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
        final String sParams = lineWords[1];
        final String[] InstrParams = sParams.split(",");
        int paramVal;

        for (final String sParamTemp : InstrParams) {
            final String sParam = sParamTemp.trim();

            // We found a symbol parameter, replace with line number from symbol table
            if (sParam.startsWith("[") && sParam.contains("]")) {
                final int locEnd = sParam.indexOf(']');
                final String sSymbol = sParam.substring(1, locEnd);

                paramVal = _symbols.get(sSymbol);
                debug("  SYMBOL : " + sSymbol + "(" + paramVal + ")");
            } else if (sParam.startsWith("g")) {
                // Handle Variable
                int memLoc = _freeMemoryLoc;
                if (_addresses.containsKey(sParam)) {
                    memLoc = _addresses.get(sParam);
                } else {
                    _addresses.put(sParam, memLoc);
                    _freeMemoryLoc++;
                }
                paramVal = memLoc;
                debug("  G-PARAM : " + sParam + "(" + paramVal + ")");
            } else {
                paramVal = Integer.parseInt(sParam);
                debug("  PARAM : " + sParam);
            }
            parameters.add(paramVal);
        }
        return parameters;
    }

    /**
     * Look for lines with symbol definitions (These are lines with "[XYZ]" where XYZ is a String).
     * If its found save the XYZ String and the line number for later use.
     * If when parsing a command during parse, we see a symbol (Parameter with "[XYZ]" where XYZ is a String)
     * we replace that symbol with the line number associated with the matching symbol in the symbol table.
     *
     * @param fis - FileInputStream
     */
    private void getSymbols(@NonNull final InputStream fis) {
        int line = 0;

        final BufferedReader buffReader = getBufferedReader(fis);

        String sLine;
        String sSymbol = "";
        try {
            sLine = buffReader.readLine();
            while (sLine != null) {
                // If line is not a comment
                if (!sLine.startsWith("//")) {
                    if (sLine.startsWith("[") && sLine.contains("]")) {
                        final int locEnd = sLine.indexOf(']');
                        sSymbol = sLine.substring(1, locEnd);
                        debug("--NEW SYM : " + sSymbol + "(" + line + ")");
                        _symbols.put(sSymbol, line);
                    } else {
                        line++;
                    }
                }
                sLine = buffReader.readLine();
            }

        } catch (@NonNull final IOException e) {
            debug("[getSymbols] IOException " + sSymbol + "(" + line + ") " + e.getMessage());
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

    /**
     * Central location for logging / debugging statements
     *
     * @param sText text to log
     */
    private void debug(final String sText) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, sText);
        }
    }
}
