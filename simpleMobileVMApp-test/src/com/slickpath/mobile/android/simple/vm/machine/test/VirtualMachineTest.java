/**
 * 
 */
package com.slickpath.mobile.android.simple.vm.machine.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.IVMListener;
import com.slickpath.mobile.android.simple.vm.VMError;
import com.slickpath.mobile.android.simple.vm.instructions.Instructions;
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine;
import com.slickpath.mobile.android.simple.vm.util.Command;

/**
 * @author PJ
 *
 */
public class VirtualMachineTest extends AndroidTestCase implements IVMListener{

	private VirtualMachine _vm = null;
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
		_vm = new VirtualMachine(getContext());
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
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#getVMListener()}.
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#setVMListener(com.slickpath.mobile.android.simple.vm.IVMListener)}.
	 */
	@Test
	public void testGetVMListener() {
		assertNull(_vm.getVMListener());
		_vm.setVMListener(this);
		assertNotNull(_vm.getVMListener());
		_vm.setVMListener(null);
		assertNull(_vm.getVMListener());
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#addCommand(com.slickpath.mobile.android.simple.vm.util.Command)}.
	 */
	@Test
	public void testAddCommand() {

		final int [] instructions = {Instructions._ADD, Instructions._EQUAL, Instructions._NOT, Instructions._PUSHC, Instructions._JUMP, Instructions._POPC};
		final Integer [] params = {null, null, null, 10, 20, 30};

		try {
			for(int i = 0; i < instructions.length; i++)
			{
				final List<Integer> paramList = new ArrayList<Integer>();
				final Integer param = params[i];
				paramList.add(param);
				final Command command = new Command(instructions[i],paramList);
				_vm.addCommand(command);
			}

			for(int i = 0; i < instructions.length; i++)
			{
				final Command command = _vm.getCommandAt(i);
				assertEquals(instructions[i], command.getCommandId().intValue());
				assertEquals(params[i], command.getParameters().get(0));
			}

		} catch (final VMError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#addCommands(com.slickpath.mobile.android.simple.vm.util.CommandList)}.
	 */
	@Test
	public void testAddCommands() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#runNextInstruction()}.
	 */
	@Test
	public void testRunNextInstruction() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#runInstructions()}.
	 */
	@Test
	public void testRunInstructions() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.slickpath.mobile.android.simple.vm.machine.VirtualMachine#runInstructions(int)}.
	 */
	@Test
	public void testRunInstructionsInt() {
		fail("Not yet implemented");
	}

	@Override
	public void completedAddingInstructions(final VMError vmError) {
		// TODO Auto-generated method stub

	}

	@Override
	public void completedRunningInstructions(final boolean bHalt,
			final int lastLineExecuted, final VMError vmError) {
		// TODO Auto-generated method stub

	}

}
