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
        ServiceResponse<QuoteNotificationRequest> result =
                ServiceResponse.<QuoteNotificationRequest>createSuccess()
                        .setCode(HttpStatus.CREATED);
        QuoteNotificationRequest saved = repository.save(quoteNotifReq);
        if (saved.getId() == 0) {
            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
                    "Could not save notification"); // TODO use properties
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
        ServiceResponse<List<QuoteNotificationRequest>> result =
                ServiceResponse.<List<QuoteNotificationRequest>>createSuccess()
                        .setCode(HttpStatus.CREATED);
        result.setObject(repository.saveAll(col));

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
                    Messages.get("error.not.saved", "any notification request"));
        }
        else if(result.getObject().size() != col.size()) {
            List<QuoteNotificationRequest> notSaved = new ArrayList<>();
            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST);
            ServiceResponse<List<QuoteNotificationRequest>> auxResult = result;
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
        ServiceResponse<QuoteNotificationRequest> result = ServiceResponse.createSuccess();
        result.setObject(repository.findById(id).orElse(null));

        if (result.getObject() == null) {
            String arg = "with id " + id;
            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                    Messages.get("error.notif.req.not.found", arg));
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
        ServiceResponse<QuoteNotificationRequest> result = ServiceResponse.createSuccess();
        result.setObject(repository.findByName(name));

        if (result.getObject() == null) {
            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                    Messages.get("error.notif.req.not.found", name));
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
        ServiceResponse<List<QuoteNotificationRequest>> result = ServiceResponse.createSuccess();
        result.setObject(repository.findAll());

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                    Messages.get("could.not.find", "any notification request"));
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
        ServiceResponse<List<QuoteNotificationRequest>> result = ServiceResponse.createSuccess();
        result.setObject(repository.findAllByLogicalOperator(logicalOperator));

        if (result.getObject() == null || result.getObject().isEmpty()) {
            String arg = "with logical operator ".concat(logicalOperator);
            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                    Messages.get("error.notif.req.not.found", arg));
        }

        return result;
    }
}
