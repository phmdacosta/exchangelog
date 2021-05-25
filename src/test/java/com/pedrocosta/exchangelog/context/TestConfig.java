package com.pedrocosta.exchangelog.context;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
@TestConfiguration
public class TestConfig {

    @Bean
    public TestTask testTask() {
        return new TestTask();
    }

    @Component
    public static class TestTask extends ScheduledTask<Object, Object> {

        @Override
        public Object doRead() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            return null;
        }

        @Override
        public Object doProcess(Object o) throws Exception {
            return null;
        }

        @Override
        public void doWrite(Object o) throws Exception {

        }
    }
}
