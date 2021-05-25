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
@SpringBootTest(classes = ExchangelogApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
public class MessagePropertiesTest {

    @Autowired
    private ApplicationContext context;

    private final String msgTestKey = "test.message";
    private final String[] msgTestArgs = {"Test1", "QWERTY"};
    private final String expectedMsg = "Test message Test1 QWERTY";

    @Test
    public void testGetMassageWithoutContext() {
        String msg = MessageProperties.get(msgTestKey, msgTestArgs);
        assert expectedMsg.equals(msg);
    }

    @Test
    public void testGetMessageWithContext() {
        String msg = MessageProperties.get(context, msgTestKey, msgTestArgs);
        assert expectedMsg.equals(msg);
    }
}
