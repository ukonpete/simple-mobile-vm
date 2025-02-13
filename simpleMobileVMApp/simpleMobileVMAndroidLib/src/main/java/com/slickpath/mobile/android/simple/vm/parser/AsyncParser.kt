package com.slickpath.mobile.android.simple.vm.parser

abstract class AsyncParser: Parser {

    private var parserListeners: MutableSet<ParserListener> = mutableSetOf()

    override suspend fun parse() {
        val parseResult = parseInstructions()
        parserListeners.forEach { parserListener ->
            parserListener.completedParse(parseResult)
        }
    }

    abstract suspend fun parseInstructions(): ParseResult

    override fun addParserListener(parserListener: ParserListener) {
        this.parserListeners.add(parserListener)
    }

    override fun removeParserListener(parserListener: ParserListener) {
        this.parserListeners.remove(parserListener)
    }

}