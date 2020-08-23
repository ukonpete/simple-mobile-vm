package com.slickpath.mobile.android.simple.vm

import java.io.IOException

/**
 * DefaultInputListener that uses the console input via System.in.read
 *
 * @author Pete Procopio
 */
class DefaultInputListener : InputListener {

    @get:Throws(IOException::class)
    override val int: Int
        get() = System.`in`.read()

    @get:Throws(IOException::class)
    override val char: Char
        get() = System.`in`.read().toChar()
}