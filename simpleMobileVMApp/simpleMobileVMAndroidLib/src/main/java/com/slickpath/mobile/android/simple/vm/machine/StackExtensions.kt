package com.slickpath.mobile.android.simple.vm.machine

import java.util.*

fun Stack<Int>.dump(): List<Int> {
    return ArrayList(this)
}