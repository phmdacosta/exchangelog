package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.batch.TaskChain;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.ScheduledJob;
import com.sun.istack.Nullable;

import java.util.Collection;
import java.util.List;

public interface ScheduledBatchJobService extends RepositoryService<ScheduledJob> {

    /**
     * Search for Scheduled Job in database.
     *
     * @param id    Scheduled Job ID
     *
     * @return  Found Scheduled Job. Null if not found.
     */
    @Override
    @Nullable ScheduledJob find(long id);

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
    @Nullable <I, O, T extends ScheduledTask<I, O>> T findScheduledTask(long id);

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
    @Nullable <I, O, T extends ScheduledTask<I, O>> T findScheduledTask(String name);

    /**
     * Search for Scheduled Job in database.
     *
     * @param name  Scheduled Job/Task name
     * @return  Found Scheduled Job. Null if not found.
     */
    @Nullable ScheduledJob findBatchJob(String name);

    /**
     * Get all Scheduled Tasks.
     *
     * @param <I>   Read class type of task
     * @param <O>   Write class type of task
     * @param <T>   {@link ScheduledTask} child type
     *
     * @return  List of found {@link ScheduledTask} objects. Empty list if not found.
     */
    <I, O, T extends ScheduledTask<I, O>> List<T> findAllScheduledTasks();

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
    TaskChain findScheduledChain(String firstJobName);

    /**
     * Get all Scheduled Jobs from database.
     *
     * @return List of of found Scheduled Jobs.
     *         Empty list, if not found.
     */
    @Override List<ScheduledJob> findAll();

    /**
     * Get names of all Scheduled Jobs from database.
     * @return  List with all scheduled job's names.
     *          Empty list if not found.
     */
    List<String> findAllJobNames();

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
    ScheduledJob save(ScheduledJob batchJob) throws SaveDataException;

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
    <I, O, T extends ScheduledTask<I, O>> T save(T scheduledTasks) throws SaveDataException;

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
    @Override List<ScheduledJob> saveAll(Collection<ScheduledJob> batchJobs) throws SaveDataException;

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
    <I, O, T extends ScheduledTask<I, O>> List<T> saveAllScheduledTasks(List<T> scheduledTasks) throws SaveDataException;
}
