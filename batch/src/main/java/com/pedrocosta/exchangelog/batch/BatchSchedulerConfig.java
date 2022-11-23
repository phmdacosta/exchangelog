package com.pedrocosta.exchangelog.batch;

import com.pedrocosta.exchangelog.batch.annotation.ScheduledTask;
import com.pedrocosta.exchangelog.batch.service.ScheduledBatchJobService;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.utils.PropertyNames;
import com.pedrocosta.springutils.PackageUtils;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Configuration class for SpringBoot to configure Scheduled Tasks.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
@Configuration
@EnableBatchProcessing
@EnableScheduling
@EnableJpaRepositories
@ComponentScan
@EntityScan
public class BatchSchedulerConfig implements SchedulingConfigurer {

    private final ApplicationContext context;
    private final ScheduledBatchJobService scheduledJobService;
    private final JobExplorer jobExplorer;

    public BatchSchedulerConfig(ApplicationContext context,
                                ScheduledBatchJobService scheduledJobService,
                                JobExplorer jobExplorer) {
        this.context = context;
        this.scheduledJobService = scheduledJobService;
        this.jobExplorer = jobExplorer;
        cancelUndoneTasks();
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }

    /**
     * Configure scheduled tasks to execute it in set schedule.
     *
     * @param taskRegistrar {@link ScheduledTaskRegistrar} object.
     */
    @Override
    public void configureTasks(@NotNull ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());

