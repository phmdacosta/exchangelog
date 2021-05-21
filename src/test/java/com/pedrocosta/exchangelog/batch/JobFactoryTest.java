package com.pedrocosta.exchangelog.batch;

import com.pedrocosta.exchangelog.ExchangelogApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
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
public class JobFactoryTest {

    @Autowired
    private ApplicationContext context;

    private JobFactory jobFactory;

    @BeforeEach
    public void setUp() {
        this.jobFactory = new JobFactory(context);
    }

    @Test
    public void testTestTaskCreationWithClass() {
        assert jobFactory.create(TestTask.class) instanceof TestTask;
    }

    @Test
    public void testTestTaskCreationWithName() {
        assert jobFactory.create("TestTask") instanceof TestTask;
    }

    private static class TestTask extends ScheduledTask<Object, Object> {

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
