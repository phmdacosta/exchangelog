package com.pedrocosta.exchangelog.utils;

import org.junit.jupiter.api.Test;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class MessagePropertiesTest {

    private final String msgTestKey = "test.message";
    private final String[] msgTestArgs = {"Test1", "QWERTY"};
    private final String expectedMsg = "Test message Test1 QWERTY";

    @Test
    public void testGetMassage() {
        String msg = Messages.get(msgTestKey, msgTestArgs);
        assert expectedMsg.equals(msg);
    }
}
