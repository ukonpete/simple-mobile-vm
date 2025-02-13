package com.slickpath.mobile.android.simple.vm.parser

interface Parser {

    suspend fun parse()

    fun addParserListener(parserListener: ParserListener)

    fun removeParserListener(parserListener: ParserListener)
}