package com.pedrocosta.exchangelog.batch;

import com.pedrocosta.exchangelog.ExchangelogApplication;
import com.pedrocosta.exchangelog.batch.jobs.SendNotificationQuoteByEmailTask;
import com.pedrocosta.exchangelog.models.ScheduledJob;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
@SpringBootTest(classes = ExchangelogApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
public class SendNotificationQuoteByEmailTaskTest {

    @Autowired
    private JobFactory<SendNotificationQuoteByEmailTask> jobFactory;

    @Test
    public void testTaskRun() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        SendNotificationQuoteByEmailTask task = jobFactory.create(SendNotificationQuoteByEmailTask.class);
        task.setSchedBatchJob(new ScheduledJob().setName("SendNotificationQuoteByEmailTaskTest"));
        JobExecution jobExecution = task.execute();
        assert BatchStatus.COMPLETED.equals(jobExecution.getStatus());
    }
}
