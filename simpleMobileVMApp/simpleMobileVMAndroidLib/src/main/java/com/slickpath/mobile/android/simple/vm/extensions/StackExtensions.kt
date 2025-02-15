package com.slickpath.mobile.android.simple.vm.extensions

import java.util.Stack

fun <T> Stack<T>.dumpAsList(): List<T> {
    return ArrayList(this)
}