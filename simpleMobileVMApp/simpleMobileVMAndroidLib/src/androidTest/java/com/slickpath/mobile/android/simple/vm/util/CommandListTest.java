package com.slickpath.mobile.android.simple.vm.util;


import android.support.test.runner.AndroidJUnit4;

import com.slickpath.mobile.android.simple.vm.instructions.Instructions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * @author Pete Procopio
 */
@RunWith(AndroidJUnit4.class)
public class CommandListTest {

    private static final int TEST_PARAM_VAL1 = 15;
    private static final int TEST_PARAM_VAL2 = 25;
    private final Integer COMMAND_1 = Instructions._PUSHC;
    private final Integer COMMAND_PARAM_VAL_1 = TEST_PARAM_VAL1;
    private final List<Integer> COMMAND_PARAM_LIST_1 = new ArrayList<>(Collections.singletonList(COMMAND_PARAM_VAL_1));
    private final Integer COMMAND_2 = Instructions._PUSH;
    private final Integer COMMAND_PARAM_VAL_2 = TEST_PARAM_VAL2;
    private final List<Integer> COMMAND_PARAM_LIST_2 = new ArrayList<>(Collections.singletonList(COMMAND_PARAM_VAL_2));
    private final Integer COMMAND_3 = Instructions._ADD;
    private final List<Integer> COMMAND_PARAM_LIST_3 = null;
    private CommandList testList1 = null;

    @Before
    public void before() throws Exception {
        testList1 = new CommandList();
        testList1.add(new Command(COMMAND_1, COMMAND_PARAM_LIST_1));
        testList1.add(COMMAND_2, COMMAND_PARAM_LIST_2);
        testList1.add(COMMAND_3, COMMAND_PARAM_LIST_3);
    }

    @Test
    public void testCommands() {
        assertNotNull(testList1);
        assertEquals(testList1.size(), 3);
        assertNotNull(testList1.get(0));
        assertEquals(testList1.get(0).getCommandId(), COMMAND_1);
        assertNotNull(testList1.get(0).getParameters());
        assertEquals(testList1.get(0).getParameters().size(), 1);
        assertEquals(testList1.get(0).getParameters().get(0), COMMAND_PARAM_VAL_1);

        assertNotNull(testList1.get(1));
        assertEquals(testList1.get(1).getCommandId(), COMMAND_2);
        assertNotNull(testList1.get(1).getParameters());
        assertEquals(testList1.get(1).getParameters().size(), 1);
        assertEquals(testList1.get(1).getParameters().get(0), COMMAND_PARAM_VAL_2);

        assertNotNull(testList1.get(2));
        assertEquals(testList1.get(2).getCommandId(), COMMAND_3);
        assertNull(testList1.get(2).getParameters().get(0));
    }

}
