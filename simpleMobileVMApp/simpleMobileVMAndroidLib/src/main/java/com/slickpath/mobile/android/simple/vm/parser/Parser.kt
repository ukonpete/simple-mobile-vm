package com.slickpath.mobile.android.simple.vm.parser

import kotlinx.coroutines.flow.Flow

interface Parser {

    suspend fun parse() : Flow<ParseResult>
}