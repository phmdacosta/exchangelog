package com.pedrocosta.exchangelog.batch.jobs;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.models.*;
import com.pedrocosta.exchangelog.services.ExchangeService;
import com.pedrocosta.exchangelog.services.QuoteNotificationRequestService;
import com.pedrocosta.exchangelog.utils.*;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class NotifyMinimumRateTask extends ScheduledTask<List<QuoteNotificationRequest>, List<NotificationMessage>> {

    @Override
    public List<QuoteNotificationRequest> doRead() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                getServiceFactory().create(QuoteNotificationRequestService.class);
        return service.findAllByLogicalOperator(ValueLogical.MINIMUM.getOperator());
    }

    @Override
    public List<NotificationMessage> doProcess(List<QuoteNotificationRequest> notificationRequests) throws Exception {
        List<NotificationMessage> notifToSend = new ArrayList<>();

        if (!notificationRequests.isEmpty()) {
            notificationRequests.forEach(notifReq -> {
                ExchangeService exchService = (ExchangeService)
                        getServiceFactory().create(ExchangeService.class);

                Date endDate = new Date();
                // Getting start date
                int period = notifReq.getPeriod() * -1; //Backward
                Date startDate = DateUtils.addPeriod(endDate,
                        notifReq.getPeriodType(), period);

                if (startDate != null) {
                    Exchange lastExchange = exchService.findLast(
                            notifReq.getExchange().getBaseCurrency(),
                            notifReq.getExchange().getQuoteCurrency());

                    Exchange minRateExch = exchService.findWithMinRate(
                            startDate, endDate);

                    if (lastExchange != null
                            && lastExchange.equals(minRateExch)) {

                        NotificationMessage notifMsg = new NotificationMessage(notifReq);

                        notifMsg.setMessage(Messages.get("notify.min.value",
                                notifReq.getExchange().getBaseCurrency().getCode(),
                                String.valueOf(minRateExch.getRate()),
                                notifReq.getExchange().getQuoteCurrency().getCode(),
                                String.valueOf(notifReq.getExchStartDate())));
                        notifToSend.add(notifMsg);
                    }
                }
            });
        }

        return notifToSend;
    }

    @Override
    public void doWrite(List<NotificationMessage> list) throws Exception {
        List<Exception> exceptions = new ArrayList<>();

        list.forEach(notifMessage -> { // For each
            switch (notifMessage.getNotificationRequest().getMeans()) {
                case APP:
                    Log.info(this, "Push notification to App");
                    // TODO Implement app notification send
                    break;
                case EMAIL:
                    NotificationRequest notifRequest = notifMessage.getNotificationRequest();
                    UserContact emailContact = notifRequest.getUser().getContacts()
                            .stream().filter(userContact ->
                                    ContactTypes.EMAIL.getName().equals(userContact.getName())
                            ).findFirst().orElse(null);

                    if (emailContact != null) {

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
                        message.setText(notifMessage.getMessage());

                        // Send e-mail
                        getContext().getBean(JavaMailSender.class).send(message);
                    } else {
                        exceptions.add(new NullPointerException(
                                Messages.get("error.contact.not.exists",
                                        "E-mail",
                                        String.valueOf(notifRequest.getUser().getId()))));
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

    private boolean isMinimumRate(List<Exchange> list, BigDecimal rate) {
        boolean isMin = true;

        for (Exchange exchange : list) {
            if (ValueLogical.GREATER_THEN.assertTrue(rate, exchange.getRate())) {
                isMin = false;
            }
        }

        return isMin;
    }
}
