package com.pedrocosta.exchangelog.services.impl;

import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.models.User;
import com.pedrocosta.exchangelog.persistence.QuoteNotificationRequestRepository;
import com.pedrocosta.exchangelog.services.*;
import com.pedrocosta.exchangelog.utils.Messages;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class QuoteNotificationRequestServiceImpl implements QuoteNotificationRequestService {

    private final QuoteNotificationRequestRepository repository;
    private ServiceFactory serviceFactory;

    public QuoteNotificationRequestServiceImpl(QuoteNotificationRequestRepository repository,
                                               ServiceFactory serviceFactory) {
        this.repository = repository;
        this.serviceFactory = serviceFactory;
    }

    /**
     * Save a notification request into database.
     *
     * @param quoteNotifReq Notification request to save
     *
     * @return  Saved notification request.
     *          If error, throws {@link SaveDataException} with error message.
     * @throws SaveDataException if not well saved
     */
    @Override
    @Transactional
    public QuoteNotificationRequest save(QuoteNotificationRequest quoteNotifReq) throws SaveDataException {
        if (!isValidParameter(quoteNotifReq)) {
            throw new SaveDataException(Messages.get("error.request.param"));
        }

        // Select saved currencies
        CurrencyService ccyServ = (CurrencyService) serviceFactory.create(Currency.class);
        try {
            Currency baseCcy = ccyServ.find(quoteNotifReq.getExchange().getBaseCurrency().getCode());
            if (baseCcy == null)
                throw new NoSuchDataException(Messages.get("error.ccy.not.found",
                        quoteNotifReq.getExchange().getBaseCurrency().getCode()));

            Currency quoteCcy = ccyServ.find(quoteNotifReq.getExchange().getQuoteCurrency().getCode());
            if (quoteCcy == null)
                throw new NoSuchDataException(Messages.get("error.ccy.not.found",
                        quoteNotifReq.getExchange().getQuoteCurrency().getCode()));

            // Get last exchange of currency pair
            ExchangeService exchServ = (ExchangeService) serviceFactory.create(Exchange.class);
            Exchange exchange = exchServ.findLast(baseCcy, quoteCcy);
            if (exchange == null)
                throw new NoSuchDataException(Messages.get("error.exch.not.found",
                                baseCcy.getCode(), quoteCcy.getCode()));

            quoteNotifReq.setExchange(exchange);
        } catch (NoSuchDataException e) {
            throw new SaveDataException(Messages.get("error.request.param"), e);
        }

        // Do save
        try {
            quoteNotifReq = repository.save(quoteNotifReq);
        } catch (Exception e) {
            throw new SaveDataException(Messages.get("error.not.saved", "notification"), e);
        }

        return quoteNotifReq;
//        if (!isValidParameter(quoteNotifReq)) {
//            return ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.request.param"));
//        }
//
//        // Select saved currencies
//        CurrencyService ccyServ = (CurrencyService) serviceFactory.create(Currency.class);
//        ServiceResponse<Currency> baseCcyResp = ccyServ.find(
//                quoteNotifReq.getExchange().getBaseCurrency().getCode());
//        ServiceResponse<Currency> quoteCcyResp = ccyServ.find(
//                quoteNotifReq.getExchange().getQuoteCurrency().getCode());
//
//        if (!baseCcyResp.isSuccess() || !quoteCcyResp.isSuccess()) {
//            return ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.request.param"));
//        }
//
//        // Get last exchange of currency pair
//        ExchangeService exchServ = (ExchangeService) serviceFactory.create(Exchange.class);
//        ServiceResponse<Exchange> exchResp = exchServ.findLast(
//                baseCcyResp.getObject(), quoteCcyResp.getObject());
//
//        if (!exchResp.isSuccess()) {
//            return ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.request.param"));
//        }
//
//        quoteNotifReq.setExchange(exchResp.getObject());
//
//        ServiceResponse<QuoteNotificationRequest> result =
//                ServiceResponse.<QuoteNotificationRequest>createSuccess(HttpStatus.CREATED)
//                        .setObject(repository.save(quoteNotifReq));
//
//        if (result.getObject().getId() == 0) {
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.not.saved", "notification"));
//        }
//        return result;
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
    @Transactional
    public List<QuoteNotificationRequest> saveAll(Collection<QuoteNotificationRequest> col) throws SaveDataException {
        for (QuoteNotificationRequest quoteNotifReq : col) {
            quoteNotifReq = save(quoteNotifReq);
        }

        return new ArrayList<>(col);

//        ServiceResponse<List<QuoteNotificationRequest>> result =
//                ServiceResponse.<List<QuoteNotificationRequest>>createSuccess(HttpStatus.CREATED)
//                        .setObject(repository.saveAll(col));
//
//        if (result.getObject() == null || result.getObject().isEmpty()) {
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.not.saved", "any notification request"));
//        }
//        else if(result.getObject().size() != col.size()) {
//            List<QuoteNotificationRequest> notSaved = new ArrayList<>();
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST);
//            ServiceResponse<List<QuoteNotificationRequest>> auxResult = result;
//            col.forEach(notifReq -> {
//                if (!auxResult.getObject().contains(notifReq)) {
//                    notSaved.add(notifReq);
//                }
//            });
//            String msg = Messages.get("error.some.notif.req.not.saved", notSaved.toString());
//            result.setMessage(msg);
//        }
//
//        return result;
    }

    /**
     * Find a specific notification request from database.
     *
     * @param id Notification request's id in database
     *
     * @return  Found notification request.
     *          Null if not found.
     */
    @Override
    public QuoteNotificationRequest find(long id) {
        return repository.findById(id).orElse(null);
//        ServiceResponse<QuoteNotificationRequest> result =
//                ServiceResponse.<QuoteNotificationRequest>createSuccess()
//                        .setObject(repository.findById(id).orElse(null));
//
//        if (result.getObject() == null) {
//            String arg = "with id " + id;
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("error.notif.req.not.found", arg));
//        }
//
//        return result;
    }

    /**
     * Find a specific notification request from database.
     *
     * @param name Notification request's name
     *
     * @return  Found notification request.
     *          Null if not found.
     */
    public QuoteNotificationRequest find(String name) {
        return repository.findByName(name);
//        ServiceResponse<QuoteNotificationRequest> result =
//                ServiceResponse.<QuoteNotificationRequest>createSuccess()
//                        .setObject(repository.findByName(name));
//
//        if (result.getObject() == null) {
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("error.notif.req.not.found", name));
//        }
//
//        return result;
    }

    /**
     * Find all notification requests in database.
     *
     * @return  List of found notification requests.
     *          Empty list if not found.
     */
    @Override
    public List<QuoteNotificationRequest> findAll() {
        return repository.findAll();
//        ServiceResponse<List<QuoteNotificationRequest>> result =
//                ServiceResponse.<List<QuoteNotificationRequest>>createSuccess()
//                        .setObject(repository.findAll());
//
//        if (result.getObject() == null || result.getObject().isEmpty()) {
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("could.not.find", "any notification request"));
//        }
//
//        return result;
    }

    /**
     * Find all notification requests in database of an user.
     *
     * @param user  {@link User} object.
     * @return  List of found notification requests.
     *          Empty list if not found.
     */
    public List<QuoteNotificationRequest> findAll(User user) {
        List<QuoteNotificationRequest> result = new ArrayList<>();
        if (user != null && user.getId() != 0) {
            result = repository.findAllByUser(user);
        }
        return result;
//        if (user == null) {
//            return ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.request.param"));
//        }
//
//        ServiceResponse<List<QuoteNotificationRequest>> result =
//                ServiceResponse.<List<QuoteNotificationRequest>>createSuccess()
//                        .setObject(repository.findAllByUser(user));
//
//        if (result.getObject() == null || result.getObject().isEmpty()) {
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("could.not.find", "any notification request"));
//        }
//
//        return result;
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
     * @return  List of found notification requests.
     *          Empty list if not found.
     */
    public List<QuoteNotificationRequest> findAllByLogicalOperator(String logicalOperator) {
        return repository.findAllByLogicalOperator(logicalOperator);
//        ServiceResponse<List<QuoteNotificationRequest>> result =
//                ServiceResponse.<List<QuoteNotificationRequest>>createSuccess()
//                        .setObject(repository.findAllByLogicalOperator(logicalOperator));
//
//        if (result.getObject() == null || result.getObject().isEmpty()) {
//            String arg = "with logical operator ".concat(logicalOperator);
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("error.notif.req.not.found", arg));
//        }
//
//        return result;
    }

    private boolean isValidParameter(QuoteNotificationRequest quoteNotificationRequest) {
        return quoteNotificationRequest != null
                && quoteNotificationRequest.getExchange() != null
                && quoteNotificationRequest.getExchange().getBaseCurrency() != null
                && quoteNotificationRequest.getExchange().getBaseCurrency().getCode() != null
                && !quoteNotificationRequest.getExchange().getBaseCurrency().getCode().isBlank()
                && quoteNotificationRequest.getExchange().getQuoteCurrency() != null
                && quoteNotificationRequest.getExchange().getQuoteCurrency().getCode() != null
                && !quoteNotificationRequest.getExchange().getQuoteCurrency().getCode().isBlank();
    }
}
