package com.slickpath.mobile.android.simple.vm.parser

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.instructions.Instructions
import com.slickpath.mobile.android.simple.vm.machine.FibonacciInstructions
import com.slickpath.mobile.android.simple.vm.machine.FileHelperForTest
import com.slickpath.mobile.android.simple.vm.util.CommandList
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4::class)
class SimpleParserTest : IParserListener {
    private var _parser: SimpleParser? = null
    private var _signal: CountDownLatch? = null
    private var _error: VMError? = null
    private var _commands: CommandList? = null
    @Before
    fun before() {
        _parser = SimpleParser(FileHelperForTest(FibonacciInstructions.instructions), this)
        _signal = CountDownLatch(1)
    }

    /**
     * Test method for [com.slickpath.mobile.android.simple.vm.parser.SimpleParser.parse].
     */
    @Test
    fun testParse() {
        _parser!!.parse()
        try {
            // Wait for Callback
            _signal!!.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            fail(e.message)
        } // wait for callback
        assertNull(_error)
        assertNotNull(_commands)
        assertEquals(35, _commands!!.size)
        var i = 0
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSHC) // 1
        assertNotNull(_commands!![i]!!.parameters) // 1
        assertEquals(1, _commands!![i]!!.parameters.size) // 1
        assertEquals(HALT_LINE_NUMBER, _commands!![i++]!!.parameters[0]) // 1
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSHC) // 2
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(0, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i++]!!.commandId, Instructions.WRINT) // 3
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSHC) // 4
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(1, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i++]!!.commandId, Instructions.WRINT) // 5
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSHC) // 6
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(1, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i++]!!.commandId, Instructions.WRINT) // 7
        // 8 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSHC) // 9
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(0, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i]!!.commandId, Instructions.POPC) // 10
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(10, _commands!![i++]!!.parameters[0])
        // 11 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSHC) // 12
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(1, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i]!!.commandId, Instructions.POPC) // 13
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(11, _commands!![i++]!!.parameters[0])
        // 14 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSH) // 15
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(11, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i]!!.commandId, Instructions.POPC) // 16
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(12, _commands!![i++]!!.parameters[0])
        // 17 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSHC) // 18
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(12, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i]!!.commandId, Instructions.POPC) // 19
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(13, _commands!![i++]!!.parameters[0])
        // 20 [FIB] - Symbol
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSH) // 21
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(11, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i]!!.commandId, Instructions.POPC) // 22
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(14, _commands!![i++]!!.parameters[0])
        // 23 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSH) // 24
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(14, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i]!!.commandId, Instructions.POPC) // 25
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(10, _commands!![i++]!!.parameters[0])
        // 26 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSH) // 27
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(12, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i]!!.commandId, Instructions.POPC) // 28
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(11, _commands!![i++]!!.parameters[0])
        // 29 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSH) // 30
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(12, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSH) // 31
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(10, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i++]!!.commandId, Instructions.ADD) // 32
        assertEquals(_commands!![i]!!.commandId, Instructions.POPC) // 33
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(12, _commands!![i++]!!.parameters[0])
        // 34 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSH) // 35
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(12, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i++]!!.commandId, Instructions.WRINT) // 36
        // 37 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSHC) // 38
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(1, _commands!![i++]!!.parameters[0])
        //
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSH) // 39
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(13, _commands!![i++]!!.parameters[0])
        //
        assertEquals(_commands!![i++]!!.commandId, Instructions.SUB) // 40
        assertEquals(_commands!![i]!!.commandId, Instructions.POPC) // 41
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(13, _commands!![i++]!!.parameters[0])
        // 42 - Comment
        // 43 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.PUSH) // 44
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(13, _commands!![i++]!!.parameters[0])
        assertEquals(_commands!![i]!!.commandId, Instructions.BREQL) // 45
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(HALT_LINE_NUMBER, _commands!![i++]!!.parameters[0])
        //
        // 46 - Comment
        assertEquals(_commands!![i]!!.commandId, Instructions.BRANCH) // 47
        assertNotNull(_commands!![i]!!.parameters)
        assertEquals(1, _commands!![i]!!.parameters.size)
        assertEquals(FIB_LINE_NUMBER, _commands!![i++]!!.parameters[0])
        // 48 - Comment
        // 49 [HALT] - Symbol
        assertEquals(_commands!![i]!!.commandId, Instructions.HALT) // 50
    }

    override fun completedParse(vmError: VMError?, commands: CommandList?) {
        // Save values on callback and release test thread
        _error = vmError
        _commands = commands
        _signal!!.countDown() // notify the count down latch
    }

    companion object {
        private const val FIB_LINE_NUMBER = 15
        private const val HALT_LINE_NUMBER = 34
    }
}