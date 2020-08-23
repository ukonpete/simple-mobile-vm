package com.slickpath.mobile.android.simple.vm.util

/**
 * The Command object is the pairing of a commandId to a list of parameters
 *
 *
 * Immutable
 *
 * @author Pete Procopio
 */
class Command(val commandId: Int, params: List<Int>?) {

    val parameters: List<Int> = params?.toList() ?: listOf()
}