package com.slickpath.mobile.android.simple.vm.parser

import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

abstract class CachedThreadPoolParser(private val parserListener: ParserListener? = null): Parser {

    companion object {
        private val executorPool = Executors.newCachedThreadPool() as ThreadPoolExecutor
    }

    override fun parse() {
        executorPool.execute { parserListener?.completedParse(parseInstructions()) }
    }

    abstract fun parseInstructions(): ParseResult
}