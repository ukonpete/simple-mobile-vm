package com.slickpath.mobile.android.simple.vm.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandList is a List Container that holds all the Commands
 * <p>
 * A Command consist of the pairing of:<p><ul>
 * <li>CommandId - Integer
 * <li>Parameters - List&lt;Integer&gt; (this can be empty for a particular command id)
 * </ul>
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
     * Takes commandId and parameters and adds an equivalent Command
     *
     * @param commandId id of command
     * @param parameters parameters for command
     */
    public void add(@NonNull final Integer commandId, final List<Integer> parameters) {
        final Command tempCommand = new Command(commandId, parameters);
        add(tempCommand);
    }

}
