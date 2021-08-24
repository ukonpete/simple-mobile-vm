package com.slickpath.mobile.android.simple.vm.parser

interface Parser {

    fun parse()

    fun addParserListener(parserListener: ParserListener)

    fun removeParserListener(parserListener: ParserListener)
}