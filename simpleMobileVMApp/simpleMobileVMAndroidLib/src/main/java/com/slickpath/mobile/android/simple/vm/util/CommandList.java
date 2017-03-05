package com.slickpath.mobile.android.simple.vm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandList is a List Container that holds all the Commands
 * A Command consist of the pairing of:
 * CommandId - Integer
 * Parameters - List<Integer> (this can be empty for a particular command id)
 *
 * @author Pete Procopio
 * @see com.slickpath.mobile.android.simple.vm.util.Command
 */
public class CommandList extends ArrayList<Command> {

    /**
     * Constructor
     */
    public CommandList() {
    }

    /**
     * takes commandId and parameters and adds an equivalent Command
     *
     * @param commandId id of command
     * @param parameters parameters for command
     */
    public void add(final Integer commandId, final List<Integer> parameters) {
        final Command tempCommand = new Command(commandId, parameters);
        add(tempCommand);
    }

}
