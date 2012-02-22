/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.util.test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.instructions.Instructions;
import com.slickpath.mobile.android.simple.vm.util.Command;

/**
 * @author PJ
 *
 */
public class CommandTest extends AndroidTestCase {

	private static final int TEST_PARAM_VAL = 15;
	private Command _commandWithParam = null;
	private final Integer COMMAND_1 =  Integer.valueOf(Instructions._PUSHC);
	private final Integer COMMAND_PARAM_VAL_1 = new Integer(TEST_PARAM_VAL);
	private final List<Integer> COMMAND_PARAM_LIST_1 = new ArrayList<Integer>(Arrays.asList(COMMAND_PARAM_VAL_1));
	private Command _commandWithOutParam = null;
	private final Integer COMMAND_2 = Integer.valueOf(Instructions._ADD);
	//private final Integer COMMAND_PARAM_VAL_2 = null;
	private final List<Integer> COMMAND_PARAM_LIST_2 = null;

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
		super.setUp();
		_commandWithParam = new Command(COMMAND_1.intValue(), COMMAND_PARAM_LIST_1);
		_commandWithOutParam = new Command(COMMAND_2.intValue(), COMMAND_PARAM_LIST_2);
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
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.util.Command#getCommandId()}.
	 */
	@Test
	public void testGetCommandId() {
		assertEquals(_commandWithParam.getCommandId(), COMMAND_1);
		assertEquals(_commandWithOutParam.getCommandId(), COMMAND_2);
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.util.Command#getParameters()}.
	 */
	@Test
	public void testGetParameters() {
		assertNotNull(_commandWithParam.getParameters());
		assertEquals(_commandWithParam.getParameters().get(0), COMMAND_PARAM_VAL_1);
		assertNotNull(_commandWithOutParam.getParameters());
	}

}
