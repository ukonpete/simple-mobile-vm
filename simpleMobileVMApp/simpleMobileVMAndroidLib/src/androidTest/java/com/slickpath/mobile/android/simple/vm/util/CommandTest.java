package com.slickpath.mobile.android.simple.vm.util;


import android.support.annotation.Nullable;
import android.test.AndroidTestCase;

import com.slickpath.mobile.android.simple.vm.instructions.Instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Pete Procopio
 */
public class CommandTest extends AndroidTestCase {

    private static final int TEST_PARAM_VAL = 15;
    private final Integer COMMAND_1 = Instructions._PUSHC;
    private final Integer COMMAND_PARAM_VAL_1 = TEST_PARAM_VAL;
    private final List<Integer> COMMAND_PARAM_LIST_1 = new ArrayList<>(Collections.singletonList(COMMAND_PARAM_VAL_1));
    private final Integer COMMAND_2 = Instructions._ADD;
    @Nullable
    private final List<Integer> COMMAND_PARAM_LIST_2 = null;
    @Nullable
    private Command _commandWithParam = null;
    @Nullable
    private Command _commandWithOutParam = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _commandWithParam = new Command(COMMAND_1, COMMAND_PARAM_LIST_1);
        _commandWithOutParam = new Command(COMMAND_2, COMMAND_PARAM_LIST_2);
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.util.Command#getCommandId()}.
     */
    public void testGetCommandId() {
        assertEquals(_commandWithParam.getCommandId(), COMMAND_1);
        assertEquals(_commandWithOutParam.getCommandId(), COMMAND_2);
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.util.Command#getParameters()}.
     */
    public void testGetParameters() {
        assertNotNull(_commandWithParam.getParameters());
        assertEquals(_commandWithParam.getParameters().get(0), COMMAND_PARAM_VAL_1);
        assertNotNull(_commandWithOutParam.getParameters());
    }

}
