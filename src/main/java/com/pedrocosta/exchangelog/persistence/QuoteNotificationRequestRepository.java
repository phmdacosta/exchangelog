package com.pedrocosta.exchangelog.persistence;

import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteNotificationRequestRepository extends JpaRepository<QuoteNotificationRequest, Long> {

    /**
     * Search in database for quote notification request by its name.
     *
     * @param name  Name that identifies notification request.
     * @return  {@link QuoteNotificationRequest} object if it exists.
     */
    QuoteNotificationRequest findByName(String name);

    /**
     * Search in database for quote notification request by logical operator.
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
     * @param logicalOperator   Logical operator to search.
     * @return  {@link QuoteNotificationRequest} object if it exists.
     */
    List<QuoteNotificationRequest> findAllByLogicalOperator(String logicalOperator);
}
