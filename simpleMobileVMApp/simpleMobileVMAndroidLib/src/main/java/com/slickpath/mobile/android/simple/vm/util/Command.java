package com.slickpath.mobile.android.simple.vm.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * The Command object is the pairing of a commandId to a list of parameters
 * <p>
 * Immutable
 *
 * @author Pete Procopio
 */
public class Command {

    @NonNull
    private final Integer commandId;
    @NonNull
    private final List<Integer> parameters;

    /**
     * Constructor
     */
    public Command(final @NonNull Integer commandId, @Nullable List<Integer> params) {
        this.commandId = commandId;
        if (params == null) {
            params = new ArrayList<>();
        }
        parameters = params;
        if (parameters.size() == 0) {
            parameters.add(null);
        }
    }

    /**
     * @return the command Id
     */
    @NonNull
    public Integer getCommandId() {
        return commandId;
    }

    /**
     * @return the parameters
     */
    @NonNull
    public List<Integer> getParameters() {
        return parameters;
    }

}
