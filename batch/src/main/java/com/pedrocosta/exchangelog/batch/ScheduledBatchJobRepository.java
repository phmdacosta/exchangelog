package com.pedrocosta.exchangelog.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledBatchJobRepository extends JpaRepository<ScheduledJob, Long> {
    ScheduledJob findByName(String name);
}
