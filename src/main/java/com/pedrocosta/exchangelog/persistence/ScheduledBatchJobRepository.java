package com.pedrocosta.exchangelog.persistence;

import com.pedrocosta.exchangelog.models.ScheduledJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledBatchJobRepository extends JpaRepository<ScheduledJob, Long> {
    public ScheduledJob findByName(String name);
}
