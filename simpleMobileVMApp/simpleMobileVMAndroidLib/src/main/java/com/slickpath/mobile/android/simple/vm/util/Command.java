package com.slickpath.mobile.android.simple.vm.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * The Command object is the pairing of a commandId to a list of parameters
 * Immutable
 *
 * @author Pete Procopio
 */
public class Command {

    @NonNull
    private final Integer _commandId;
    @NonNull
    private final List<Integer> _parameters;

    /**
     * Constructor
     */
    public Command(final Integer commandId, @Nullable List<Integer> params) {
        _commandId = commandId;
        if (params == null) {
            params = new ArrayList<>();
        }
        _parameters = params;
        if (_parameters.size() == 0) {
            _parameters.add(null);
        }
    }

    /**
     * @return the command Id
     */
    @NonNull
    public Integer getCommandId() {
        return _commandId;
    }

    /**
     * @return the parameters
     */
    @NonNull
    public List<Integer> getParameters() {
        return _parameters;
    }

}
