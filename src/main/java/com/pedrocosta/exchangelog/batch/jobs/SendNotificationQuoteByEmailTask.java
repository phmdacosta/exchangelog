package com.pedrocosta.exchangelog.batch.jobs;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.models.UserContact;
import com.pedrocosta.exchangelog.services.ExchangeService;
import com.pedrocosta.exchangelog.services.QuoteNotificationRequestService;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import com.pedrocosta.exchangelog.utils.ValueLogical;
import com.pedrocosta.exchangelog.utils.notifications.NotificationSender;
import com.pedrocosta.exchangelog.utils.notifications.NotificationSenderFactory;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Task to send notifications to users based on it's notification configuration.
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class SendNotificationQuoteByEmailTask extends ScheduledTask<List<QuoteNotificationRequest>, List<QuoteNotificationRequest>> {

    @Override
    public List<QuoteNotificationRequest> doRead() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return getServiceFactory().create(QuoteNotificationRequestService.class)
                .findAll();
    }

    @Override
    public List<QuoteNotificationRequest> doProcess(List<QuoteNotificationRequest> notifRequests) throws Exception {

        List<QuoteNotificationRequest> notificationsToLaunch = new ArrayList<>();

        for (QuoteNotificationRequest notifReq : notifRequests) {
            ExchangeService exchangeService = getServiceFactory()
                    .create(ExchangeService.class);
            Exchange notReqExch = notifReq.getExchange();

            if (notReqExch != null) {
                if (notReqExch.getBaseCurrency() == null
                        || notReqExch.getQuoteCurrency() == null) {
                    Log.warn(this, (Messages.get("error.ccy.not.set",
                            "Base or quote", "notification request")));
                    continue;
                }

                Exchange mostCurrentExch = exchangeService.findLast(
                        notReqExch.getBaseCurrency(), notReqExch.getQuoteCurrency());
                // Check if target logic was reached
                ValueLogical valLogical = ValueLogical.get(notifReq.getLogicalOperator());
                if (valLogical != null
                        && valLogical.assertTrue(mostCurrentExch.getRate(),
                                                 notifReq.getQuoteValue())) {
                    notificationsToLaunch.add(notifReq);
                }
            }
        }

        return notificationsToLaunch;
    }

    @Override
    public void doWrite(List<QuoteNotificationRequest> list) throws Exception {
        Log.info(this, Messages.get("notify.total.to.send", String.valueOf(list.size())));
        List<String> errorMsgs = new ArrayList<>();
        AtomicInteger countSent = new AtomicInteger();

        list.forEach(quoteNotificationRequest -> { // For each
            UserContact contact = quoteNotificationRequest.getUser().getContacts()
                    .stream().filter(userContact ->
                            quoteNotificationRequest.getMeans().equals(userContact.getType().getMeans())
                    ).findFirst().orElse(null);

            if (contact != null) {
                Exchange exch = quoteNotificationRequest.getExchange();

                NotificationSender sender = NotificationSenderFactory.create(
                        getContext(), quoteNotificationRequest.getMeans());

                sender.setTo(contact.getValue());
                sender.setSubject(Messages.get("notify.quote.title"));
                sender.setBody(Messages.get("notify.target.value",
                        exch.getBaseCurrency().getCode(),
                        exch.getQuoteCurrency().getCode(),
                        String.valueOf(quoteNotificationRequest.getQuoteValue())));
                sender.send();
                countSent.getAndIncrement();
            } else {
                errorMsgs.add(Messages.get("error.contact.not.exists",
                        quoteNotificationRequest.getMeans().name(),
                        String.valueOf(quoteNotificationRequest.getUser().getId())));
            }
        });

        Log.info(this, Messages.get("notify.total.sent", countSent.toString()));

        if (!errorMsgs.isEmpty())
            Log.warn(this, Messages.get("error.list.notify.send",
                    errorMsgs.toString()));
    }
}
