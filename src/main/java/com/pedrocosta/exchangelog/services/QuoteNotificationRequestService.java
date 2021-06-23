package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.models.User;

import java.util.Collection;
import java.util.List;

public interface QuoteNotificationRequestService extends RepositoryService<QuoteNotificationRequest> {

    /**
     * Save a notification request into database.
     *
     * @param quoteNotifReq Notification request to save
     *
     * @return  Saved notification request.
     *          If error, throws {@link SaveDataException} with error message.
     * @throws SaveDataException if not well saved
     */
    @Override QuoteNotificationRequest save(QuoteNotificationRequest quoteNotifReq) throws SaveDataException;

    /**
     * Save all notification request from a list into database.
     *
     * @param col   List of notification requests to save
     *
     * @return  {@link ServiceResponse} object with saved notification requests.
     *          If error, returns {@link ServiceResponse} with error message.
     */
    @Override List<QuoteNotificationRequest> saveAll(Collection<QuoteNotificationRequest> col) throws SaveDataException;

    /**
     * Find a specific notification request from database.
     *
     * @param id Notification request's id in database
     *
     * @return  Found notification request.
     *          Null if not found.
     */
    @Override QuoteNotificationRequest find(long id);

    /**
     * Find a specific notification request from database.
     *
     * @param name Notification request's name
     *
     * @return  Found notification request.
     *          Null if not found.
     */
    QuoteNotificationRequest find(String name);

    /**
     * Find all notification requests in database.
     *
     * @return  List of found notification requests.
     *          Empty list if not found.
     */
    @Override List<QuoteNotificationRequest> findAll();

    /**
     * Find all notification requests in database of an user.
     *
     * @param user  {@link User} object.
     * @return  List of found notification requests.
     *          Empty list if not found.
     */
    List<QuoteNotificationRequest> findAll(User user);

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
    List<QuoteNotificationRequest> findAllByLogicalOperator(String logicalOperator);
}
