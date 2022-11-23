package com.pedrocosta.exchangelog.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

public class BatchLauncher {
    public static JobExecution launch(ScheduledTask<?,?> task) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
            JobParametersInvalidException, JobRestartException {
        if (ObjectUtils.isEmpty(task)) {
            return null;
        }

        TaskChain taskChain = instantiateChain();
        taskChain.add(task);
        return launch(task);
    }

    public static JobExecution launch(Set<ScheduledTask<?,?>> tasks) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
            JobParametersInvalidException, JobRestartException {
        if (CollectionUtils.isEmpty(tasks)) {
            return null;
        }

        TaskChain taskChain = instantiateChain();
        taskChain.addAll(tasks);
        return launch(taskChain);
    }

    public static JobExecution launch(TaskChain tasks) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
            JobParametersInvalidException, JobRestartException {
        if (CollectionUtils.isEmpty(tasks)) {
            return null;
        }

        ScheduledTask<?,?> firstTask = tasks.stream().findFirst().get();

        JobBuilderFactory jobBuilderFactory = firstTask.getJobBuilderFactory();
        JobLauncher jobLauncher = firstTask.getJobLauncher();

        // Create job builder including first step
        SimpleJobBuilder jobBuilder = jobBuilderFactory.get(tasks.getName())
                .start(firstTask.getStep());
        // Include other steps
        tasks.stream().skip(1).forEachOrdered(task ->
                jobBuilder.next(task.getStep()));

        Job job = jobBuilder.build();
        return jobLauncher.run(job, new JobParametersBuilder()
                .addDate("date", new Date()) // create new parameter
                .toJobParameters());
    }

    private static TaskChain instantiateChain() {
        TaskChain taskChain = new TaskChain();
        taskChain.setName("generated_task");
        return taskChain;
    }
}
