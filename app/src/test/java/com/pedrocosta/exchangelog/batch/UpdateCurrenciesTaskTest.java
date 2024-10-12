package com.pedrocosta.exchangelog.batch;

import com.pedrocosta.exchangelog.ExchangelogApplication;
import com.pedrocosta.exchangelog.currency.UpdateCurrenciesTask;
import org.junit.jupiter.api.*;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateCurrenciesTaskTest {

    @Autowired
    private ApplicationContext context;
    private JobFactory<UpdateCurrenciesTask> jobFactory;

    @BeforeEach
    public void setUp() {
        this.jobFactory = new JobFactory<>(context);
    }

    @Test
    @Order(1)
    public void testJobFactoryNotNull() {
        assert this.jobFactory != null;
    }

    @Test
    @Order(2)
    public void testTaskRun() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        UpdateCurrenciesTask task = jobFactory.create(UpdateCurrenciesTask.class);
        task.setSchedBatchJob(new ScheduledJob().setName("UpdateCurrenciesTaskTest"));
        JobExecution jobExecution = task.execute();
        assert BatchStatus.COMPLETED.equals(jobExecution.getStatus());
    }
}
