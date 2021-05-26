package com.pedrocosta.exchangelog.batch.jobs;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.models.UserContact;
import com.pedrocosta.exchangelog.services.ExchangeService;
import com.pedrocosta.exchangelog.services.QuoteNotificationRequestService;
import com.pedrocosta.exchangelog.utils.ContactTypes;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import com.pedrocosta.exchangelog.utils.ValueLogical;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
        List<Exception> exceptions = new ArrayList<>();
        list.forEach(quoteNotificationRequest -> { // For each
            switch (quoteNotificationRequest.getMeans()) {
                case APP:
                    Log.info(this, "Push notification to App");
                    // TODO Implement app notification send
                    break;
                case EMAIL:
                    UserContact emailContact = quoteNotificationRequest.getUser().getContacts()
                            .stream().filter(userContact ->
                                    ContactTypes.EMAIL.getName().equals(userContact.getName())
                            ).findFirst().orElse(null);

                    if (emailContact != null) {
                        Exchange exch = quoteNotificationRequest.getExchange();

                        String defaultFromAddress = getEnvironment().getProperty("mail.default.address");
                        NullPointerException defaultAddressNullEx =
                                new NullPointerException(Messages.get("error.default.mail.not.set"));
                        if (defaultFromAddress == null) {
                            if (!exceptions.contains(defaultAddressNullEx))
                                exceptions.add(defaultAddressNullEx);
                            break;
                        }

                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setFrom(defaultFromAddress);
                        message.setTo(emailContact.getValue());
                        message.setSubject(Messages.get("notify.quote.title"));
                        message.setText(Messages.get("notify.target.value",
                                exch.getBaseCurrency().getCode(),
                                exch.getQuoteCurrency().getCode(),
                                String.valueOf(quoteNotificationRequest.getQuoteValue())));

                        // Send e-mail
                        getContext().getBean(JavaMailSender.class).send(message);
                    } else {
                        exceptions.add(new NullPointerException(
                                Messages.get("error.contact.not.exists",
                                        "E-mail",
                                        String.valueOf(quoteNotificationRequest.getUser().getId()))));
                    }
                    break;
                default:
                    exceptions.add(new IllegalArgumentException(
                            Messages.get("error.value.not.set",
                                    "Mean", "notification request")));
            }
        });

        if (!exceptions.isEmpty())
            Log.warn(this, exceptions.toString());
    }
}
