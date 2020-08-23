package com.slickpath.mobile.android.simple.vm.machine

import java.util.*

fun <T> Stack<T>.dumpAsList(): List<T> {
    return ArrayList(this)
}