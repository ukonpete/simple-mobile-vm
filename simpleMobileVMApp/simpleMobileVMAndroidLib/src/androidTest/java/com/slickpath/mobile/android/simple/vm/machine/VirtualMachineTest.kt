package com.slickpath.mobile.android.simple.vm.machine

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.VMListener
import com.slickpath.mobile.android.simple.vm.instructions.Instructions
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser
import com.slickpath.mobile.android.simple.vm.util.Command
import com.slickpath.mobile.android.simple.vm.util.CommandList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class VirtualMachineTest {

    private var _vmError: VMError? = null
    private var _bHalt = false
    private var _lastLineExecuted = 0
    private var _instructionAddedCount = 0
    private var _commands: CommandList? = null

    @Before
    fun before() {
        _bHalt = false
        _lastLineExecuted = -1
        _instructionAddedCount = -1
        _vmError = null
        _commands = null
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.vmListener].
     */
    @Test
    fun testGetVMListener() {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        assertNull(virtualMachine.vmListener)
        virtualMachine.vmListener = null
        virtualMachine.vmListener = object : VMListener {
            override fun completedAddingInstructions(vmError: VMError?, instructionsAdded: Int) {
                this@VirtualMachineTest.completedAddingInstructions(instructionsAdded)
            }

            override fun completedRunningInstructions(
                bHalt: Boolean,
                lastLineExecuted: Int,
                vmError: VMError?
            ) {
                this@VirtualMachineTest.completedRunningInstructions(
                    bHalt,
                    lastLineExecuted,
                    vmError
                )
            }
        }
        assertNotNull(virtualMachine.vmListener)
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
            val signalAddCommands = CountDownLatch(1)
            addVMListenerAdding(virtualMachine, signalAddCommands)
            virtualMachine.addCommands(commandList)
            assertNull(_vmError)
            try {
                // Wait for Callback
                signalAddCommands.await()
            } catch (e: InterruptedException) {
                e.printStackTrace()
                fail(e.message)
            } // wait for callback
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
        val signalAddCommands = CountDownLatch(1)
        addVMListenerAdding(virtualMachine, signalAddCommands)
        virtualMachine.addCommands(parseResult.commands)
        try {
            // Wait for Callback
            Log.d(TAG, "+...........................VM ADD COMMANDS WAIT ")
            signalAddCommands.await()
            Log.d(TAG, "+...........................VM ADD COMMANDS DONE WAIT ")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        assertNull(parseResult.vmError)
        Log.d(TAG, "+...........................VM RUN INSTRS START")
        val signalRunInstructions = CountDownLatch(1)
        addVMListenerRunning(virtualMachine, signalRunInstructions)
        virtualMachine.runInstructions(NUM_COMMANDS_TO_RUN)
        try {
            // Wait for Callback
            Log.d(TAG, "+...........................VM RUN INSTRS WAIT ")
            signalRunInstructions.await()
            Log.d(TAG, "+...........................VM RUN INSTRS DONE WAIT ")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        assertFalse(_bHalt)
        assertEquals(NUM_COMMANDS_TO_RUN, _lastLineExecuted)
        assertNull(_vmError)
        for (i in 0..99) {
            try {
                val results = virtualMachine.runNextInstruction()
                assertFalse("($i) Halt= ", results.halt)
            } catch (vmError: VMError) {
                assertNull(
                    "(" + i + ") LINE=" + _lastLineExecuted + " --> " + vmError.message,
                    _vmError
                )
            }
        }
    }

    private fun addVMListenerAdding(virtualMachine: VirtualMachine, signal: CountDownLatch) {
        virtualMachine.vmListener = object : VMListener {
            override fun completedAddingInstructions(vmError: VMError?, instructionsAdded: Int) {
                this@VirtualMachineTest.completedAddingInstructions(instructionsAdded)
                signal.countDown() // notify the count down latch
            }

            override fun completedRunningInstructions(
                bHalt: Boolean,
                lastLineExecuted: Int,
                vmError: VMError?
            ) {
                // Do Nothing
            }
        }
    }

    private fun addVMListenerRunning(virtualMachine: VirtualMachine, signal: CountDownLatch) {
        virtualMachine.vmListener = object : VMListener {
            override fun completedAddingInstructions(vmError: VMError?, instructionsAdded: Int) {
                // Do Nothing
            }

            override fun completedRunningInstructions(
                bHalt: Boolean,
                lastLineExecuted: Int,
                vmError: VMError?
            ) {
                this@VirtualMachineTest.completedRunningInstructions(
                    bHalt,
                    lastLineExecuted,
                    vmError
                )
                signal.countDown() // notify the count down latch
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
        val signalAddCommands = CountDownLatch(1)
        addVMListenerAdding(virtualMachine, signalAddCommands)
        virtualMachine.addCommands(parseResult.commands)
        assertNull(parseResult.vmError)
        try {
            // Wait for Callback
            signalAddCommands.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        val signalRunInstructions = CountDownLatch(1)
        addVMListenerRunning(virtualMachine, signalRunInstructions)
        virtualMachine.runInstructions()
        try {
            // Wait for Callback
            signalRunInstructions.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        assertTrue(_bHalt)
        assertEquals(35, _lastLineExecuted)
        assertNull(_vmError)
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
        val signalAddCommands = CountDownLatch(1)
        addVMListenerAdding(virtualMachine, signalAddCommands)
        virtualMachine.addCommands(parseResult.commands)
        assertNull(parseResult.vmError)
        try {
            // Wait for Callback
            signalAddCommands.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        val signalRunInstructions = CountDownLatch(1)
        addVMListenerRunning(virtualMachine, signalRunInstructions)
        virtualMachine.runInstructions(10)
        try {
            // Wait for Callback
            signalRunInstructions.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        assertFalse(_bHalt)
        Log.d(TAG, "+......... checking  last line executed")
        assertEquals(10, _lastLineExecuted)
        assertNull(_vmError)
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
        assertEquals(35, addInstructionsResult.commands.size)
        val signalRunInstructions = CountDownLatch(1)
        addVMListenerRunning(virtualMachine, signalRunInstructions)
        virtualMachine.runInstructions()
        try {
            // Wait for Callback
            signalRunInstructions.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        Log.d(TAG, "+......... checking last line executed")
        assertEquals(35, virtualMachine.programCounter)
        Log.d(TAG, "??????????????????????????????")
    }

    private fun completedAddingInstructions(instructionsAdded: Int) {
        Log.d(TAG, "+..........completedAddingInstructions ")
        _instructionAddedCount = instructionsAdded
        Log.d(TAG, "+..........completedAddingInstructions CountDown")
    }

    private fun completedRunningInstructions(
        bHalt: Boolean,
        lastLineExecuted: Int, vmError: VMError?
    ) {
        Log.d(
            TAG,
            "+..........CompletedRunningInstructions $lastLineExecuted halt = $bHalt vmError = $vmError"
        )
        _vmError = vmError
        _bHalt = bHalt
        _lastLineExecuted = lastLineExecuted
        Log.d(TAG, "+..........CompletedRunningInstructions CountDown")
    }

    companion object {
        private val TAG = VirtualMachineTest::class.java.name
        private const val NUM_COMMANDS_TO_RUN = 10
    }
}