package com.slickpath.mobile.android.simple.vm.parser

interface Parser {

    suspend fun parse(): ParseResult
}