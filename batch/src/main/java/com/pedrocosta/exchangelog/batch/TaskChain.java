package com.pedrocosta.exchangelog.batch;

import com.sun.istack.Nullable;
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

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;

public class TaskChain extends LinkedHashSet<ScheduledTask<Object, Object>> {

    private String name;
    private JobBuilderFactory jobBuilderFactory;
    private JobLauncher jobLauncher;

    public void setName(String name) {
        this.name = name + "_chain";
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean add(ScheduledTask<Object, Object> obj) {
        if (getName() == null) {
            setName(obj.getJobName());
        }

//        if (jobBuilderFactory == null) {
//            if (obj.getJobBuilderFactory() != null) {
//                jobBuilderFactory = obj.getJobBuilderFactory();
//            }
//        }
//
//        if (jobLauncher == null) {
//            if (obj.getJobLauncher() != null) {
//                jobLauncher = obj.getJobLauncher();
//            }
//        }

        return super.add(obj);
    }

    @Override
    public boolean addAll(Collection<? extends ScheduledTask<Object, Object>> col) {
        if (col == null) {
            return false;
        }

        if (!col.isEmpty()) {
            ScheduledTask<Object, Object> first = col.stream().findFirst().get();

            if (getName() == null) {
                setName(first.getJobName());
            }

//            if (jobBuilderFactory == null) {
//                if (first.getJobBuilderFactory() != null) {
//                    jobBuilderFactory = first.getJobBuilderFactory();
//                }
//            }
//
//            if (jobLauncher == null) {
//                if (first.getJobLauncher() != null) {
//                    jobLauncher = first.getJobLauncher();
//                }
//            }
        }

        return super.addAll(col);
    }

//    @Nullable
//    public JobExecution execute() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
//                                         JobRestartException, JobInstanceAlreadyCompleteException {
//        if (!this.isEmpty()) {
//            // Create job builder including first step
//            SimpleJobBuilder jobBuilder = jobBuilderFactory.get(getName())
//                    .start(this.stream().findFirst().get().getStep());
//            // Include other steps
//            this.stream().skip(1).forEachOrdered(task ->
//                    jobBuilder.next(task.getStep()));
//
//            Job job = jobBuilder.build();
//            return jobLauncher.run(job, new JobParametersBuilder()
//                    .addDate("date", new Date()) // create new parameter
//                    .toJobParameters());
//        }
//
//        return null;
//    }

    @Nullable
    public JobExecution executeAll() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        if (!this.isEmpty()) {
            ScheduledTask<?,?> firstTask = this.stream().findFirst().orElse(null);

            JobBuilderFactory jobBuilderFactory = firstTask.getJobBuilderFactory();
            JobLauncher jobLauncher = firstTask.getJobLauncher();

            // Create job builder including first step
            SimpleJobBuilder jobBuilder = jobBuilderFactory.get(getName())
                    .start(firstTask.getStep());
            // Include other steps
            this.stream().skip(1).forEachOrdered(task ->
                    jobBuilder.next(task.getStep()));

            Job job = jobBuilder.build();
            return jobLauncher.run(job, new JobParametersBuilder()
                    .addDate("date", new Date()) // create new parameter
                    .toJobParameters());
        }

        return null;
    }
}
