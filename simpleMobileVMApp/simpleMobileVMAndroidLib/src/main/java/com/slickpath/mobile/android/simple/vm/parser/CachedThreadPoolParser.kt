package com.slickpath.mobile.android.simple.vm.parser

import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

abstract class CachedThreadPoolParser: Parser {

    private var parserListeners: MutableSet<ParserListener> = mutableSetOf()

    companion object {
        private val executorPool = Executors.newCachedThreadPool() as ThreadPoolExecutor
    }

    override fun parse() {
        executorPool.execute {
            parserListeners.forEach { parserListener ->
                parserListener.completedParse(parseInstructions())
            }

        }
    }

    abstract fun parseInstructions(): ParseResult

    override fun addParserListener(parserListener: ParserListener) {
        this.parserListeners.add(parserListener)
    }

    override fun removeParserListener(parserListener: ParserListener) {
        this.parserListeners.remove(parserListener)
    }

}