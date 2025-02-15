package com.slickpath.mobile.android.simple.vm.util

/**
 * CommandList is a List Container that holds all the Commands
 *
 *
 * A Command consist of the pairing of:
 *
 *
 *  * CommandId - Integer
 *  * Parameters - List&lt;Integer&gt; (this can be empty for a particular command id)
 *
 *
 * @author Pete Procopio
 * @see com.slickpath.mobile.android.simple.vm.util.Command
 */
class CommandList : ArrayList<Command>() {
    /**
     * Takes commandId and parameters and adds an equivalent Command
     *
     * @param commandId id of command
     * @param parameters parameters for command
     */
    fun add(commandId: Int, parameters: List<Int>?) {
        add(Command(commandId, parameters))
    }
}