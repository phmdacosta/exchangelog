package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.batch.JobFactory;
import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.batch.TaskChain;
import com.pedrocosta.exchangelog.models.ScheduledJob;
import com.pedrocosta.exchangelog.persistence.ScheduledBatchJobRepository;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import com.sun.istack.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ScheduledBatchJobService implements RepositoryService<ScheduledJob> {

    private final ScheduledBatchJobRepository repository;
    private final ApplicationContext context;

    public ScheduledBatchJobService(ScheduledBatchJobRepository repository,
                                    ApplicationContext context) {
        this.repository = repository;
        this.context = context;
    }

    @Nullable
    public <I, O, T extends ScheduledTask<I, O>> ServiceResponse<T> findScheduledTask(String name) {
        ServiceResponse<T> result = new ServiceResponse<>(HttpStatus.OK);
        ServiceResponse<ScheduledJob> scheduledJobResp = findBatchJob(name);

        if (scheduledJobResp.isSuccess()) {
            ScheduledJob scheduledJob = scheduledJobResp.getObject();
            JobFactory<T> jobFactory = new JobFactory<>(context);
            T scheduledTask = jobFactory.create(scheduledJob.getName());
            if (scheduledTask != null) {
                scheduledTask.setSchedBatchJob(scheduledJob);
                result.setObject(scheduledTask);
            } else {
                result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
                result.setMessage(Messages.get("task.job.not.found", name));
            }
        } else {
            result.fromError(scheduledJobResp);
        }

        return result;
    }

    /**
     * Search for Scheduled Job in database.
     *
     * @param id    Scheduled Job ID
     *
     * @return  {@link ServiceResponse} object with found Scheduled Job.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<ScheduledJob> find(long id) {
        ServiceResponse<ScheduledJob> result =
                new ServiceResponse<ScheduledJob>(HttpStatus.OK)
                    .setObject(this.repository.findById(id).orElse(null));

        if (result.getObject() == null) {
            String arg = "with id " + id;
            result = new ServiceResponse<ScheduledJob>(HttpStatus.NOT_FOUND)
                    .setMessage(Messages.get("task.job.not.found", arg));
        }

        return result;
    }

    public ServiceResponse<ScheduledJob> findBatchJob(String name) {
        ServiceResponse<ScheduledJob> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(this.repository.findByName(name));

        if (result.getObject() == null) {
            result = new ServiceResponse<ScheduledJob>(HttpStatus.NOT_FOUND)
                    .setMessage(Messages.get("task.job.not.found", name));
        }

        return result;
    }

    public <I, O, T extends ScheduledTask<I, O>> ServiceResponse<List<T>> findAllScheduledTasks() {
        ServiceResponse<List<T>> result = new ServiceResponse<>(HttpStatus.OK);
        List<T> scheduledTaskList = new ArrayList<>();
        ServiceResponse<List<ScheduledJob>> jobListResp = findAll();

        if (!jobListResp.isSuccess())
            return result.fromError(jobListResp);

        JobFactory<T> jobFactory = new JobFactory<>(context);
        for (ScheduledJob batchJob : jobListResp.getObject()) {
            T scheduledTask = jobFactory.create(batchJob.getName());
            if (scheduledTask != null) {
                scheduledTask.setSchedBatchJob(batchJob);
                scheduledTaskList.add(scheduledTask);
            }
        }

        if (!scheduledTaskList.isEmpty())
            result.setObject(scheduledTaskList);
        else
            result = new ServiceResponse<List<T>>(HttpStatus.NOT_FOUND)
                    .setMessage(Messages.get("task.no.job.found"));

        return result;
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
    public ServiceResponse<TaskChain> findScheduledChain(String firstJobName) {
        ServiceResponse<TaskChain> result = new ServiceResponse<>(HttpStatus.OK);
        String jobName = firstJobName;
        TaskChain chain = new TaskChain();
        boolean finished = false;

        while (!finished) {
            ServiceResponse<ScheduledTask<Object, Object>> taskResp = this.findScheduledTask(jobName);
            if (!taskResp.isSuccess()) {
                result.fromError(taskResp);
                break;
            }
            ScheduledTask<Object, Object> task = taskResp.getObject();
            chain.add(task);
            if (task.getNextJobID() == null || task.getNextJobID() == 0L) {
                finished = true;
                continue;
            }
            ScheduledJob nextJob = this.repository.getOne(task.getNextJobID());
            jobName = nextJob.getName();
        }

        if (!chain.isEmpty())
            result.setObject(chain);
        else
            result = new ServiceResponse<TaskChain>(HttpStatus.NOT_FOUND)
                    .setMessage(Messages.get("task.chain.not.found", firstJobName));

        return result;
    }

    /**
     * Get all Scheduled Jobs from database.
     *
     * @return {@link ServiceResponse} object with list of found Scheduled Jobs.
     *         If not found, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<List<ScheduledJob>> findAll() {
        ServiceResponse<List<ScheduledJob>> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(this.repository.findAll());

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = new ServiceResponse<List<ScheduledJob>>(HttpStatus.NOT_FOUND)
                    .setMessage(Messages.get("task.no.job.found"));
        }

        return result;
    }

    public ServiceResponse<List<String>> findAllJobNames() {
        ServiceResponse<List<String>> result = new ServiceResponse<>(HttpStatus.OK);
        ServiceResponse<List<ScheduledJob>> jobListResp = findAll();

        if (!jobListResp.isSuccess())
            return result.fromError(jobListResp);

        List<ScheduledJob> list = jobListResp.getObject();
        List<String> names = new ArrayList<>(list.size());

        for (ScheduledJob job : list) {
            names.add(job.getName());
        }

        result.setObject(names);
        return result;
    }

    public ServiceResponse<ScheduledJob> save(ScheduledJob batchJob) {
        return new ServiceResponse<ScheduledJob>(HttpStatus.OK).setObject(repository.save(batchJob));
    }

    public <I, O, T extends ScheduledTask<I, O>> ServiceResponse<T> save(T scheduledTasks) {
        ServiceResponse<T> result = new ServiceResponse<T>(HttpStatus.OK);
        try {
            result.setObject((T) scheduledTasks.clone());
        } catch (Exception e) {
            Log.error(this, e);
            result.setObject(scheduledTasks);
        }

        ServiceResponse<ScheduledJob> saveJobResp = save(result.getObject().getSchedBatchJob());

        if (!saveJobResp.isSuccess())
            return result.fromError(saveJobResp);

        result.getObject().setSchedBatchJob(saveJobResp.getObject());
        return result;
    }

    /**
     * Save all Scheduled Jobs from a list into database.
     *
     * @param batchJobs List of Scheduled Jobs to save
     *
     * @return {@link ServiceResponse} object with list of saved Scheduled Jobs.
     *         If error, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<List<ScheduledJob>> saveAll(Collection<ScheduledJob> batchJobs) {
        return new ServiceResponse<List<ScheduledJob>>(HttpStatus.OK)
                .setObject(repository.saveAll(batchJobs));
    }

    public <I, O, T extends ScheduledTask<I, O>> ServiceResponse<List<T>> saveAllScheduledTasks(List<T> scheduledTasks) {
        ServiceResponse<List<T>> result = new ServiceResponse<>(HttpStatus.OK);
        List<ScheduledJob> batchJobs = new ArrayList<>(scheduledTasks.size());
        for (ScheduledTask<?, ?> task : scheduledTasks) {
            batchJobs.add(task.getSchedBatchJob());
        }
        ServiceResponse<List<ScheduledJob>> saveJobResp = saveAll(batchJobs);

        if (!saveJobResp.isSuccess())
            return result.fromError(saveJobResp);

        for (int i = 0; i < scheduledTasks.size(); i++) {
            if (i < batchJobs.size()) {
                scheduledTasks.get(i).setSchedBatchJob(batchJobs.get(i));
            }
        }

        return result.setObject(scheduledTasks);
    }
}
