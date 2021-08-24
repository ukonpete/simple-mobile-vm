package com.slickpath.mobile.android.simple.vm.parser

import com.slickpath.mobile.android.simple.vm.VMError
import com.slickpath.mobile.android.simple.vm.util.CommandList

/**
 *
 * @param vmError  - If an error is seen this object contains error information, else returns Null
 * @param commands - The CommandList of the parsed data
 *
*/
data class ParseResult(val vmError: VMError?, val commands: CommandList)