        try {
            String projPackageStr = PackageUtils.getProjectPackage(context).getName();
            configureImplementedJobsFromProject(taskRegistrar, projPackageStr);
        } catch (ClassNotFoundException e) {
            Log.error(this, e);
            configureSavedJobs(taskRegistrar);
        }
    }

    private void configureSavedJobs(final ScheduledTaskRegistrar taskRegistrar) {
        List<String> jobNameList = scheduledJobService.findAllJobNames();

        for (String jobName : jobNameList) {
            ScheduledJob batchJob = scheduledJobService.findBatchJob(jobName);
            if (batchJob != null) {
                registerScheduledJob(taskRegistrar, batchJob);
            }
        }
    }

    private void configureImplementedJobsFromProject(@NotNull final ScheduledTaskRegistrar taskRegistrar, @NotNull final String packageName) {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(com.pedrocosta.exchangelog.batch.annotation.ScheduledTask.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(packageName)) {
            if (bd.getBeanClassName() == null) {
                continue;
            }

            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                if (clazz.getAnnotation(ScheduledTask.class) != null) {
                    String jobName = clazz.getAnnotation(ScheduledTask.class).value();
                    if (StringUtils.isEmpty(jobName)) {
                        jobName = bd.getBeanClassName()
                                .substring(bd.getBeanClassName().lastIndexOf(".") + 1)
                                .replace("Task", "");
                    }

                    ScheduledJob batchJob = scheduledJobService.findBatchJob(jobName);
                    if (batchJob == null) {
                        batchJob = new ScheduledJob();
                        batchJob.setName(jobName);
                        try {
                            batchJob = scheduledJobService.save(batchJob);
                            registerScheduledJob(taskRegistrar, batchJob);
                        } catch (SaveDataException e) {
                            Log.error(this, e);
                        }
                    }
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    private void registerScheduledJob(final ScheduledTaskRegistrar taskRegistrar, final ScheduledJob batchJob) {
        if (!batchJob.isEnabled()) {
            return;
        }

        Log.info(this, Messages.get(
                "task.config.execution", batchJob.getName()));

        String jobCron = buildCron(batchJob, context.getEnvironment());
        // Ignore jobs with invalid cron
        if (!isValidCron(jobCron)) {
            Log.warn(this, Messages.get(
                    "task.cron.invalid", batchJob.getName()));
            return;
        }

        taskRegistrar.addTriggerTask(
                () -> executeAll(batchJob.getName()),
                new CronTrigger(jobCron)
        );
    }

    private void executeAll(String jobName) {
        try {
            TaskChain taskChain = scheduledJobService.findScheduledChain(jobName);
            BatchLauncher.launch(taskChain);

        } catch (JobExecutionAlreadyRunningException
                 | JobRestartException
                 | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException e) {
            Log.error(this, e);
        }
    }

    /**
     * Build a cron based on attributes set in {@link ScheduledJob}.
     * <br><br>
     * Cron expression is based on the following rules:
     * <pre>
     *  ┌───────── second (0-59)
     *  │ ┌───────── minute (0 - 59)
     *  │ │ ┌───────── hour (0 - 23)
     *  │ │ │ ┌───────── day of the month (1 - 31)
     *  │ │ │ │ ┌───────── month (1 - 12) (or JAN-DEC)
     *  │ │ │ │ │ ┌───────── day of the week (0 - 7)
     *  │ │ │ │ │ │          (or MON-SUN -- 0 or 7 is Sunday)
     *  * * * * * *
     *  </pre>
     *
     * @param batchJob    {@link ScheduledJob} object.
     * @param environment Context's {@link Environment}
     * @return Cron expression. If no attribute was filled,
     * it will return a not valid cron set on property named 'project.default.schedule.cron'.
     */
    private String buildCron(@NotNull ScheduledJob batchJob, Environment environment) {
        String jobCron = environment.getProperty(PropertyNames.PROJECT_DEFAULT_SCHED_CRON);
        String emptyCron = "0 0 0 0 *";
        StringBuilder cronBuilder = new StringBuilder();

        // If we have a cron and it is valid, return it
        if (isValidCron(batchJob.getScheduleCron())) {
            return batchJob.getScheduleCron();
        }

        final String SEPARATE = " ";

        if (batchJob.getExecTime() != null && !batchJob.getExecTime().isBlank()) {
            cronBuilder.append(batchJob.getExecTime(), 0, 2);
            cronBuilder.append(SEPARATE);
            cronBuilder.append(batchJob.getExecTime(), 1, 4);
        } else {
            cronBuilder.append("0");
            cronBuilder.append(SEPARATE);
            cronBuilder.append("0");
        }
        cronBuilder.append(SEPARATE);

        if (batchJob.getExecDay() != null) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(batchJob.getExecDay());
            cronBuilder.append(cal.get(Calendar.DAY_OF_MONTH));
            cronBuilder.append(SEPARATE);
            cronBuilder.append(cal.get(Calendar.MONTH));
        } else {
            cronBuilder.append("0");
            cronBuilder.append(SEPARATE);
            cronBuilder.append("0");
        }
        cronBuilder.append(SEPARATE);

        cronBuilder.append(batchJob.isExecWeekend() ? "*" : "1-5");

        if (!emptyCron.equals(cronBuilder.toString())) {
            jobCron = cronBuilder.toString();
        }

        return jobCron;
    }

    /**
     * Stops all undone past executed jobs.
     * <br><br>
     * When a task is running and system does down,
     * it may be on starting status and never finish.
     * To avoid it to be executed again or repeated,
     * this method will search for these tasks and stop it.
     */
    private void cancelUndoneTasks() {
        List<String> jobNames = new ArrayList<>();
        try {
            jobNames = jobExplorer.getJobNames();
        } catch (Exception e) {
            Log.warn(this, e.getMessage());
        }

        for (String jobName : jobNames) {
            try {
                int instanceCount = jobExplorer.getJobInstanceCount(jobName);
                List<JobInstance> instances = jobExplorer.getJobInstances(jobName, 0, instanceCount);
                JobOperator operator = context.getBean(JobOperator.class);
                instances.forEach(jobInstance -> {
                    List<JobExecution> executions = jobExplorer.getJobExecutions(jobInstance);
                    executions.forEach(jobExecution -> {
                        if (BatchStatus.STARTED.equals(jobExecution.getStatus())
                                && ExitStatus.UNKNOWN.equals(jobExecution.getExitStatus())) {
                            try {
                                operator.stop(jobExecution.getId());
                            } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                });
            } catch (NoSuchJobException e) {
                Log.warn(this, Messages.get(
                        "task.execution.not.canceled", jobName));
                Log.error(this, e);
            }
        }
    }

    private boolean isValidCron(String cron) {
        if (cron == null) return false;
        return CronSequenceGenerator.isValidExpression(cron);
    }
}
