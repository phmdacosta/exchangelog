package com.pedrocosta.exchangelog.batch;

import com.pedrocosta.exchangelog.ExchangelogApplication;
import org.junit.jupiter.api.BeforeEach;
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
public class JobFactoryTest {

    @Autowired
    private ApplicationContext context;

    private JobFactory<TestTask> jobFactory;

    @BeforeEach
    public void setUp() {
        this.jobFactory = new JobFactory<>(context);
    }

    @Test
    public void testTestTaskCreationWithClass() {
        assert jobFactory.create(TestTask.class) != null;
    }

    @Test
    public void testTestTaskCreationWithName() {
        assert jobFactory.create("TestTask") != null;
    }

    private static class TestTask extends ScheduledTask<Object, Object> {

        @Override
        public Object doRead() {
            return null;
        }

        @Override
        public Object doProcess(Object o) {
            return null;
        }

        @Override
        public void doWrite(Object o) {

        }
    }
}
