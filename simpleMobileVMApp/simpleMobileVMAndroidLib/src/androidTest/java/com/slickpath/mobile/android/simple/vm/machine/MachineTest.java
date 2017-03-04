package com.slickpath.mobile.android.simple.vm.machine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.VMError;

/**
 * @author Pete Procopio
 *
 */
public class MachineTest extends AndroidTestCase {

	private static final int MEM_LOC_1 = 10;
	private static final int RESULT_LOC = 5;
	private static final int LOW_VAL = 15;
	private static final int HIGH_VAL = 17;
	@Nullable
	private Machine _machine = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	public void setUp() throws Exception {
		_machine = new Machine();
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#ADD()}.
	 */
	public void testADD() {
		try {
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.ADD();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			final int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(HIGH_VAL + LOW_VAL, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("ADD should have thrown VMError", didMethodFail(_machine, "ADD"));
		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#SUB()}.
	 */
	public void testSUB() {
		try {
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.SUB();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			final int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(HIGH_VAL - LOW_VAL, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("ADD should have thrown VMError", didMethodFail(_machine, "SUB"));
		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#MUL()}.
	 */
	public void testMUL() {
		try {
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.MUL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			final int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(HIGH_VAL * LOW_VAL, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("MUL should have thrown VMError", didMethodFail(_machine, "MUL"));
		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#DIV()}.
	 */
	public void testDIV() {
		try {
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.DIV();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			final int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(HIGH_VAL / LOW_VAL, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("DIV should have thrown VMError", didMethodFail(_machine, "DIV"));
		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#NEG()}.
	 */
	public void testNEG() {
		try {
			_machine.PUSHC(LOW_VAL);
			_machine.NEG();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			final int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(-LOW_VAL, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("NEG should have thrown VMError", didMethodFail(_machine, "NEG"));
		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#EQUAL()}.
	 */
	public void testEQUAL() {
		try {
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.EQUAL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("EQUAL should have thrown VMError", didMethodFail(_machine, "EQUAL"));

			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.EQUAL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(1, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("EQUAL should have thrown VMError", didMethodFail(_machine, "EQUAL"));

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#NOTEQL()}.
	 */
	public void testNOTEQL() {
		try
		{
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.NOTEQL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(1, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("NOTEQL should have thrown VMError", didMethodFail(_machine, "NOTEQL"));

			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.NOTEQL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("NOTEQL should have thrown VMError", didMethodFail(_machine, "NOTEQL"));

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#GREATER()}.
	 */
	public void testGREATER() {
		try
		{
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.GREATER();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(1, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("GREATER should have thrown VMError", didMethodFail(_machine, "GREATER"));

			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.GREATER();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("GREATER should have thrown VMError", didMethodFail(_machine, "GREATER"));

			_machine.PUSHC(HIGH_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.GREATER();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("GREATER should have thrown VMError", didMethodFail(_machine, "GREATER"));

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#LESS()}.
	 */
	public void testLESS() {
		try
		{
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.LESS();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("LESS should have thrown VMError", didMethodFail(_machine, "LESS"));

			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.LESS();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("LESS should have thrown VMError", didMethodFail(_machine, "LESS"));

			_machine.PUSHC(HIGH_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.LESS();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(1, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("LESS should have thrown VMError", didMethodFail(_machine, "LESS"));

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#GTREQL()}.
	 */
	public void testGTREQL() {
		try
		{
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.GTREQL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(1, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("GTREQL should have thrown VMError", didMethodFail(_machine, "GTREQL"));

			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.GTREQL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(1, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("GTREQL should have thrown VMError", didMethodFail(_machine, "GTREQL"));

			_machine.PUSHC(HIGH_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.GTREQL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("GTREQL should have thrown VMError", didMethodFail(_machine, "GTREQL"));

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#LSSEQL()}.
	 */
	public void testLSSEQL() {
		try
		{
			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(HIGH_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.LSSEQL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("LSSEQL should have thrown VMError", didMethodFail(_machine, "LSSEQL"));

			_machine.PUSHC(LOW_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.LSSEQL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(1, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("LSSEQL should have thrown VMError", didMethodFail(_machine, "LSSEQL"));

			_machine.PUSHC(HIGH_VAL);
			_machine.setValueAt(LOW_VAL, MEM_LOC_1);
			_machine.PUSH(MEM_LOC_1);
			_machine.LSSEQL();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(1, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("LSSEQL should have thrown VMError", didMethodFail(_machine, "LSSEQL"));

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#NOT()}.
	 */
	public void testNOT() {
		try {
			_machine.PUSHC(0);
			_machine.NOT();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			int val = _machine.getValueAt(RESULT_LOC);
			assertEquals(1, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("NOT should have thrown VMError", didMethodFail(_machine, "NOT"));

			_machine.PUSHC(1);
			_machine.NOT();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("NOT should have thrown VMError", didMethodFail(_machine, "NOT"));

			_machine.PUSHC(-1);
			_machine.NOT();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("NOT should have thrown VMError", didMethodFail(_machine, "NOT"));


			_machine.PUSHC(14);
			_machine.NOT();
			_machine.PUSHC(RESULT_LOC);
			_machine.POP();
			val = _machine.getValueAt(RESULT_LOC);
			assertEquals(0, val);
			assertTrue("POP should have thrown VMError", didPopFail());
			assertTrue("NOT should have thrown VMError", didMethodFail(_machine, "NOT"));

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#PUSH(int)}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#POPC(int)}.
	 */
	public void testPUSH() {
		try {
			final int [] vals = new int[]{20, 15, 17, 44, 101};
			final int [] locs = new int[]{33, 45, 62, 77, 249};

			for(int i = 0; i < vals.length; i++)
			{
				_machine.setValueAt(vals[i], locs[i]);
				_machine.PUSH(locs[i]);
			}

			final List<Integer> stackDump = _machine.dumpStack();

			for(int i = 0; i < vals.length; i++)
			{
				final int memVal = _machine.getValueAt(locs[i]);
				assertEquals(vals[i], memVal);
				final int stackVal = stackDump.get(i);
				assertEquals(vals[i], stackVal);
				_machine.POPC(i);
				final int poppedVal = _machine.getValueAt(i);
				assertEquals(vals[vals.length - i - 1], poppedVal);
			}

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#PUSHC(int)}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#POPC(int)}.
	 */
	public void testPUSHC() {
		try {
			final int [] vals = new int[]{20, 15, 17, 44, 101};

			for (final int val : vals) {
				_machine.PUSHC(val);
			}

			final List<Integer> stackDump = _machine.dumpStack();

			for(int i = 0; i < vals.length; i++)
			{
				final int stackVal = stackDump.get(i);
				assertEquals(vals[i], stackVal);
				_machine.POPC(i);
				final int poppedVal = _machine.getValueAt(i);
				assertEquals(vals[vals.length - i - 1], poppedVal);
			}

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#POP()}.
	 */
	public void testPOP() {
		try {
			final int [] vals = new int[]{20, 15, 17, 44, 101};
			final int [] locs = new int[]{33, 45, 62, 77, 249};

			for(int i = 0; i < vals.length; i++)
			{
				_machine.PUSHC(vals[i]);
				_machine.PUSHC(locs[i]);
			}

			final List<Integer> stackDump = _machine.dumpStack();

			for(int i = 0; i < vals.length; i++)
			{
				final int stackVal = stackDump.get(i*2);
				final int stackValLoc = stackDump.get((i*2)+1);
				assertEquals(vals[i], stackVal);
				assertEquals(locs[i], stackValLoc);
				_machine.POP();
				final int poppedVal = _machine.getValueAt(locs[vals.length - i - 1]);
				assertEquals(vals[vals.length - i - 1], poppedVal);
			}

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

    /**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#BRANCH(int)}.
	 */
	public void testBRANCH() {
		try {
			assertEquals(Memory.START_LOC, _machine.getProgramCounter());
			for (int i = 0; i < 100; i++) {
				_machine.incProgramCounter();
			}
			assertEquals(100, _machine.getProgramCounter());
			for (int i = 0; i < 20; i++) {
				_machine.decProgramCounter();
			}
			assertEquals(80, _machine.getProgramCounter());
			_machine.branch(50);
			assertEquals(50, _machine.getProgramCounter());
			_machine.branch(249);
			assertEquals(249, _machine.getProgramCounter());
			_machine.branch(1);
			assertEquals(1, _machine.getProgramCounter());
			_machine.resetProgramCounter();
			assertEquals(Memory.START_LOC, _machine.getProgramCounter());
			_machine.branch(25);
			assertEquals(25, _machine.getProgramCounter());
		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#JUMP()}.
	 */
	public void testJUMP() {
		try {

			assertEquals(Memory.START_LOC, _machine.getProgramCounter());
			for (int i = 0; i < 100; i++) {
				_machine.incProgramCounter();
			}
			assertEquals(100, _machine.getProgramCounter());
			for (int i = 0; i < 20; i++) {
				_machine.decProgramCounter();
			}
			assertEquals(80, _machine.getProgramCounter());
			_machine.push(2);
			_machine.push(12);
			_machine.push(72);
			_machine.push(99);
			_machine.jump();
			assertEquals(99, _machine.getProgramCounter());
			_machine.jump();
			assertEquals(72, _machine.getProgramCounter());
			_machine.jump();
			assertEquals(12, _machine.getProgramCounter());
			_machine.resetStack();
			_machine.push(7);
			assertEquals(12, _machine.getProgramCounter());
			_machine.jump();
			assertEquals(7, _machine.getProgramCounter());
		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#BREQL(int)}.
	 */
	public void testBREQL() {
		try {
			assertEquals(Memory.START_LOC, _machine.getProgramCounter());
			for (int i = 0; i < 100; i++) {
				_machine.incProgramCounter();
			}
			assertEquals(100, _machine.getProgramCounter());
			for (int i = 0; i < 20; i++) {
				_machine.decProgramCounter();
			}
			assertEquals(80, _machine.getProgramCounter());

			_machine.PUSHC(1);
			_machine.BREQL(12);
			assertEquals(80, _machine.getProgramCounter());

			_machine.PUSHC(0);
			_machine.BREQL(56);
			assertEquals(56, _machine.getProgramCounter());

			boolean bFailed = false;
			try
			{
				_machine.PUSHC(0);
				_machine.BREQL(602);
			}
			catch(@NonNull final VMError vmError)
			{
				bFailed = true;
			}
			assertTrue("BREQL 602 is out of bounds", bFailed);
		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#BRLSS(int)}.
	 */
	public void testBRLSS() {
		try {
			assertEquals(Memory.START_LOC, _machine.getProgramCounter());
			for (int i = 0; i < 100; i++) {
				_machine.incProgramCounter();
			}
			assertEquals(100, _machine.getProgramCounter());
			for (int i = 0; i < 20; i++) {
				_machine.decProgramCounter();
			}
			assertEquals(80, _machine.getProgramCounter());

			_machine.PUSHC(1);
			_machine.BRLSS(12);
			assertEquals(80, _machine.getProgramCounter());

			_machine.PUSHC(0);
			_machine.BRLSS(12);
			assertEquals(80, _machine.getProgramCounter());

			_machine.PUSHC(-1);
			_machine.BRLSS(56);
			assertEquals(56, _machine.getProgramCounter());

			boolean bFailed = false;
			try
			{
				_machine.PUSHC(-782);
				_machine.BRLSS(602);
			}
			catch(@NonNull final VMError vmError)
			{
				bFailed = true;
			}
			assertTrue("BREQL 602 is out of bounds", bFailed);

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#BRGTR(int)}.
	 */
	public void testBRGTR() {
		try {
			assertEquals(Memory.START_LOC, _machine.getProgramCounter());
			for (int i = 0; i < 100; i++) {
				_machine.incProgramCounter();
			}
			assertEquals(100, _machine.getProgramCounter());
			for (int i = 0; i < 20; i++) {
				_machine.decProgramCounter();
			}
			assertEquals(80, _machine.getProgramCounter());

			_machine.PUSHC(-1);
			_machine.BRGTR(12);
			assertEquals(80, _machine.getProgramCounter());

			_machine.PUSHC(0);
			_machine.BRGTR(12);
			assertEquals(80, _machine.getProgramCounter());

			_machine.PUSHC(1);
			_machine.BRGTR(56);
			assertEquals(56, _machine.getProgramCounter());

			boolean bFailed = false;
			try
			{
				_machine.PUSHC(782);
				_machine.BRGTR(602);
			}
			catch(@NonNull final VMError vmError)
			{
				bFailed = true;
			}
			assertTrue("BRGTR 602 is out of bounds", bFailed);

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#CONTENTS()}.
	 */
	public void testCONTENTS() {
		try {
			final int [] vals = new int[]{20, 15, 17, 44, 101};
			final int [] locs = new int[]{33, 45, 62, 77, 249};

			for(int i = 0; i < vals.length; i++)
			{
				_machine.PUSHC(locs[i]);
				_machine.setValueAt(vals[i], locs[i]);
			}

            for(int i = 0; i < vals.length; i++)
			{
				_machine.CONTENTS();
				_machine.POPC(i);
			}

            for(int i = 0; i < vals.length; i++)
			{
				final int val = _machine.getValueAt(i);
				assertEquals(vals[vals.length-i-1], val);
			}

            boolean bFailed = false;
			try
			{
				_machine.CONTENTS();
			}
			catch(@NonNull final VMError vmError)
			{
				bFailed = true;
			}
			assertTrue("CONTENTS is out of bounds", bFailed);

		} catch (@NonNull final VMError e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.Machine#HALT()}.
	 */
	public void testHALT() {

		final List<Integer> memory = _machine.dumpMemory();
		final List<Integer> stack = _machine.dumpStack();

		for (int i = 0; i < 100; i++) {
			_machine.incProgramCounter();
		}
		assertEquals(100, _machine.getProgramCounter());
		for (int i = 0; i < 20; i++) {
			_machine.decProgramCounter();
		}
		assertEquals(80, _machine.getProgramCounter());

		_machine.HALT();
		assertEquals(80, _machine.getProgramCounter());
		_machine.HALT();
		assertEquals(80, _machine.getProgramCounter());

		assertTrue(_machine.dumpMemory().equals(memory));
		assertTrue(_machine.dumpStack().equals(stack));
	}

	/**
	 * Does a POP and returns if it failed or not (true if VMError is thrown)
	 * 
	 * @return
	 */
	private boolean didPopFail() {
		boolean bFailed = false;
		try
		{
			_machine.POP();
		}
		catch (@NonNull final VMError e)
		{
			bFailed = true;
		}
		return bFailed;
	}

	/**
	 * Does a Machine.Method fail and returns if it failed or not (true if VMError is thrown)
	 * Uses reflection so that this one method could be called in place of having the code repeated over and over again for each
	 * method.
	 * 
	 * @param machine
	 * @param sMethod
	 * @return
	 */
	@VisibleForTesting
	public boolean didMethodFail(final Machine machine, final String sMethod){
		boolean bFailed = false;

		Method method;
		try {
			method = Machine.class.getDeclaredMethod(sMethod);
			method.invoke(machine);
		}
		catch (@NonNull final InvocationTargetException e)
		{
			if ( e.getCause() instanceof VMError )
			{
				bFailed = true;
			}
			e.printStackTrace();
		}
		catch (@NonNull final SecurityException | NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (@NonNull final Exception e) {
			bFailed = true;
		}
		return bFailed;
	}
}
