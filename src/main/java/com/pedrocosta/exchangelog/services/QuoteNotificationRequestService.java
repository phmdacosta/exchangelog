package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.persistence.QuoteNotificationRequestRepository;
import com.pedrocosta.exchangelog.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class QuoteNotificationRequestService implements RepositoryService<QuoteNotificationRequest> {

    private final QuoteNotificationRequestRepository repository;

    public QuoteNotificationRequestService(QuoteNotificationRequestRepository repository) {
        this.repository = repository;
    }

    /**
     * Save a notification request into database.
     *
     * @param quoteNotifReq Notification request to save
     *
     * @return  {@link ServiceResponse} object with saved notification request.
     *          If error, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<QuoteNotificationRequest> save(QuoteNotificationRequest quoteNotifReq) {
        ServiceResponse<QuoteNotificationRequest> result = new ServiceResponse<>(HttpStatus.CREATED);
        QuoteNotificationRequest saved = repository.save(quoteNotifReq);
        if (saved.getId() == 0) {
            result = new ServiceResponse<>(HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    /**
     * Save all notification request from a list into database.
     *
     * @param col   List of notification requests to save
     *
     * @return  {@link ServiceResponse} object with saved notification requests.
     *          If error, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<List<QuoteNotificationRequest>> saveAll(Collection<QuoteNotificationRequest> col) {
        ServiceResponse<List<QuoteNotificationRequest>> result = new ServiceResponse<>(HttpStatus.CREATED);
        result.setObject(repository.saveAll(col));

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = new ServiceResponse<>(HttpStatus.BAD_REQUEST);
            result.setMessage(Messages.get("error.not.saved", "any notification request"));
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

    /**
     * Find a specific notification request from database.
     *
     * @param id Notification request's id in database
     *
     * @return  {@link ServiceResponse} object with found notification request.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<QuoteNotificationRequest> find(long id) {
        ServiceResponse<QuoteNotificationRequest> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findById(id).orElse(null));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            String arg = "with id " + id;
            result.setMessage(Messages.get("error.notif.req.not.found", arg));
        }

        return result;
    }

    /**
     * Find a specific notification request from database.
     *
     * @param name Notification request's name
     *
     * @return  {@link ServiceResponse} object with found notification request.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<QuoteNotificationRequest> find(String name) {
        ServiceResponse<QuoteNotificationRequest> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findByName(name));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.notif.req.not.found", name));
        }

        return result;
    }

    /**
     * Find all notification requests in database.
     *
     * @return  {@link ServiceResponse} object with found notification requests.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<List<QuoteNotificationRequest>> findAll() {
        ServiceResponse<List<QuoteNotificationRequest>> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findAll());

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("could.not.find", "any notification request"));
        }

        return result;
    }

    /**
     * Find all notification request from database with specific logical operator.
     * <pre>
     * Logical operators are:
     *      = (equals),
     *      < (less then),
     *      > (greater then),
     *      <= (less then and equals),
     *      >= (greater then and equals),
     *      min (minimum),
     *      max (maximum)
     * </pre>
     *
     * @param logicalOperator Notification request's logical operator
     *
     * @return  {@link ServiceResponse} object with found notification requests.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<List<QuoteNotificationRequest>> findAllByLogicalOperator(String logicalOperator) {
        ServiceResponse<List<QuoteNotificationRequest>> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findAllByLogicalOperator(logicalOperator));

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            String arg = "with logical operator ".concat(logicalOperator);
            result.setMessage(Messages.get("error.notif.req.not.found", arg));
        }

        return result;
    }
}
