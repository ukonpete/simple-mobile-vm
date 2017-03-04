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

	int _BEGIN = -1;

	// All commands in the 0-999 range are 0 parameter commands
	// ARITHMATIC
	int _ADD = 0;
	int _SUB = 1;
	int _MUL = 2;
	int _DIV = 3;
	int _NEG = 4;
	// LOGIC
	int _EQUAL = 5;
	int _NOTEQL = 6;
	int _GREATER = 7;
	int _LESS = 8;
	int _GTREQL = 9;
	int _LSSEQL = 10;
	int _NOT = 11;
	// All commands in the 1000-1999 range are 1 parameter commands
	// STACK
	int _PUSHC = 1000;
	int _PUSH = 1001;
	int _POPC = 1002;
	int _POP = 15;
	// PROGRAM FLOW
	int _BRANCH = 1003;
	int _JUMP = 17;
	int _BREQL = 1004;
	int _BRLSS = 1005;
	int _BRGTR = 1006;
	// I/O
	int _RDCHAR = 21;
	int _RDINT = 22;
	int _WRCHAR = 23;
	int _WRINT = 24;
	// MISC
	int _CONTENTS = 25;
	int _HALT = 26;

	int NUM_COMMANDS = _HALT + 1;

	String _ADD_STR = "ADD";
	String _SUB_STR = "SUB";
	String _MUL_STR = "MUL";
	String _DIV_STR = "DIV";
	String _NEG_STR = "NEG";
	// LOGIC
	String _EQUAL_STR = "EQUAL";
	String _NOTEQL_STR = "NOTEQL";
	String _GREATER_STR = "GREATER";
	String _LESS_STR = "LESS";
	String _GTREQL_STR = "GTREQL";
	String _LSSEQL_STR = "LSSEQL";
	String _NOT_STR = "NOT";
	// STACK
	String _PUSHC_STR = "PUSHC";
	String _PUSH_STR = "PUSH";
	String _POPC_STR = "POPC";
	String _POP_STR = "POP";
	// PROGRAM FLOW
	String _BRANCH_STR = "BRANCH";
	String _JUMP_STR = "JUMP";
	String _BREQL_STR = "BREQL";
	String _BRLSS_STR = "BRLSS";
	String _BRGTR_STR = "BRGTR";
	// I/O
	String _RDCHAR_STR = "RDCHAR";
	String _RDINT_STR = "RDINT";
	String _WRCHAR_STR = "WRCHAR";
	String _WRINT_STR = "WRINT";
	// MISC
	String _CONTENTS_STR = "CONTENTS";
	String _HALT_STR = "HALT";

}
