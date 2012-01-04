/**
 * 
 */
package com.slickpath.mobile.android.simple.vm;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;



/**
 * @author PJ
 *
 */
public class SimpleParser {

    private String _sInstructionFile;

    private Hashtable<String, Integer> _htSymbols = new Hashtable<String, Integer>();

    private Hashtable<String, Integer> _htAddresses = new Hashtable<String, Integer>();

    private int _freeMemoryLoc = 0;

    public SimpleParser(String sFile)
    {
        _sInstructionFile = sFile;
        BaseInstructionSet bis = new BaseInstructionSet();
    }

    public List<Integer> parse(List<List<Integer>> allParameters) throws VMError
    {
    	List<Integer> instructions = new ArrayList<Integer>();
        FileInputStream fis = null;

        try
        {
            fis = new FileInputStream(_sInstructionFile);
            getSymbols(fis);
            fis.close();
            fis = new FileInputStream(_sInstructionFile);

            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in), 8192 );
            
            String sLine = br.readLine();

            while (sLine != null)
            {
                // $$$$ System.out.println("LINE : " + sLine);
            	
            	// If line is not a comment
                if (!sLine.startsWith("//"))
                {
                    //char[] delimiters = new char[] { ' ' };
                    String[] words = sLine.split(" ");
                    //String[] words = sLine.Split(delimiters, StringSplitOptions.RemoveEmptyEntries);

                    //long wordVal  = words[0];
                    String sInstruction = words[0];

                    // If the line does not start with a symbol
                    if (!(sInstruction.startsWith("[") && sInstruction.contains("]")))
                    {
                        int commandVal = (int)BaseInstructionSet.INSTRUCTION_SET_HT.get(sInstruction);
                        instructions.add(commandVal);
                        List<Integer> parameters = new ArrayList<Integer>();
                        if (words.length > 1)
                        {
                            String sParams = words[1];
                            // char[] paramDelimiters = new char[] { ',' };
                            String[] InstrParams = sParams.split(",");
                            //String[] InstrParams = sParams.Split(paramDelimiters, StringSplitOptions.RemoveEmptyEntries);

                            int paramVal = -1;

                            for (String sParamTemp : InstrParams)
                            {
                                String sParam = sParamTemp.trim();
                                //String sParam = words[1];

                                // We found a symbol parameter, replace with line number from symbol table
                                if (sParam.startsWith("[") && sParam.contains("]"))
                                {
                                    int locEnd = sParam.indexOf("]");
                                    String sSymbol = sParam.substring(1, locEnd - 1);

                                    System.out.println("SYMBOL : " + sSymbol);
                                    paramVal = _htSymbols.get(sSymbol);
                                }
                                else if (sParam.startsWith("g"))
                                {
                                    int memLoc = _freeMemoryLoc;
                                    if (_htAddresses.containsKey(sParam))
                                    {
                                        memLoc = (Integer)_htAddresses.get(sParam);
                                    }
                                    else
                                    {
                                        _htAddresses.put(sParam, memLoc);
                                        _freeMemoryLoc++;
                                    }
                                    paramVal = memLoc;
                                }
                                else
                                {
                                    paramVal = Integer.parseInt(sParam);
                                }
                                parameters.add(paramVal);
                            }
                        }
                        allParameters.add(parameters);
                    }
                }
                sLine = br.readLine();
            }
        }
        catch(Exception e)
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        return instructions;
    }

    /**
     * Look for lines with symbol definitions (These are lines with [XXX] where XXX is a String).
     * If its found save the XXX String and the line number for later use.
     * If when parsing a command during parse, we see a symbol (Parameter with [XXX] where XXX is a String)
     * we replace that symbol with the line number associated with the matching symbol in the symbol table.
     */
    public void getSymbols(FileInputStream fis)
    {
        int line = 0;
        
        DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in), 8192 );
        
        String sLine;
		try {
			sLine = br.readLine();
            while (sLine != null)
            {
                // If line is not a comment
                if (!sLine.startsWith("//"))
                {
                    if (sLine.startsWith("[") && sLine.contains("]"))
                    {
                        int locEnd = sLine.indexOf("]");
                        String sSymbol = sLine.substring(1, locEnd - 1);
                        _htSymbols.put(sSymbol, line);
                    }
                    else
                    {
                        line++;
                    }
                }
                sLine = br.readLine();
            }
        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
