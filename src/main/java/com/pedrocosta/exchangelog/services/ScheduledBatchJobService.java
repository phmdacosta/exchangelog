package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.batch.JobFactory;
import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.batch.TaskChain;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.ScheduledJob;
import com.pedrocosta.exchangelog.persistence.ScheduledBatchJobRepository;
import com.sun.istack.Nullable;
import org.springframework.context.ApplicationContext;
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

    /**
     * Search for Scheduled Job in database.
     *
     * @param id    Scheduled Job ID
     *
     * @return  Found Scheduled Job. Null if not found.
     */
    @Override
    @Nullable
    public ScheduledJob find(long id) {
//        ScheduledJob result = this.repository.findById(id).orElse(null);
//
//        if (result == null) {
//            String arg = "with id " + id;
//            throw new ServiceException(Messages.get("task.job.not.found", arg));
//        }
//        ServiceResponse<ScheduledJob> result =
//                ServiceResponse.<ScheduledJob>createSuccess()
//                    .setObject(this.repository.findById(id).orElse(null));
//
//        if (result.getObject() == null) {
//            String arg = "with id " + id;
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("task.job.not.found", arg));
//        }

        return this.repository.findById(id).orElse(null);
    }

    /**
     * Search for Scheduled Task.
     *
     * @param id    Scheduled Job ID
     * @param <I>   Read class type of task
     * @param <O>   Write class type of task
     * @param <T>   {@link ScheduledTask} child type
     *
     * @return  Found {@link ScheduledTask} object. Null if not found.
     */
    @Nullable
    public <I, O, T extends ScheduledTask<I, O>> T findScheduledTask(long id) {
        T result = null;
        ScheduledJob scheduledJob = find(id);

        if (scheduledJob != null) {
            JobFactory<T> jobFactory = new JobFactory<>(context);
            T scheduledTask = jobFactory.create(scheduledJob.getName());
            if (scheduledTask != null) {
                scheduledTask.setSchedBatchJob(scheduledJob);
                result = scheduledTask;
            }
        }

        return result;
    }

    /**
     * Search for Scheduled Task.
     *
     * @param name  Scheduled Job/Task name
     * @param <I>   Read class type of task
     * @param <O>   Write class type of task
     * @param <T>   {@link ScheduledTask} child type
     *
     * @return  {@link ScheduledTask} object. Null if not found.
     */
    @Nullable
    public <I, O, T extends ScheduledTask<I, O>> T findScheduledTask(String name) {
        T result = null;
        ScheduledJob scheduledJob = this.repository.findByName(name);

        if (scheduledJob != null) {
            JobFactory<T> jobFactory = new JobFactory<>(context);
            T scheduledTask = jobFactory.create(scheduledJob.getName());
            if (scheduledTask != null) {
                scheduledTask.setSchedBatchJob(scheduledJob);
                result = scheduledTask;
            }
        }

        return result;
    }

    /**
     * Search for Scheduled Job in database.
     *
     * @param name  Scheduled Job/Task name
     * @return  Found Scheduled Job. Null if not found.
     */
    @Nullable
    public ScheduledJob findBatchJob(String name) {
//        ScheduledJob result = this.repository.findByName(name);
//
//        if (result == null) {
//            throw new ServiceException(Messages.get("task.job.not.found", name));
//        }
//        ServiceResponse<ScheduledJob> result =
//                ServiceResponse.<ScheduledJob>createSuccess()
//                        .setObject(this.repository.findByName(name));
//
//        if (result.getObject() == null) {
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("task.job.not.found", name));
//        }

        return this.repository.findByName(name);
    }

    /**
     * Get all Scheduled Tasks.
     *
     * @param <I>   Read class type of task
     * @param <O>   Write class type of task
     * @param <T>   {@link ScheduledTask} child type
     *
     * @return  List of found {@link ScheduledTask} objects. Empty list if not found.
     */
    public <I, O, T extends ScheduledTask<I, O>> List<T> findAllScheduledTasks() {
        List<T> result = new ArrayList<>();
        List<ScheduledJob> jobList = findAll();

        JobFactory<T> jobFactory = new JobFactory<>(context);
        for (ScheduledJob batchJob : jobList) {
            T scheduledTask = jobFactory.create(batchJob.getName());
            if (scheduledTask != null) {
                scheduledTask.setSchedBatchJob(batchJob);
                result.add(scheduledTask);
            }
        }

//        if (result.isEmpty())
//            throw new ServiceException(Messages.get("task.no.job.found"));
//        ServiceResponse<List<T>> result = ServiceResponse.createSuccess();
//        List<T> scheduledTaskList = new ArrayList<>();
//        ServiceResponse<List<ScheduledJob>> jobListResp = findAll();
//
//        if (!jobListResp.isSuccess())
//            return result.fromError(jobListResp);
//
//        JobFactory<T> jobFactory = new JobFactory<>(context);
//        for (ScheduledJob batchJob : jobListResp.getObject()) {
//            T scheduledTask = jobFactory.create(batchJob.getName());
//            if (scheduledTask != null) {
//                scheduledTask.setSchedBatchJob(batchJob);
//                scheduledTaskList.add(scheduledTask);
//            }
//        }
//
//        if (!scheduledTaskList.isEmpty())
//            result.setObject(scheduledTaskList);
//        else
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("task.no.job.found"));

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
     *          Empty chain if not found.
     */
    public TaskChain findScheduledChain(String firstJobName) {
//        TaskChain result = ServiceResponse.createSuccess();
        TaskChain result = new TaskChain();
        String jobName = firstJobName;
        boolean finished = false;

        while (!finished) {
            ScheduledTask<Object, Object> task = this.findScheduledTask(jobName);
            result.add(task);
            if (task.getNextJobID() == null || task.getNextJobID() == 0L) {
                finished = true;
                continue;
            }
            ScheduledJob nextJob = this.repository.getOne(task.getNextJobID());
            jobName = nextJob.getName();
        }
//        ServiceResponse<TaskChain> result = ServiceResponse.createSuccess();
//        String jobName = firstJobName;
//        TaskChain chain = new TaskChain();
//        boolean finished = false;
//
//        while (!finished) {
//            ServiceResponse<ScheduledTask<Object, Object>> taskResp = this.findScheduledTask(jobName);
//            if (!taskResp.isSuccess()) {
//                result.fromError(taskResp);
//                break;
//            }
//            ScheduledTask<Object, Object> task = taskResp.getObject();
//            chain.add(task);
//            if (task.getNextJobID() == null || task.getNextJobID() == 0L) {
//                finished = true;
//                continue;
//            }
//            ScheduledJob nextJob = this.repository.getOne(task.getNextJobID());
//            jobName = nextJob.getName();
//        }
//
//        if (!chain.isEmpty())
//            result.setObject(chain);
//        else
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("task.chain.not.found", firstJobName));
        return result;
    }

    /**
     * Get all Scheduled Jobs from database.
     *
     * @return List of of found Scheduled Jobs.
     *         Empty list, if not found.
     */
    @Override
    public List<ScheduledJob> findAll() {
//        List<ScheduledJob> result = this.repository.findAll();

//        if (result.isEmpty()) {
//            throw new ServiceException(Messages.get("task.no.job.found"));
//        }
//        ServiceResponse<List<ScheduledJob>> result =
//                ServiceResponse.<List<ScheduledJob>>createSuccess()
//                        .setObject(this.repository.findAll());
//
//        if (result.getObject() == null || result.getObject().isEmpty()) {
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("task.no.job.found"));
//        }

        return this.repository.findAll();
    }

    /**
     * Get names of all Scheduled Jobs from database.
     * @return  List with all scheduled job's names.
     *          Empty list if not found.
     */
    public List<String> findAllJobNames() {
        List<ScheduledJob> jobList = findAll();
        List<String> result = new ArrayList<>(jobList.size());
        for (ScheduledJob job : jobList) {
            result.add(job.getName());
        }
//        ServiceResponse<List<String>> result = ServiceResponse.createSuccess();
//        ServiceResponse<List<ScheduledJob>> jobListResp = findAll();
//
//        if (!jobListResp.isSuccess())
//            return result.fromError(jobListResp);
//
//        List<ScheduledJob> list = jobListResp.getObject();
//        List<String> names = new ArrayList<>(list.size());
//
//        for (ScheduledJob job : list) {
//            names.add(job.getName());
//        }
//
//        result.setObject(names);
        return result;
    }

    /**
     * Save a {@link ScheduledJob} object.
     * <br>
     * If exchange's id is not zero, it will update this data.
     *
     * @param batchJob Scheduled Job to save
     * @return  Saved scheduled job.
     *          If error, throws {@link SaveDataException} with error message.
     * @throws SaveDataException if not well saved
     */
    public ScheduledJob save(ScheduledJob batchJob) throws SaveDataException {
        try {
            batchJob = repository.save(batchJob);
        } catch (Exception e) {
            throw new SaveDataException(e);
        }
        return batchJob;
    }

    /**
     * Save a {@link ScheduledTask} object.
     * <br>
     * If exchange's id is not zero, it will update this data.
     *
     * @param scheduledTasks    Scheduled Task to save
     * @param <I>   Read class type of task
     * @param <O>   Write class type of task
     * @param <T>   {@link ScheduledTask} child type
     *
     * @return Saved scheduled job.
     *         If error, throws {@link SaveDataException} with error message.
     * @throws SaveDataException if not well saved
     */
    public <I, O, T extends ScheduledTask<I, O>> T save(T scheduledTasks) throws SaveDataException {
        scheduledTasks.setSchedBatchJob(save(scheduledTasks.getSchedBatchJob()));
//        ServiceResponse<T> result = ServiceResponse.createSuccess();
//        try {
//            result.setObject((T) scheduledTasks.clone());
//        } catch (Exception e) {
//            Log.error(this, e);
//            result.setObject(scheduledTasks);
//        }
//
//        ServiceResponse<ScheduledJob> saveJobResp = save(result.getObject().getSchedBatchJob());
//
//        if (!saveJobResp.isSuccess())
//            return result.fromError(saveJobResp);
//
//        result.getObject().setSchedBatchJob(saveJobResp.getObject());
        return scheduledTasks;
    }

    /**
     * Save all Scheduled Jobs from a list into database.
     *
     * @param batchJobs List of Scheduled Jobs to save
     *
     * @return Saved list of tasks.
     *         If error, throws {@link SaveDataException} with error message.
     *
     * @throws SaveDataException if not well saved
     */
    @Override
    public List<ScheduledJob> saveAll(Collection<ScheduledJob> batchJobs) throws SaveDataException {
        try {
            batchJobs = repository.saveAll(batchJobs);
        } catch (Exception e) {
            throw new SaveDataException(e);
        }
        return new ArrayList<>(batchJobs);
    }

    /**
     * Save all Scheduled Jobs from a list of tasks into database.
     *
     * @param scheduledTasks    List of scheduled tasks
     * @param <I>   Read class type of task
     * @param <O>   Write class type of task
     * @param <T>   {@link ScheduledTask} child type
     *
     * @return  Saved list of tasks.
     *          If error, throws {@link SaveDataException} with error message.
     *
     * @throws SaveDataException if not well saved
     */
    public <I, O, T extends ScheduledTask<I, O>> List<T> saveAllScheduledTasks(List<T> scheduledTasks) throws SaveDataException {
        List<ScheduledJob> batchJobs = new ArrayList<>(scheduledTasks.size());
        for (ScheduledTask<?, ?> task : scheduledTasks) {
            batchJobs.add(task.getSchedBatchJob());
        }
        List<ScheduledJob> savedJobs = saveAll(batchJobs);

        for (int i = 0; i < scheduledTasks.size(); i++) {
            if (i < batchJobs.size()) {
                scheduledTasks.get(i).setSchedBatchJob(savedJobs.get(i));
            }
        }
//        ServiceResponse<List<T>> result = ServiceResponse.createSuccess();
//        List<ScheduledJob> batchJobs = new ArrayList<>(scheduledTasks.size());
//        for (ScheduledTask<?, ?> task : scheduledTasks) {
//            batchJobs.add(task.getSchedBatchJob());
//        }
//        ServiceResponse<List<ScheduledJob>> saveJobResp = saveAll(batchJobs);
//
//        if (!saveJobResp.isSuccess())
//            return result.fromError(saveJobResp);
//
//        for (int i = 0; i < scheduledTasks.size(); i++) {
//            if (i < batchJobs.size()) {
//                scheduledTasks.get(i).setSchedBatchJob(batchJobs.get(i));
//            }
//        }

        return scheduledTasks;
    }
}
