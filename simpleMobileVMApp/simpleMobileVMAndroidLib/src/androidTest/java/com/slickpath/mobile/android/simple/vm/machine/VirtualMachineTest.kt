package com.slickpath.mobile.android.simple.vm.machine

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.IVMListener
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.instructions.Instructions
import com.slickpath.mobile.android.simple.vm.parser.IParserListener
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser
import com.slickpath.mobile.android.simple.vm.util.Command
import com.slickpath.mobile.android.simple.vm.util.CommandList
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class VirtualMachineTest {
    
    private var _vmError: VMError? = null
    private var _bHalt = false
    private var _lastLineExecuted = 0
    private var _commands: CommandList? = null
    
    @Before
    fun before() {
        _bHalt = false
        _lastLineExecuted = -1
        _vmError = null
        _commands = null
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.vMListener].
     */
    @Test
    fun testGetVMListener() {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        assertNull(virtualMachine.vMListener)
        virtualMachine.vMListener = null
        assertNull(virtualMachine.vMListener)
        virtualMachine.vMListener = object : IVMListener {
            override fun completedAddingInstructions(vmError: VMError?) {
                this@VirtualMachineTest.completedAddingInstructions(vmError)
            }

            override fun completedRunningInstructions(bHalt: Boolean, lastLineExecuted: Int, vmError: VMError?) {
                this@VirtualMachineTest.completedRunningInstructions(bHalt, lastLineExecuted, vmError)
            }
        }
        assertNotNull(virtualMachine.vMListener)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.addCommand].
     */
    @Test
    fun testAddCommand() {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val instructions = intArrayOf(Instructions.ADD, Instructions.EQUAL, Instructions.NOT, Instructions.PUSHC, Instructions.JUMP, Instructions.POPC)
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
    fun testAddCommands() {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val instructions = intArrayOf(Instructions.ADD, Instructions.EQUAL, Instructions.NOT, Instructions.PUSHC, Instructions.JUMP, Instructions.POPC)
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
    fun testRunNextInstruction() {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val signalParser = CountDownLatch(1)
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.instructions), object : IParserListener {
            override fun completedParse(vmError: VMError?, commands: CommandList?) {
                this@VirtualMachineTest.completedParse(vmError, commands)
                signalParser.countDown() // notify the count down latch
            }
        })
        Log.d(TAG, "+...........................PARSE START ")
        parser.parse()
        try {
            // Wait for Callback
            Log.d(TAG, "+...........................PARSE WAIT ")
            signalParser.await()
            Log.d(TAG, "+...........................PARSE DONE WAIT ")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        Log.d(TAG, "+...........................VM ADD COMMANDS START")
        val signalAddCommands = CountDownLatch(1)
        addVMListenerAdding(virtualMachine, signalAddCommands)
        virtualMachine.addCommands(_commands)
        try {
            // Wait for Callback
            Log.d(TAG, "+...........................VM ADD COMMANDS WAIT ")
            signalAddCommands.await()
            Log.d(TAG, "+...........................VM ADD COMMANDS DONE WAIT ")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        assertNull(_vmError)
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
                val halt = virtualMachine.runNextInstruction()
                assertFalse("($i) Halt= ", halt)
            } catch (vmError: VMError) {
                assertNull("(" + i + ") LINE=" + _lastLineExecuted + " --> " + vmError.message, _vmError)
            }
        }
    }

    private fun addVMListenerAdding(virtualMachine: VirtualMachine, signal: CountDownLatch) {
        virtualMachine.vMListener = object : IVMListener {
            override fun completedAddingInstructions(vmError: VMError?) {
                this@VirtualMachineTest.completedAddingInstructions(vmError)
                signal.countDown() // notify the count down latch
            }

            override fun completedRunningInstructions(bHalt: Boolean, lastLineExecuted: Int, vmError: VMError?) {
                // Do Nothing
            }
        }
    }

    private fun addVMListenerRunning(virtualMachine: VirtualMachine, signal: CountDownLatch) {
        virtualMachine.vMListener = object : IVMListener {
            override fun completedAddingInstructions(vmError: VMError?) {
                // Do Nothing
            }

            override fun completedRunningInstructions(bHalt: Boolean, lastLineExecuted: Int, vmError: VMError?) {
                this@VirtualMachineTest.completedRunningInstructions(bHalt, lastLineExecuted, vmError)
                signal.countDown() // notify the count down latch
            }
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.runInstructions].
     */
    @Test
    fun testRunInstructions() {
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val signalParse = CountDownLatch(1)
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.instructions), object : IParserListener {
            override fun completedParse(vmError: VMError?, commands: CommandList?) {
                this@VirtualMachineTest.completedParse(vmError, commands)
                signalParse.countDown()
            }
        })
        parser.parse()
        try {
            // Wait for Callback
            signalParse.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        val signalAddCommands = CountDownLatch(1)
        addVMListenerAdding(virtualMachine, signalAddCommands)
        virtualMachine.addCommands(_commands)
        assertNull(_vmError)
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
    fun testRunInstructionsInt() {
        Log.d(TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
        val virtualMachine = VirtualMachine(ApplicationProvider.getApplicationContext())
        val signalParse = CountDownLatch(1)
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.instructions), object : IParserListener {
            override fun completedParse(vmError: VMError?, commands: CommandList?) {
                this@VirtualMachineTest.completedParse(vmError, commands)
                signalParse.countDown()
            }
        })
        parser.parse()
        try {
            // Wait for Callback
            signalParse.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        val signalAddCommands = CountDownLatch(1)
        addVMListenerAdding(virtualMachine, signalAddCommands)
        virtualMachine.addCommands(_commands)
        assertNull(_vmError)
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

    private fun completedAddingInstructions(vmError: VMError?) {
        Log.d(TAG, "+..........completedAddingInstructions ")
        _vmError = vmError
        Log.d(TAG, "+..........completedAddingInstructions CountDown")
    }

    private fun completedRunningInstructions(bHalt: Boolean,
                                             lastLineExecuted: Int, vmError: VMError?) {
        Log.d(TAG, "+..........CompletedRunningInstructions " + lastLineExecuted + " halt = " + bHalt + "vmError = " + vmError)
        _vmError = vmError
        _bHalt = bHalt
        _lastLineExecuted = lastLineExecuted
        Log.d(TAG, "+..........CompletedRunningInstructions CountDown")
    }

    private fun completedParse(vmError: VMError?, commands: CommandList?) {
        // Save values on callback and release test thread
        Log.d(TAG, "+..........completedParse ")
        _vmError = vmError
        _commands = commands
        Log.d(TAG, "+..........completedParse CountDown")
    }

    companion object {
        private val TAG = VirtualMachineTest::class.java.name
        private const val NUM_COMMANDS_TO_RUN = 10
    }
}