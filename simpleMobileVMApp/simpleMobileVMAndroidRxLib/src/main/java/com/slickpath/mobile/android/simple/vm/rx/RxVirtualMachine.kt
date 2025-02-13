package com.slickpath.mobile.android.simple.vm.rx

import android.content.Context
import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.VMListener
import com.slickpath.mobile.android.simple.vm.machine.VirtualMachine
import com.slickpath.mobile.android.simple.vm.parser.ParseResult
import com.slickpath.mobile.android.simple.vm.parser.Parser
import com.slickpath.mobile.android.simple.vm.parser.ParserListener
import com.slickpath.mobile.android.simple.vm.util.Command
import com.slickpath.mobile.android.simple.vm.util.CommandList
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.runBlocking

@Suppress("MemberVisibilityCanBePrivate", "unused")
class RxVirtualMachine(context: Context) {

    private val vm = VirtualMachine(context)

    fun addCommand(command: Command): Single<Boolean> {
        return Single.create { emitter ->
            vm.addCommand(command)
            emitter.onSuccess(true)
        }
    }

    private val rxVMListener = RxVMListener()

    fun addCommands(commands: CommandList?): Single<Int> {
        return Single.create { emitter ->
            rxVMListener.addListener(object : VMListener {
                override fun completedAddingInstructions(
                    vmError: VMError?,
                    instructionsAdded: Int
                ) {
                    if (vmError != null) {
                        throw vmError
                    }
                    rxVMListener.removeListener(this)
                    emitter.onSuccess(instructionsAdded)
                }

                override fun completedRunningInstructions(
                    bHalt: Boolean,
                    lastLineExecuted: Int,
                    vmError: VMError?
                ) {
                    rxVMListener.removeListener(this)
                }

            })
            vm.vmListener = rxVMListener
            vm.addCommands(commands)
        }
    }

    fun addCommands(parser: Parser): Single<Int> {
        return Single.create<ParseResult> { emitter ->
            parser.addParserListener(object : ParserListener {
                override fun completedParse(parseResult: ParseResult) {
                    if (parseResult.vmError != null) {
                        parseResult.vmError?.let {
                            throw it
                        }
                    }
                    parser.removeParserListener(this)
                    emitter.onSuccess(parseResult)
                }
            })
            runBlocking {
                parser.parse()
            }
        }.flatMap { parseResult ->
            addCommands(parseResult.commands)
        }
    }

    fun runNextInstruction(): Single<VirtualMachine.Results> {
        return Single.create { emitter ->
            emitter.onSuccess(vm.runNextInstruction())
        }
    }

    fun runInstructions(): Single<VirtualMachine.Results> {
        return Single.create { emitter ->
            rxVMListener.addListener(object : VMListener {
                override fun completedAddingInstructions(
                    vmError: VMError?,
                    instructionsAdded: Int
                ) {
                    rxVMListener.removeListener(this)
                }

                override fun completedRunningInstructions(
                    bHalt: Boolean,
                    lastLineExecuted: Int,
                    vmError: VMError?
                ) {
                    if (vmError != null) {
                        throw vmError
                    }
                    rxVMListener.removeListener(this)
                    emitter.onSuccess(VirtualMachine.Results(bHalt, lastLineExecuted, vmError))
                }

            })
            vm.vmListener = rxVMListener
            vm.runInstructions()
        }
    }

    fun runInstructions(numInstructionsToRun: Int): Single<VirtualMachine.Results> {
        return Single.create { emitter ->
            rxVMListener.addListener(object : VMListener {
                override fun completedAddingInstructions(
                    vmError: VMError?,
                    instructionsAdded: Int
                ) {
                    rxVMListener.removeListener(this)
                }

                override fun completedRunningInstructions(
                    bHalt: Boolean,
                    lastLineExecuted: Int,
                    vmError: VMError?
                ) {
                    if (vmError != null) {
                        throw vmError
                    }
                    rxVMListener.removeListener(this)
                    emitter.onSuccess(VirtualMachine.Results(bHalt, lastLineExecuted, vmError))
                }

            })
            vm.vmListener = rxVMListener
            vm.runInstructions(numInstructionsToRun)
        }
    }

    fun getCommandAt(location: Int): Command {
        return vm.getCommandAt(location)
    }

    private class RxVMListener: VMListener {

        private val vmListeners: MutableSet<VMListener> = mutableSetOf()

        fun addListener(vmListener: VMListener) {
            vmListeners.add(vmListener)
        }

        fun removeListener(vmListener: VMListener) {
            vmListeners.remove(vmListener)
        }

        override fun completedAddingInstructions(vmError: VMError?, instructionsAdded: Int) {
            vmListeners.forEach {
                it.completedAddingInstructions(vmError, instructionsAdded)
            }
        }

        override fun completedRunningInstructions(
            bHalt: Boolean,
            lastLineExecuted: Int,
            vmError: VMError?
        ) {
            vmListeners.forEach {
                it.completedRunningInstructions(bHalt, lastLineExecuted, vmError)
            }
        }
    }
}