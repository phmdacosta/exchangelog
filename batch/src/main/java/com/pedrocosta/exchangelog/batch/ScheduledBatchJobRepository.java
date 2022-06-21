package com.pedrocosta.exchangelog.batch;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledBatchJobRepository extends JpaRepository<ScheduledJob, Long> {
    ScheduledJob findByName(String name);
}
