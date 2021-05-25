package com.pedrocosta.exchangelog.batch;

import com.pedrocosta.exchangelog.models.BatchJobParameter;
import com.pedrocosta.exchangelog.models.ScheduledJob;
import com.pedrocosta.exchangelog.services.ServiceFactory;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.MessageProperties;
import com.pedrocosta.exchangelog.utils.PropertyNames;
import com.sun.istack.NotNull;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.*;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * Main class of scheduled tasks.
 * <br><br>
 *
 * Scheduled task are jobs the run based on a schedule.
 * Each job carries a cron that tells when it should be executed.
 * These tasks are executed by {@link BatchSchedulerConfig} which search
 * for all registered jobs in database and schedule it based on its attributes.
 * If theses tasks are linked by next id attribute,
 * they are placed in a chain {@link TaskChain}.
 * <br><br>
 *
 * Cron of these scheduled tasks are based on spring's one, it follows the following rules:
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
 * @param <I> Type to read
 * @param <O> Type to write
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public abstract class ScheduledTask<I, O> implements ItemReader<I>, ItemProcessor<I, O>, ItemWriter<O> {

    private ApplicationContext context;
    private ServiceFactory factory;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private JobLauncher launcher;
    private ScheduledJob schedBatchJob;
    private Map<String, String> mapJobParameters;
    private int startLimit;
    private int chunk;
    private ScheduledTaskListener listener;
    private boolean executed;

    public ScheduledTask() {
        this.executed = false;
        setParameters(new HashMap<>());
        setStartLimit(1);
        setChunk(1);
    }

    public ScheduledTask<I, O> setContext(ApplicationContext context) {
        this.context = context;
        this.factory = context.getBean(ServiceFactory.class);
        this.jobBuilderFactory = context.getBean(JobBuilderFactory.class);
        this.stepBuilderFactory = context.getBean(StepBuilderFactory.class);
        this.launcher = context.getBean(JobLauncher.class);
        this.listener = context.getBean(ScheduledTaskListener.class);
        return this;
    }

    protected ApplicationContext getContext() {
        return this.context;
    }

    /**
     * Get current project API engine set in properties.
     * @return API engine name set in properties
     */
    protected String getProjectEngine() {
        return getEnvironment().getProperty(PropertyNames.PROJECT_ENGINE);
    }

    protected ServiceFactory getServiceFactory() {
        return this.factory;
    }

    protected ScheduledTask<I, O> setServiceFactory(ServiceFactory factory) {
        this.factory = factory;
        return this;
    }

    /**
     * Get context environment.
     * @return Context environment
     */
    protected Environment getEnvironment() {
        return this.context.getEnvironment();
    }

    protected JobBuilderFactory getJobBuilderFactory() {
        return this.jobBuilderFactory;
    }

    protected ScheduledTask<I, O> setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        return this;
    }

    protected ScheduledTask<I, O> setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
        return this;
    }

    protected StepBuilderFactory getStepBuilderFactory() {
        return stepBuilderFactory;
    }

    protected JobLauncher getJobLauncher() {
        return this.launcher;
    }

    protected ScheduledTask<I, O> setJobLauncher(JobLauncher launcher) {
        this.launcher = launcher;
        return this;
    }

    protected int getStartLimit() {
        return startLimit;
    }

    protected void setStartLimit(int startLimit) {
        this.startLimit = startLimit;
    }

    /**
     * Get chunk size.
     * <br><br>
     * Chunk size defines how many times methods read, process and write will run.
     * If chunk is 10, each method will run 10 times.
     *
     * @return Chunk size.
     */
    protected int getChunk() {
        return chunk;
    }

    /**
     * Set chunk size.
     * <br><br>
     * Chunk size defines how many times methods read, process and write will run.
     * If chunk is 10, each method will run 10 times.
     *
     * @param chunk Chunk size
     * @return Current ScheduledTask object.
     */
    protected ScheduledTask<I, O> setChunk(int chunk) {
        this.chunk = chunk;
        return this;
    }

    protected ScheduledTaskListener getListener() {
        return listener;
    }

    protected ScheduledTask<I, O> setListener(ScheduledTaskListener listener) {
        this.listener = listener;
        return this;
    }

    public ScheduledJob getSchedBatchJob() {
        return schedBatchJob;
    }

    public ScheduledTask<I, O> setSchedBatchJob(@NotNull ScheduledJob schedBatchJob) {
        this.schedBatchJob = schedBatchJob;
        if (schedBatchJob.getParameters() != null) {
            for (BatchJobParameter param : schedBatchJob.getParameters()) {
                mapJobParameters.put(param.getName(), param.getValue());
            }
        }
        return this;
    }

    public Map<String, String> getParameters() {
        return mapJobParameters;
    }

    public String getParameter(String name) {
        return mapJobParameters.get(name);
    }

    public ScheduledTask<I, O> setParameters(Map<String, String> mapJobParameters) {
        this.mapJobParameters = mapJobParameters;
        return this;
    }

    public String getJobName() {
        if (schedBatchJob != null) {
            return schedBatchJob.getName();
        }
        return null;
    }

    public Long getNextJobID() {
        if (schedBatchJob != null
                && schedBatchJob.getNextJobId() != null) {
            return schedBatchJob.getNextJobId();
        }
        return 0L;
    }

    protected ItemReader<I> getReader() {
        return this;
    }

    protected ItemProcessor<I, O> getProcessor() {
        return this;
    }

    protected ItemWriter<O> getWriter() {
        return this;
    }

    public TaskletStep getStep() {
        return stepBuilderFactory.get("step_" + schedBatchJob.getName())
                .<I, O>chunk(getChunk())
                .reader(getReader())
                .processor(getProcessor())
                .writer(getWriter())
                .listener(getListener())
                .startLimit(getStartLimit())
                .build();
    }

    /**
     * Creates and execute the job.
     * <br><br>
     * It calls {@link ScheduledTask#execute(JobParameters)} method, passing a default date parameter.
     *
     * @return {@link JobExecution} object.
     * @throws JobParametersInvalidException
     * @throws JobExecutionAlreadyRunningException
     * @throws JobRestartException
     * @throws JobInstanceAlreadyCompleteException
     */
    public JobExecution execute() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        return this.execute(new JobParametersBuilder()
                .addDate("date", new Date()) // create new parameter
                .toJobParameters());
    }

    /**
     * Creates and execute the job.
     *
     * @param parameters Parameters of job.
     * @return {@link JobExecution} object.
     * @throws JobParametersInvalidException
     * @throws JobExecutionAlreadyRunningException
     * @throws JobRestartException
     * @throws JobInstanceAlreadyCompleteException
     */
    public JobExecution execute(JobParameters parameters) throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
                                    JobRestartException, JobInstanceAlreadyCompleteException {
        SimpleJobBuilder jobBuilder = jobBuilderFactory.get(getJobName())
                .start(getStep());
        Job job = jobBuilder.build();
        Log.info(this, MessageProperties.get("task.start", getJobName()));
        Log.info(this, this.toString());
        return launcher.run(job, parameters);
    }

    @Override
    public I read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!executed) {
            this.executed = true;
            return doRead();
        }
        return null;
    }

    @Override
    public O process(I i) throws Exception {
        return doProcess(i);
    }

    @Override
    public void write(List<? extends O> list) throws Exception {
        if (!list.isEmpty()) {
            O o = list.stream().findFirst().get();
            doWrite(o);
        }
    }

    public abstract I doRead() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException;

    public abstract O doProcess(I i) throws Exception;

    public abstract void doWrite(O o) throws Exception;

    @Override
    public String toString() {
        if (schedBatchJob != null) {
            String s = schedBatchJob.toString();
            return s.replace("ScheduledJob", "ScheduledTask");
        }
        return "ScheduledTask{" + "id=0, " + "startLimit=" + startLimit +
                ", chunk=" + chunk +
                '}';
    }
}
