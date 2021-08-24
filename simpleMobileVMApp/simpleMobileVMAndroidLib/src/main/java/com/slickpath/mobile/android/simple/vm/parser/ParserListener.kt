package com.slickpath.mobile.android.simple.vm.parser

/**
 * An interface allowing a calling entity of SimpleParser to listen for the completedParse event after SimpleParser.parse() is called
 *
 * @author Pete Procopio
 * @see com.slickpath.mobile.android.simple.vm.parser.SimpleParser
 *
 * @see com.slickpath.mobile.android.simple.vm.VMError
 */
interface ParserListener {
    /**
     * Called when SimpleParser.parse() is completed so the caller can react to the completion or error state of parse
     *
     * @param parseResult - result of parse [com.slickpath.mobile.android.simple.vm.parser.ParseResult]]
     */
    fun completedParse(parseResult: ParseResult)
}