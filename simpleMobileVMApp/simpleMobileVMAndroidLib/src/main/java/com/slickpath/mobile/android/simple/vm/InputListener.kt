package com.slickpath.mobile.android.simple.vm

import java.io.IOException

/**
 * Listener for VM to retrieve input from
 *
 * @author Pete Procopio
 */
interface InputListener {

    /**
     * @return one int for input into VM
     */
    @get:Throws(IOException::class)
    val int: Int

    /**
     * @return one char for input into VM
     */
    @get:Throws(IOException::class)
    val char: Char
}