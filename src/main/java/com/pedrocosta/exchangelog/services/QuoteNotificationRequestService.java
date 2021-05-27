package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.persistence.QuoteNotificationRequestRepository;
import com.pedrocosta.exchangelog.utils.Messages;
import com.sun.istack.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class QuoteNotificationRequestService implements RepositoryService<QuoteNotificationRequest> {

    private QuoteNotificationRequestRepository repository;

    public QuoteNotificationRequestService(QuoteNotificationRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceResponse<QuoteNotificationRequest> save(QuoteNotificationRequest quoteNotifReq) {
        ServiceResponse<QuoteNotificationRequest> result = new ServiceResponse<>(HttpStatus.CREATED);
        QuoteNotificationRequest saved = repository.save(quoteNotifReq);
        if (saved.getId() == 0) {
            result = new ServiceResponse<>(HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    @Override
    public ServiceResponse<List<QuoteNotificationRequest>> saveAll(Collection<QuoteNotificationRequest> col) {
        ServiceResponse<List<QuoteNotificationRequest>> result = new ServiceResponse<>(HttpStatus.CREATED);
        result.setObject(repository.saveAll(col));

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = new ServiceResponse<>(HttpStatus.BAD_REQUEST);
            String arg = "any notification request";
            result.setMessage(Messages.get("error.not.saved", arg));
        }
        else if(result.getObject().size() != col.size()) {
            List<QuoteNotificationRequest> notSaved = new ArrayList<>();
            ServiceResponse<List<QuoteNotificationRequest>> auxResult =
                    result.setCode(HttpStatus.BAD_REQUEST);
            col.forEach(notifReq -> {
                if (!auxResult.getObject().contains(notifReq)) {
                    notSaved.add(notifReq);
                }
            });
            String msg = Messages.get("error.some.notif.req.not.saved", notSaved.toString());
            result.setMessage(msg);
        }

        return result;
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
