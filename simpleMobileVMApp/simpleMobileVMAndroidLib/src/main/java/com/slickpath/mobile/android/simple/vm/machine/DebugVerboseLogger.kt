package com.slickpath.mobile.android.simple.vm.machine

import android.util.Log

interface IDebugVerboseLogger {
    fun debugVerbose(tag: String, text: String)
    fun debugVerbose(tag: String, text: () -> String)
    fun debug(tag: String, text: String)
    fun debug(tag: String, text: () -> String)

    var debugDump: Boolean
}

class DebugVerboseLogger(
    private val debug: Boolean,
    private val debugVerbose: Boolean
) : IDebugVerboseLogger {

    override var debugDump = false

    /**
     * If debugVerbose is enabled write the output to a log
     *
     * @param tag  - the Log.d LOG_TAG to use
     * @param text test to log
     */
    override fun debugVerbose(tag: String, text: String) {
        if (debugVerbose) {
            debug(tag, text)
        }
    }

    /**
     * If debugVerbose is enabled write the output to a log
     *
     * @param tag  - the Log.d LOG_TAG to use
     * @param text callback to get text is debugVerbose & debug is enabled
     */
    override fun debugVerbose(tag: String, text: () -> String) {
        if (debugVerbose && debug) {
            debug(tag, text())
        }
    }

    /**
     * If debug is enabled write the output to a log
     *
     * @param tag  the Log.d LOG_TAG to use
     * @param text string to log if debug is enabled
     */
    override fun debug(tag: String, text: String) {
        if (debug) {
            Log.d(tag, text)
        }
    }

    /**
     * If debugVerbose is enabled write the output to a log
     *
     * @param tag  - the Log.d LOG_TAG to use
     * @param text callback to get text is debug is enabled
     */
    override fun debug(tag: String, text: () -> String) {
        if (debug) {
            debug(tag, text())
        }
    }
}