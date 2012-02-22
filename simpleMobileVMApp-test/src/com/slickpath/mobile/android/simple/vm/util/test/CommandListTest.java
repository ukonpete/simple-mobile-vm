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
import com.slickpath.mobile.android.simple.vm.util.CommandList;

public class CommandListTest extends AndroidTestCase {

	private static final int TEST_PARAM_VAL1 = 15;
	private static final int TEST_PARAM_VAL2 = 25;

	private CommandList testList1 = null;

	private final Integer COMMAND_1 =  Integer.valueOf(Instructions._PUSHC);
	private final Integer COMMAND_PARAM_VAL_1 = new Integer(TEST_PARAM_VAL1);
	private final List<Integer> COMMAND_PARAM_LIST_1 = new ArrayList<Integer>(Arrays.asList(COMMAND_PARAM_VAL_1));

	private final Integer COMMAND_2 =  Integer.valueOf(Instructions._PUSH);
	private final Integer COMMAND_PARAM_VAL_2 = new Integer(TEST_PARAM_VAL2);
	private final List<Integer> COMMAND_PARAM_LIST_2 = new ArrayList<Integer>(Arrays.asList(COMMAND_PARAM_VAL_2));

	private final Integer COMMAND_3 =  Integer.valueOf(Instructions._ADD);
	private final List<Integer> COMMAND_PARAM_LIST_3 = null;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		testList1 = new CommandList();
		testList1.add(new Command(COMMAND_1, COMMAND_PARAM_LIST_1));
		testList1.add(COMMAND_2, COMMAND_PARAM_LIST_2);
		testList1.add(COMMAND_3, COMMAND_PARAM_LIST_3);
	}

	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testCommands() {
		assertEquals(testList1.size(), 3);
		assertNotNull(testList1.get(0));
		assertEquals(testList1.get(0).getCommandId(), COMMAND_1);
		assertNotNull(testList1.get(0).getParameters());
		assertEquals(testList1.get(0).getParameters().size(), 1);
		assertEquals(testList1.get(0).getParameters().get(0), COMMAND_PARAM_VAL_1);

		assertNotNull(testList1.get(1));
		assertEquals(testList1.get(1).getCommandId(), COMMAND_2);
		assertNotNull(testList1.get(1).getParameters());
		assertEquals(testList1.get(1).getParameters().size(), 1);
		assertEquals(testList1.get(1).getParameters().get(0), COMMAND_PARAM_VAL_2);

		assertNotNull(testList1.get(2));
		assertEquals(testList1.get(2).getCommandId(), COMMAND_3);
		assertNull(testList1.get(2).getParameters().get(0));
	}

}
