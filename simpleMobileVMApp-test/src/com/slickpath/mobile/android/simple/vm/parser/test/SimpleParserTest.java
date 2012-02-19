/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.parser.test;


import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.parser.IParserListener;
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser;
import com.slickpath.mobile.android.simple.vm.util.CommandList;

/**
 * @author PJ
 *
 */
public class SimpleParserTest extends AndroidTestCase implements IParserListener{

	private static final int FIB_LINE_NUMBER = 15;
	private static final int HALT_LINE_NUMNBER = 34;
	private SimpleParser _parser;
	private CountDownLatch _signal;
	private VMError _error;
	private CommandList _commands;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#setUp()
	 */
	@Override
	@Before
	protected void setUp() throws Exception {

		final File filesDir = mContext.getFilesDir();
		_parser = new SimpleParser(filesDir.getPath() + File.separator + "fibonacciAndroid.ins.txt", this);
		_signal = new CountDownLatch(1);
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.parser.SimpleParser#parse()}.
	 */
	@Test
	public void testParse() {
		_parser.parse();
		try {
			_signal.await();
		} catch (final InterruptedException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}// wait for callback
		assertNull(_error);
		assertNotNull(_commands);
		assertEquals(35, _commands.size() );
		int i = 0;
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSHC); // 1
		assertNotNull(_commands.get(i).getParameters()); // 1
		assertEquals(1, _commands.get(i).getParameters().size()); // 1
		assertEquals(HALT_LINE_NUMNBER, _commands.get(i++).getParameters().get(0).intValue()); // 1

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSHC); // 2
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(0, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i++).getCommandId().intValue(), BaseInstructionSet._WRINT); // 3
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSHC); // 4
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(1, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i++).getCommandId().intValue(), BaseInstructionSet._WRINT); // 5
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSHC); // 6
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(1, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i++).getCommandId().intValue(), BaseInstructionSet._WRINT); // 7
		// 8 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSHC); // 9
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(0, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._POPC); // 10
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(10, _commands.get(i++).getParameters().get(0).intValue());
		// 11 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSHC); // 12
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(1, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._POPC); // 13
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(11, _commands.get(i++).getParameters().get(0).intValue());
		// 14 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSH); // 15
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(11, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._POPC); // 16
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());
		// 17 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSHC); // 18
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._POPC); // 19
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(13, _commands.get(i++).getParameters().get(0).intValue());
		// 20 [FIB] - Symbol
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSH); // 21
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(11, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._POPC); // 22
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(14, _commands.get(i++).getParameters().get(0).intValue());
		// 23 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSH); // 24
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(14, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._POPC); // 25
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(10, _commands.get(i++).getParameters().get(0).intValue());
		// 26 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSH); // 27
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._POPC); // 28
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(11, _commands.get(i++).getParameters().get(0).intValue());
		// 29 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSH); // 30
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSH); // 31
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(10, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i++).getCommandId().intValue(), BaseInstructionSet._ADD); // 32

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._POPC); // 33
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());
		// 34 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSH); // 35
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(12, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i++).getCommandId().intValue(), BaseInstructionSet._WRINT); // 36
		// 37 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSHC); // 38
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(1, _commands.get(i++).getParameters().get(0).intValue());
		//
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSH); // 39
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(13, _commands.get(i++).getParameters().get(0).intValue());
		//
		assertEquals(_commands.get(i++).getCommandId().intValue(), BaseInstructionSet._SUB); // 40

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._POPC); // 41
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(13, _commands.get(i++).getParameters().get(0).intValue());
		// 42 - Comment
		// 43 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._PUSH); // 44
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(13, _commands.get(i++).getParameters().get(0).intValue());

		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._BREQL); // 45
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(HALT_LINE_NUMNBER, _commands.get(i++).getParameters().get(0).intValue());
		//
		// 46 - Comment
		assertEquals(_commands.get(i).getCommandId().intValue(), BaseInstructionSet._BRANCH); // 47
		assertNotNull(_commands.get(i).getParameters());
		assertEquals(1, _commands.get(i).getParameters().size());
		assertEquals(FIB_LINE_NUMBER, _commands.get(i++).getParameters().get(0).intValue());
		// 48 - Comment
		// 49 [HALT] - Symbol
		assertEquals(_commands.get(i++).getCommandId().intValue(), BaseInstructionSet._HALT); // 50
	}

	public void completedParse(final VMError vmError, final CommandList commands) {
		// Save values on callback and release test thread
		_error = vmError;
		_commands = commands;
		_signal.countDown();// notify the count down latch
	}

}
