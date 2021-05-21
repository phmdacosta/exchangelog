package com.pedrocosta.exchangelog.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Listener to control scheduled tasks execution.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
@Component
public class ScheduledTaskListener {

    private final List<?> items;

    public ScheduledTaskListener(List<?> items) {
        this.items = items;
    }

    /**
     * Called after job execution.
     *
     * @param stepExecution {@link StepExecution} object
     * @return If we still have items to process, return exit status 'CONTINUE',
     *         or 'FINISHED' if not.
     */
    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        if(items.size() > 0) {
            return new ExitStatus("CONTINUE");
        }
        else {
            return new ExitStatus("FINISHED");
        }
    }
}
