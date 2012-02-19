/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.instructions.test;


import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.instructions.BaseInstructionSet;
import com.slickpath.mobile.android.simple.vm.instructions.Instructions;

/**
 * @author PJ
 *
 */
public class BaseInstructionSetTest extends AndroidTestCase {

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
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testInternals() {
		assertEquals(BaseInstructionSet.INSTRUCTION_SET_HT.size(), Instructions.NUM_COMMANDS);
		assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.size(), Instructions.NUM_COMMANDS);

		assertEquals(BaseInstructionSet.INSTRUCTION_SET_HT.get(Instructions._ADD_STR).intValue(), Instructions._ADD);
		assertEquals(BaseInstructionSet.INSTRUCTION_SET_HT.get(Instructions._HALT_STR).intValue(), Instructions._HALT);

		assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(Instructions._DIV), Instructions._DIV_STR);
		assertEquals(BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(Instructions._BREQL), Instructions._BREQL_STR);


		final Set<String> keys = BaseInstructionSet.INSTRUCTION_SET_HT.keySet();

		for(final String key: keys)
		{
			final int val = BaseInstructionSet.INSTRUCTION_SET_HT.get(key);

			final String sKey = BaseInstructionSet.INSTRUCTION_SET_CONV_HT.get(val);

			assertEquals(sKey,key);
		}
	}

}
