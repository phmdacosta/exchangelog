package com.pedrocosta.exchangelog.persistence;

import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteNotificationRequestRepository extends JpaRepository<QuoteNotificationRequest, Long> {

    QuoteNotificationRequest findByName(String name);
    List<QuoteNotificationRequest> findAllByLogicalOperator(String logicalOperator);
}
