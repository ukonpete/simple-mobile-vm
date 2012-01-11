/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.instructions;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @author PJ
 *
 */
public class BaseInstructionSet implements Instructions{

	private static final int DEFAULT_INSTRUCTION_SIZE = 30;
	/**
	 * Lookup table for Command ID (Integer) by Command Name (String)
	 */
	public static final Map<String, Integer> INSTRUCTION_SET_HT = new Hashtable<String, Integer>(DEFAULT_INSTRUCTION_SIZE);
	/**
	 * Lookup table for Command Name (String) by Command ID (Integer)
	 */
	public static final Map<Integer, String> INSTRUCTION_SET_CONV_HT = new Hashtable<Integer, String>(DEFAULT_INSTRUCTION_SIZE);

	static
	{
		//
		INSTRUCTION_SET_HT.put(_ADD_STR, _ADD);
		INSTRUCTION_SET_HT.put(_SUB_STR, _SUB);
		INSTRUCTION_SET_HT.put(_MUL_STR, _MUL);
		INSTRUCTION_SET_HT.put(_DIV_STR, _DIV);
		INSTRUCTION_SET_HT.put(_NEG_STR, _NEG);
		//
		INSTRUCTION_SET_HT.put(_EQUAL_STR, _EQUAL);
		INSTRUCTION_SET_HT.put(_NOTEQL_STR, _NOTEQL);
		INSTRUCTION_SET_HT.put(_GREATER_STR, _GREATER);
		INSTRUCTION_SET_HT.put(_LESS_STR, _LESS);
		INSTRUCTION_SET_HT.put(_GTREQL_STR, _GTREQL);
		INSTRUCTION_SET_HT.put(_LSSEQL_STR, _LSSEQL);
		INSTRUCTION_SET_HT.put(_NOT_STR, _NOT);
		//
		INSTRUCTION_SET_HT.put(_PUSHC_STR, _PUSHC);
		INSTRUCTION_SET_HT.put(_PUSH_STR, _PUSH);
		INSTRUCTION_SET_HT.put(_POPC_STR, _POPC);
		INSTRUCTION_SET_HT.put(_POP_STR, _POP);
		//
		INSTRUCTION_SET_HT.put(_BRANCH_STR, _BRANCH);
		INSTRUCTION_SET_HT.put(_JUMP_STR, _JUMP);
		INSTRUCTION_SET_HT.put(_BREQL_STR, _BREQL);
		INSTRUCTION_SET_HT.put(_BRLSS_STR, _BRLSS);
		INSTRUCTION_SET_HT.put(_BRGTR_STR, _BRGTR);
		//
		INSTRUCTION_SET_HT.put(_RDCHAR_STR, _RDCHAR);
		INSTRUCTION_SET_HT.put(_RDINT_STR, _RDINT);
		INSTRUCTION_SET_HT.put(_WRCHAR_STR, _WRCHAR);
		INSTRUCTION_SET_HT.put(_WRINT_STR, _WRINT);
		//
		INSTRUCTION_SET_HT.put(_CONTENTS_STR, _CONTENTS);
		INSTRUCTION_SET_HT.put(_HALT_STR, _HALT);

		final Set<String> keys = INSTRUCTION_SET_HT.keySet();
		for (final String sName : keys)
		{
			INSTRUCTION_SET_CONV_HT.put(INSTRUCTION_SET_HT.get(sName), sName);
		}
	}

}
