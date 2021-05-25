package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.ExchangelogApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

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
