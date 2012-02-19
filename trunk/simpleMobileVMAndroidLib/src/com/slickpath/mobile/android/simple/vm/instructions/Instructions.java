package com.slickpath.mobile.android.simple.vm.instructions;

/**
 * Contains the string and integer definitions for all the instructions
 * 
 * If command value is
 * 		0 - 999 			: No Parameters
 * 		1000 - 1999 		: 1 Parameters
 * 		All other values 	: Undefined
 * 
 * @author Pete Procopio
 *
 */
public interface Instructions {

	public static final int _BEGIN = -1;

	// All commands in the 0-999 range are 0 parameter commands
	// ARITHMATIC
	public static final int _ADD = 0;
	public static final int _SUB = 1;
	public static final int _MUL = 2;
	public static final int _DIV = 3;
	public static final int _NEG = 4;
	// LOGIC
	public static final int _EQUAL = 5;
	public static final int _NOTEQL = 6;
	public static final int _GREATER = 7;
	public static final int _LESS = 8;
	public static final int _GTREQL = 9;
	public static final int _LSSEQL = 10;
	public static final int _NOT = 11;
	// All commands in the 1000-1999 range are 1 parameter commands
	// STACK
	public static final int _PUSHC = 1000;
	public static final int _PUSH = 1001;
	public static final int _POPC = 1002;
	public static final int _POP = 15;
	// PROGRAM FLOW
	public static final int _BRANCH = 1003;
	public static final int _JUMP = 17;
	public static final int _BREQL = 1004;
	public static final int _BRLSS = 1005;
	public static final int _BRGTR = 1006;
	// I/O
	public static final int _RDCHAR = 21;
	public static final int _RDINT = 22;
	public static final int _WRCHAR = 23;
	public static final int _WRINT = 24;
	// MISC
	public static final int _CONTENTS = 25;
	public static final int _HALT = 26;

	public static final String _ADD_STR = "ADD";
	public static final String _SUB_STR = "SUB";
	public static final String _MUL_STR = "MUL";
	public static final String _DIV_STR = "DIV";
	public static final String _NEG_STR = "NEG";
	// LOGIC
	public static final String _EQUAL_STR = "EQUAL";
	public static final String _NOTEQL_STR = "NOTEQL";
	public static final String _GREATER_STR = "GREATER";
	public static final String _LESS_STR = "LESS";
	public static final String _GTREQL_STR = "GTREQL";
	public static final String _LSSEQL_STR = "LSSEQL";
	public static final String _NOT_STR = "NOT";
	// STACK
	public static final String _PUSHC_STR = "PUSHC";
	public static final String _PUSH_STR = "PUSH";
	public static final String _POPC_STR = "POPC";
	public static final String _POP_STR = "POP";
	// PROGRAM FLOW
	public static final String _BRANCH_STR = "BRANCH";
	public static final String _JUMP_STR = "JUMP";
	public static final String _BREQL_STR = "BREQL";
	public static final String _BRLSS_STR = "BRLSS";
	public static final String _BRGTR_STR = "BRGTR";
	// I/O
	public static final String _RDCHAR_STR = "RDCHAR";
	public static final String _RDINT_STR = "RDINT";
	public static final String _WRCHAR_STR = "WRCHAR";
	public static final String _WRINT_STR = "WRINT";
	// MISC
	public static final String _CONTENTS_STR = "CONTENTS";
	public static final String _HALT_STR = "HALT";

}
