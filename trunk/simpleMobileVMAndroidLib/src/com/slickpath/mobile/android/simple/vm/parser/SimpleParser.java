/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.parser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.util.Log;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.util.CommandSet;

/**
 * @author PJ
 *
 */
public class SimpleParser {

	private static final String TAG = SimpleParser.class.getName();

	private ParserListener _parserListener;

	private final String _sInstructionFile;
	private final Hashtable<String, Integer> _htSymbols = new Hashtable<String, Integer>();
	private final Hashtable<String, Integer> _htAddresses = new Hashtable<String, Integer>();
	private final CommandSet _commandSet = new CommandSet();
	private int _freeMemoryLoc = 0;

	protected boolean _bDebug = false;

	public SimpleParser(final String sFile)
	{
		_sInstructionFile = sFile;
		try {
			Class.forName("BaseInstructionSet");
		} catch (final ClassNotFoundException e) {
			debug(e.getMessage());
		}
	}

	public void setListener(final ParserListener listener)
	{
		_parserListener = listener;
	}

	public void parse()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				VMError vmError = null;
				try {
					_parse();
				} catch (final VMError e) {
					vmError = e;
				}
				if ( _parserListener != null)
				{
					_parserListener.completedParse(vmError, _commandSet);
				}
			}
		}).start();
	}

	public CommandSet getCommandSet()
	{
		return _commandSet;
	}

	private void _parse() throws VMError
	{
		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(_sInstructionFile);
			getSymbols(fis);
			fis.close();
			fis = new FileInputStream(_sInstructionFile);

			final DataInputStream in = new DataInputStream(fis);
			final BufferedReader br = new BufferedReader(new InputStreamReader(in), 8192 );

			String sLine = br.readLine();

			while (sLine != null)
			{
				debug("LINE : " + sLine);

				// If line is not a comment
				if (!sLine.startsWith("//"))
				{
					debug("-Line " + sLine);
					final String[] words = sLine.split(" ");

					final String sInstruction = words[0];

					debug("-RAW " + sInstruction);
					// If the line does not start with a symbol
					if (!(sInstruction.startsWith("[") && sInstruction.contains("]")))
					{
						final int commandVal = BaseInstructionSet.INSTRUCTION_SET_HT.get(sInstruction);
						debug("INST : " + BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(commandVal) + "(" + commandVal + ")");
						final List<Integer> parameters = new ArrayList<Integer>();
						if (words.length > 1)
						{
							final String sParams = words[1];
							final String[] InstrParams = sParams.split(",");
							int paramVal = -1;

							for (final String sParamTemp : InstrParams)
							{
								final String sParam = sParamTemp.trim();

								// We found a symbol parameter, replace with line number from symbol table
								if (sParam.startsWith("[") && sParam.contains("]"))
								{
									final int locEnd = sParam.indexOf("]");
									final String sSymbol = sParam.substring(1, locEnd);

									paramVal = _htSymbols.get(sSymbol);
									debug("  SYMBOL : " + sSymbol + "(" + paramVal + ")");
								}
								else if (sParam.startsWith("g"))
								{
									int memLoc = _freeMemoryLoc;
									if (_htAddresses.containsKey(sParam))
									{
										memLoc = _htAddresses.get(sParam);
									}
									else
									{
										_htAddresses.put(sParam, memLoc);
										_freeMemoryLoc++;
									}
									paramVal = memLoc;
									debug("  G-PARAM : " + sParam + "(" + paramVal + ")");
								}
								else
								{
									paramVal = Integer.parseInt(sParam);
									debug("  PARAM : " + sParam);
								}
								parameters.add(paramVal);
							}
						}
						_commandSet.addCommand(commandVal, parameters);
					}
				}
				sLine = br.readLine();
			}
		}
		catch(final Exception e)
		{
			e.printStackTrace();
			throw new VMError("[runInstructions]" + e.getMessage(), e, VMError.VM_ERROR_TYPE_UNKOWN);
		}
		finally
		{
			if (fis != null )
			{
				try {
					fis.close();
				} catch (final IOException e) {
					throw new VMError("[runInstructions] finally " + e.getMessage(), e, VMError.VM_ERROR_TYPE_IO);
				}
			}
		}
	}

	/**
	 * Look for lines with symbol definitions (These are lines with "[XYZ]" where XYZ is a String).
	 * If its found save the XYZ String and the line number for later use.
	 * If when parsing a command during parse, we see a symbol (Parameter with "[XYZ]" where XYZ is a String)
	 * we replace that symbol with the line number associated with the matching symbol in the symbol table.
	 */
	public void getSymbols(final FileInputStream fis)
	{
		int line = 0;

		final DataInputStream in = new DataInputStream(fis);
		final BufferedReader br = new BufferedReader(new InputStreamReader(in), 8192 );

		String sLine = "";
		String sSymbol = "";
		try {
			sLine = br.readLine();
			while (sLine != null)
			{
				// If line is not a comment
				if (!sLine.startsWith("//"))
				{
					if (sLine.startsWith("[") && sLine.contains("]"))
					{
						final int locEnd = sLine.indexOf("]");
						sSymbol = sLine.substring(1, locEnd);
						debug("--NEW SYM : " + sSymbol + "(" + line + ")");
						_htSymbols.put(sSymbol, line);
					}
					else
					{
						line++;
					}
				}
				sLine = br.readLine();
			}

		} catch (final IOException e) {
			debug("[getSymbols] IOException " + sSymbol + "(" + line + ")");
			e.printStackTrace();
		}

	}

	protected void debug(final String sText)
	{
		if ( _bDebug )
		{
			Log.d(TAG, sText);
		}
	}
}
