package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.batch.JobFactory;
import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.batch.TaskChain;
import com.pedrocosta.exchangelog.models.ScheduledJob;
import com.pedrocosta.exchangelog.persistence.ScheduledBatchJobRepository;
import com.sun.istack.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduledBatchJobService implements CoreService {

    private final ScheduledBatchJobRepository repository;
    private final ApplicationContext context;

    public ScheduledBatchJobService(ScheduledBatchJobRepository repository,
                                    ApplicationContext context) {
        this.repository = repository;
        this.context = context;
    }

    @Nullable
    public <I, O, T extends ScheduledTask<I, O>> T findScheduledTask(String name) {
        T scheduledTask = null;
        ScheduledJob scheduledJob = findBatchJob(name);
        JobFactory<T> jobFactory = new JobFactory<>(context);

        if (scheduledJob != null) {
            scheduledTask = jobFactory.create(scheduledJob.getName());
            if (scheduledTask != null)
                scheduledTask.setSchedBatchJob(scheduledJob);
        }

        return scheduledTask;
    }

    public ScheduledJob findBatchJob(String name) {
        return this.repository.findByName(name);
    }

    public <I, O, T extends ScheduledTask<I, O>> List<T> findAllScheduledTasks() {
        List<T> scheduledTaskList = new ArrayList<>();
        List<ScheduledJob> schedBatchJobList = findAllBatchJobs();
        JobFactory<T> jobFactory = new JobFactory<>(context);

        for (ScheduledJob batchJob : schedBatchJobList) {
            T scheduledTask = jobFactory.create(batchJob.getName());
            if (scheduledTask != null) {
                scheduledTask.setSchedBatchJob(batchJob);
                scheduledTaskList.add(scheduledTask);
            }
        }

        return scheduledTaskList;
    }

    /**
     * Find a scheduled task chain based on first job.
     *
     * Chain sequence is defined by jobs relationships using
     * {@linkplain ScheduledJob#getNextJobId() nextJobId} field.
     * The first job wil carry on another job in <b>nextJobId</b> field,
     * and the second one will carry a third one and it goes on.
     *
     * @param firstJobName  Name of the first job of the chain
     * @return  List of scheduled tasks ordered by chain sequence.
     */
    public TaskChain findScheduledChain(String firstJobName) {
        String jobName = firstJobName;
        TaskChain chain = new TaskChain();
        boolean finished = false;

        while (!finished) {
            ScheduledTask<Object, Object> task = this.findScheduledTask(jobName);
            chain.add(task);
            Long nextId = task.getSchedBatchJob().getNextJobId();
            if (nextId == null || nextId == 0L) {
                finished = true;
                continue;
            }
            ScheduledJob nextJob = this.repository.getOne(task.getNextJobID());
            jobName = nextJob.getName();
        }

        return chain;
    }

    public List<ScheduledJob> findAllBatchJobs() {
        return this.repository.findAll();
    }

    public List<String> findAllJobNames() {
        List<ScheduledJob> list = findAllBatchJobs();
        List<String> names = new ArrayList<>(list.size());

        for (ScheduledJob job : list) {
            names.add(job.getName());
        }

        return names;
    }

    public ScheduledJob save(ScheduledJob batchJob) {
        return repository.save(batchJob);
    }

    public <I, O, T extends ScheduledTask<I, O>> T save(T scheduledTasks) {
        ScheduledJob saved = save(scheduledTasks.getSchedBatchJob());
        scheduledTasks.setSchedBatchJob(saved);
        return scheduledTasks;
    }

    public List<ScheduledJob> saveAllBatchJobs(List<ScheduledJob> batchJobs) {
        return repository.saveAll(batchJobs);
    }

    public <I, O, T extends ScheduledTask<I, O>> List<T> saveAllScheduledTasks(List<T> scheduledTasks) {
        List<ScheduledJob> batchJobs = new ArrayList<>(scheduledTasks.size());
        for (ScheduledTask<?, ?> task : scheduledTasks) {
            batchJobs.add(task.getSchedBatchJob());
        }
        batchJobs = saveAllBatchJobs(batchJobs);

        for (int i = 0; i < scheduledTasks.size(); i++) {
            if (i < batchJobs.size()) {
                scheduledTasks.get(i).setSchedBatchJob(batchJobs.get(i));
            }
        }

        return scheduledTasks;
    }
}
