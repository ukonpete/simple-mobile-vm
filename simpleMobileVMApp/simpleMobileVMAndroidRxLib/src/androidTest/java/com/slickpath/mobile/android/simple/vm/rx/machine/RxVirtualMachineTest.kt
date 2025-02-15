package com.slickpath.mobile.android.simple.vm.rx.machine

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.instructions.Instructions
import com.slickpath.mobile.android.simple.vm.parser.ParseResult
import com.slickpath.mobile.android.simple.vm.parser.ParserListener
import com.slickpath.mobile.android.simple.vm.parser.SimpleParser
import com.slickpath.mobile.android.simple.vm.rx.RxSchedulersRule
import com.slickpath.mobile.android.simple.vm.rx.RxVirtualMachine
import com.slickpath.mobile.android.simple.vm.util.Command
import com.slickpath.mobile.android.simple.vm.util.CommandList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.CountDownLatch


/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class RxVirtualMachineTest {

    @Rule
    @JvmField
    var schedulersRule: RxSchedulersRule = RxSchedulersRule()

    private var _parseError: VMError? = null
    private var _bHalt = false
    private var _commands: CommandList? = null

    @Before
    fun before() {
        _bHalt = false
        _parseError = null
        _commands = null
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.addCommand].
     */
    @Test
    fun testAddCommand() {
        val virtualMachine = RxVirtualMachine(ApplicationProvider.getApplicationContext())
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
                val commandResult = virtualMachine.addCommand(command).blockingGet()
                assertTrue(commandResult)
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
        val virtualMachine = RxVirtualMachine(ApplicationProvider.getApplicationContext())
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
            val commandResult = virtualMachine.addCommands(commandList).blockingGet()
            assertEquals(6, commandResult)
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
        val virtualMachine = RxVirtualMachine(ApplicationProvider.getApplicationContext())
        val signalParse = CountDownLatch(1)
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.INSTRUCTIONS))
/*        parser.addParserListener(object : ParserListener {
            override fun completedParse(parseResult: ParseResult) {
                this@RxVirtualMachineTest.completedParse(parseResult)
                signalParse.countDown() // notify the count down latch
            }
        })*/
        assertNull(_parseError)

        Log.d(TAG, "+...........................PARSE START ")
        runBlocking {
            parser.parse()
        }
        try {
            // Wait for Callback
            Log.d(TAG, "+...........................PARSE WAIT ")
            signalParse.await()
            Log.d(TAG, "+...........................PARSE DONE WAIT ")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        Log.d(TAG, "+...........................VM ADD COMMANDS START")
        val commandsAdded = virtualMachine.addCommands(_commands).blockingGet()
        assertEquals(_commands?.size, commandsAdded)
        Log.d(TAG, "+...........................VM RUN INSTRS START")
        assertFalse(_bHalt)
        val runInstructionsResults =
            virtualMachine.runInstructions(NUM_COMMANDS_TO_RUN).blockingGet()
        assertEquals(NUM_COMMANDS_TO_RUN, runInstructionsResults.lastLineExecuted)
        var lastLineExecuted = -1
        for (i in 0..99) {
            try {
                val runNextResults = virtualMachine.runNextInstruction().blockingGet()
                assertFalse("($i) Halt= ", runNextResults.halt)
            } catch (vmError: VMError) {
                assertNull(
                    "(" + i + ") LINE=" + lastLineExecuted + " --> " + vmError.message,
                )
            }
            lastLineExecuted++
        }
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.runInstructions].
     */
    @Test
    fun testRunInstructions() {
        val virtualMachine = RxVirtualMachine(ApplicationProvider.getApplicationContext())
        val signalParse = CountDownLatch(1)
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.INSTRUCTIONS))
        parser.addParserListener(object : ParserListener {
            override fun completedParse(parseResult: ParseResult) {
                this@RxVirtualMachineTest.completedParse(parseResult)
                signalParse.countDown() // notify the count down latch
            }
        })
        assertNull(_parseError)
        runBlocking {
            parser.parse()
        }
        try {
            // Wait for Callback
            signalParse.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        val commandResult = virtualMachine.addCommands(_commands).blockingGet()
        assertEquals(35, commandResult)
        val results = virtualMachine.runInstructions().blockingGet()
        assertEquals(35, results.lastLineExecuted)

    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.runInstructions].
     */
    @Test
    fun testRunInstructionsInt() {
        Log.d(TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
        val virtualMachine = RxVirtualMachine(ApplicationProvider.getApplicationContext())
        val signalParse = CountDownLatch(1)
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.INSTRUCTIONS))
        parser.addParserListener(object : ParserListener {
            override fun completedParse(parseResult: ParseResult) {
                this@RxVirtualMachineTest.completedParse(parseResult)
                signalParse.countDown() // notify the count down latch
            }
        })
        assertNull(_parseError)
        runBlocking {
            parser.parse()
        }
        try {
            // Wait for Callback
            signalParse.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        val numInstructionsAdded = virtualMachine.addCommands(_commands).blockingGet()
        assertEquals(_commands?.size, numInstructionsAdded)
        val results = virtualMachine.runInstructions(10).blockingGet()
        Log.d(TAG, "+......... checking last line executed")
        assertEquals(10, results.lastLineExecuted)
        Log.d(TAG, "??????????????????????????????")
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.machine.VirtualMachine.runInstructions].
     */
    @Test
    fun testAddInstructionsWithParser() {
        Log.d(TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
        val virtualMachine = RxVirtualMachine(ApplicationProvider.getApplicationContext())
        val parser = SimpleParser(FileHelperForTest(FibonacciInstructions.INSTRUCTIONS))
        //val numInstructionsAdded = virtualMachine.addCommands(parser).blockingGet()
        //assertEquals(35, numInstructionsAdded)
        val results = virtualMachine.runInstructions().blockingGet()
        Log.d(TAG, "+......... checking last line executed")
        assertEquals(35, results.lastLineExecuted)
        Log.d(TAG, "??????????????????????????????")
    }

    private fun completedParse(parseResult: ParseResult) {
        // Save values on callback and release test thread
        Log.d(TAG, "+..........completedParse ")
        _parseError = parseResult.vmError
        _commands = parseResult.commands
        Log.d(TAG, "+..........completedParse CountDown")
    }

    companion object {
        private val TAG = RxVirtualMachineTest::class.java.name
        private const val NUM_COMMANDS_TO_RUN = 10
    }
}