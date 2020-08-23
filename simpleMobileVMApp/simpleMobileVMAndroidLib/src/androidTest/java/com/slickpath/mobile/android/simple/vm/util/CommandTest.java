package com.slickpath.mobile.android.simple.vm.util;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.slickpath.mobile.android.simple.vm.instructions.Instructions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4.class)
public class CommandTest {

    private static final int TEST_PARAM_VAL = 15;
    private final Integer COMMAND_1 = Instructions._PUSHC;
    private final Integer COMMAND_PARAM_VAL_1 = TEST_PARAM_VAL;
    private final List<Integer> COMMAND_PARAM_LIST_1 = new ArrayList<>(Collections.singletonList(COMMAND_PARAM_VAL_1));
    private final Integer COMMAND_2 = Instructions._ADD;
    private final List<Integer> COMMAND_PARAM_LIST_2 = null;
    private Command _commandWithParam = null;
    private Command _commandWithOutParam = null;

    @Before
    public void before() {
        _commandWithParam = new Command(COMMAND_1, COMMAND_PARAM_LIST_1);
        _commandWithOutParam = new Command(COMMAND_2, COMMAND_PARAM_LIST_2);
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.util.Command#getCommandId()}.
     */
    @Test
    public void testGetCommandId() {
        assertEquals(_commandWithParam.getCommandId(), COMMAND_1);
        assertEquals(_commandWithOutParam.getCommandId(), COMMAND_2);
    }

    /**
     * Test method for {@link com.slickpath.mobile.android.simple.vm.util.Command#getParameters()}.
     */
    @Test
    public void testGetParameters() {
        assertNotNull(_commandWithParam.getParameters());
        assertEquals(_commandWithParam.getParameters().get(0), COMMAND_PARAM_VAL_1);
        assertNotNull(_commandWithOutParam.getParameters());
    }

}
