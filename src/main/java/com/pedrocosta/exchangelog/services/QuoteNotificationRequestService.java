package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.persistence.QuoteNotificationRequestRepository;
import com.sun.istack.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class QuoteNotificationRequestService implements RepositoryService<QuoteNotificationRequest> {

    private QuoteNotificationRequestRepository repository;

    public QuoteNotificationRequestService(QuoteNotificationRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuoteNotificationRequest save(QuoteNotificationRequest quoteNotifReq) {
        return repository.save(quoteNotifReq);
    }

    @Override
    public List<QuoteNotificationRequest> saveAll(Collection<QuoteNotificationRequest> col) {
        return repository.saveAll(col);
    }

    @Override
    @Nullable
    public QuoteNotificationRequest find(long id) {
        return repository.findById(id).orElse(null);
    }

    public QuoteNotificationRequest find(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<QuoteNotificationRequest> findAll() {
        return repository.findAll();
    }

    public List<QuoteNotificationRequest> findAllByLogicalOperator(String logicalOperator) {
        return repository.findAllByLogicalOperator(logicalOperator);
    }
}
