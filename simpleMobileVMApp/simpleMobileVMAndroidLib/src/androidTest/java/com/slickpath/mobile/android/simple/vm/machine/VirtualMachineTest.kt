package com.slickpath.mobile.android.simple.vm.machine

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.instructions.Instructions
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser
import com.slickpath.mobile.android.simple.vm.util.Command
import com.slickpath.mobile.android.simple.vm.util.CommandList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class VirtualMachineTest {

    @Before
    fun before() {
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.addCommand].
     */
    @Test
    fun testAddCommand() {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val instructions = intArrayOf(
            Instructions.ADD,
            Instructions.EQUAL,
            Instructions.NOT,
            Instructions.PUSHC,
            Instructions.JUMP,
            Instructions.POPC
        )
        val params = arrayOf(-1, -1, -1, 10, 20, 30)
        try {
            for (i in instructions.indices) {
                val instruction = instructions[i]
                val paramList: MutableList<Int> = ArrayList()
                val param = params[i]
                if (instruction >= 1000) {
                    paramList.add(param)
                }
                val command = Command(instruction, paramList)
                virtualMachine.addCommand(command)
            }
            for (i in instructions.indices) {
                val command = virtualMachine.getCommandAt(i)
                assertEquals(instructions[i], command.commandId)
                if (command.commandId >= 1000) {
                    assertEquals(params[i], command.parameters[0])
                } else {
                    assertEquals(0, command.parameters.size)
                }
            }
        } catch (e: VMError) {
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.addCommands].
     */
    @Test
    fun testAddCommands() = runTest {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val instructions = intArrayOf(
            Instructions.ADD,
            Instructions.EQUAL,
            Instructions.NOT,
            Instructions.PUSHC,
            Instructions.JUMP,
            Instructions.POPC
        )
        val params = arrayOf(-1, -1, -1, 10, 20, 30)
        try {
            val commandList = CommandList()
            for (i in instructions.indices) {
                val instruction = instructions[i]
                val paramList: MutableList<Int> = ArrayList()
                val param = params[i]
                if (instruction >= 1000) {
                    paramList.add(param)
                }
                val command = Command(instruction, paramList)
                commandList.add(command)
            }
            virtualMachine.addCommands(commandList)
            for (i in instructions.indices) {
                val command = virtualMachine.getCommandAt(i)
                assertEquals(instructions[i], command.commandId)
                if (command.commandId >= 1000) {
                    assertEquals(params[i], command.parameters[0])
                } else {
                    assertEquals(0, command.parameters.size)
                }
            }
        } catch (e: VMError) {
            fail(e.message)
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.runNextInstruction].
     */
    @Test
    fun testRunNextInstruction() = runTest {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.INSTRUCTIONS))
        val parseResult = parser.parse()
        Log.d(TAG, "+...........................VM ADD COMMANDS START")
        virtualMachine.addCommands(parseResult.commands)
        assertNull(parseResult.vmError)
        Log.d(TAG, "+...........................VM RUN INSTRS START")
        val runResult = virtualMachine.runInstructions(NUM_COMMANDS_TO_RUN)
        assertFalse(runResult.didHalt)
        assertEquals(NUM_COMMANDS_TO_RUN, runResult.lastLineExecuted)
        assertNull(runResult.vmError)

        for (i in 0..99) {
            var result: VirtualMachine.RunResult? = null
            try {
                result = virtualMachine.runNextInstruction()
                assertFalse("($i) Halt= ", result.didHalt)
            } catch (vmError: VMError) {
                assertNull(
                    "(" + i + ") LINE=" + result?.lastLineExecuted + " --> " + vmError.message,
                    result?.vmError
                )
            }
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.runInstructions].
     */
    @Test
    fun testRunInstructions() = runTest {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.INSTRUCTIONS))
        val parseResult = parser.parse()
        virtualMachine.addCommands(parseResult.commands)
        assertNull(parseResult.vmError)
        val result = virtualMachine.runInstructions()
        assertTrue(result.didHalt)
        assertEquals(35, result.lastLineExecuted)
        assertNull(result.vmError)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.runInstructions].
     */
    @Test
    fun testRunInstructionsInt() = runTest {
        Log.d(TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.INSTRUCTIONS))
        val parseResult = parser.parse()
        virtualMachine.addCommands(parseResult.commands)
        assertNull(parseResult.vmError)
        val result = virtualMachine.runInstructions(10)
        assertFalse(result.didHalt)
        Log.d(TAG, "+......... checking  last line executed")
        assertEquals(10, result.lastLineExecuted)
        assertNull(result.vmError)
        Log.d(TAG, "??????????????????????????????")
    }


    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.runInstructions].
     */
    @Test
    fun testAddInstructionsWithParser() = runTest {
        Log.d(TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.INSTRUCTIONS))
        val addInstructionsResult = virtualMachine.addCommands(parser)
        assertEquals(35, addInstructionsResult.addedInstructionCount)
        virtualMachine.runInstructions()
        Log.d(TAG, "+......... checking last line executed")
        assertEquals(35, virtualMachine.programCounter)
        Log.d(TAG, "??????????????????????????????")
    }

    companion object {
        private val TAG = VirtualMachineTest::class.java.name
        private const val NUM_COMMANDS_TO_RUN = 10
    }
}