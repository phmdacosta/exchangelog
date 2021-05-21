package com.pedrocosta.exchangelog.batch.jobs;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.services.ExchangeService;
import com.pedrocosta.exchangelog.services.QuoteNotificationRequestService;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.ValueLogical;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

/**
 * Task to send notifications to users based on it's notification configuration.
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class SendNotificationQuoteByEmailTask extends ScheduledTask<List<QuoteNotificationRequest>, List<QuoteNotificationRequest>> {

    @Override
    public List<QuoteNotificationRequest> doRead() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                getServiceFactory().create(QuoteNotificationRequestService.class);
        return service.findAll();
    }

    @Override
    public List<QuoteNotificationRequest> doProcess(List<QuoteNotificationRequest> notifRequests) throws Exception {

        List<QuoteNotificationRequest> notificationsToLaunch = new ArrayList<>();

        for (QuoteNotificationRequest notifReq : notifRequests) {
            ExchangeService exchangeService = (ExchangeService)
                    getServiceFactory().create(ExchangeService.class);
            Exchange notReqExch = notifReq.getExchange();

            if (notReqExch != null) {
                Exchange mostCurrentExch = exchangeService.findLast(
                        notReqExch.getBaseCurrency(), notReqExch.getQuoteCurrency());
                if (mostCurrentExch != null) {
                    // Check if target logic was reached
                    ValueLogical valLogical = ValueLogical.get(notifReq.getLogicalOperator());
                    if (valLogical != null
                            && valLogical.assertTrue(mostCurrentExch.getRate(),
                                                     notifReq.getQuoteValue())) {
                        notificationsToLaunch.add(notifReq);
                    }
                }
            }
        }

        return notificationsToLaunch;
    }

    @Override
    public void doWrite(List<QuoteNotificationRequest> list) throws Exception {
        list.forEach(quoteNotificationRequest -> { // For each
            switch (quoteNotificationRequest.getMeans()) {
                case APP:
                    Log.info(this, "Push notification to App");
                    // TODO Implement app notification send
                    break;
                case EMAIL:
                    Log.info(this, "Sending notification to e-mail");
                    // TODO Implement email send
                    break;
                default:
            }
        });
    }
}
