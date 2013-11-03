/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.util.test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.instructions.Instructions;
import com.slickpath.mobile.android.simple.vm.util.Command;

/**
 * @author Pete Procopio
 *
 */
public class CommandTest extends AndroidTestCase {

	private static final int TEST_PARAM_VAL = 15;
	private Command _commandWithParam = null;
	private final Integer COMMAND_1 =  Integer.valueOf(Instructions._PUSHC);
	private final Integer COMMAND_PARAM_VAL_1 = Integer.valueOf(TEST_PARAM_VAL);
	private final List<Integer> COMMAND_PARAM_LIST_1 = new ArrayList<Integer>(Arrays.asList(COMMAND_PARAM_VAL_1));
	private Command _commandWithOutParam = null;
	private final Integer COMMAND_2 = Integer.valueOf(Instructions._ADD);
	private final List<Integer> COMMAND_PARAM_LIST_2 = null;

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_commandWithParam = new Command(COMMAND_1.intValue(), COMMAND_PARAM_LIST_1);
		_commandWithOutParam = new Command(COMMAND_2.intValue(), COMMAND_PARAM_LIST_2);
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.util.Command#getCommandId()}.
	 */
	public void testGetCommandId() {
		assertEquals(_commandWithParam.getCommandId(), COMMAND_1);
		assertEquals(_commandWithOutParam.getCommandId(), COMMAND_2);
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.util.Command#getParameters()}.
	 */
	public void testGetParameters() {
		assertNotNull(_commandWithParam.getParameters());
		assertEquals(_commandWithParam.getParameters().get(0), COMMAND_PARAM_VAL_1);
		assertNotNull(_commandWithOutParam.getParameters());
	}

}
