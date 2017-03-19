package com.slickpath.mobile.android.simple.vm.parser;

import android.support.annotation.NonNull;
import android.util.Log;

import com.slickpath.mobile.android.simple.vm.FileHelper;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.VMErrorType;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
public class SimpleParser extends SimpleParserBase {

    private static final String LOG_TAG = SimpleParser.class.getName();

    private final String instructions;
    private boolean parserDebug = false;

    public SimpleParser(@NonNull final FileHelper fileHelper, final IParserListener listener) {
        super(fileHelper, listener);
        instructions = (String) getFileHelper().getInstructions();
    }

    /**
     * Parse file for all commands, parameters, variables and symbols
     *
     * @throws VMError when unable to parse for the reason given in the VMError
     */
    @Override
    protected boolean doParse() throws VMError {
        InputStream stream = null;
        Exception thrownException = null;
        VMErrorType vmErrorType = VMErrorType.VM_ERROR_TYPE_LAZY_UNSET;
        String additionalExceptionInfo = "[runInstructions] ";

        try {
            stream = new ByteArrayInputStream(instructions.getBytes());
            setSymbols(getSymbolsFromStream(stream));
            stream.close();
            stream = new ByteArrayInputStream(instructions.getBytes());

            final BufferedReader buffReader = getBufferedReader(stream);

            String line = buffReader.readLine();

            final ArrayList<Integer> emptyList = new ArrayList<>(0);

            while (line != null) {
                doParseLine(line, emptyList);
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
        return true;
    }

    /**
     * Look for lines with symbol definitions (These are lines with "[XYZ]" where XYZ is a String).
     * If its found save the XYZ String and the line number for later use.
     * If when parsing a command during parse, we see a symbol (Parameter with "[XYZ]" where XYZ is a String)
     * we replace that symbol with the line number associated with the matching symbol in the symbol table.
     *
     * @param fis - FileInputStream
     */
    private Map<String, Integer> getSymbolsFromStream(@NonNull final InputStream fis) {
        int lineNum = 0;
        Map<String, Integer> symbols = new HashMap<>();
        final BufferedReader buffReader = getBufferedReader(fis);

        String line;
        try {
            line = buffReader.readLine();
            while (line != null) {
                // If line is not a comment
                lineNum = doParseSymbolLine(lineNum, symbols, line);
                line = buffReader.readLine();
            }

        } catch (@NonNull final IOException e) {
            debug("[getSymbolsFromStream] IOException " + "(" + lineNum + ") " + e.getMessage());
        }
        return symbols;
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
