package com.pedrocosta.exchangelog.batch.jobs;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.models.*;
import com.pedrocosta.exchangelog.services.ExchangeService;
import com.pedrocosta.exchangelog.services.QuoteNotificationRequestService;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.utils.*;
import com.pedrocosta.exchangelog.utils.notifications.NotificationSender;
import com.pedrocosta.exchangelog.utils.notifications.NotificationSenderFactory;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class NotifyMinimumRateTask extends ScheduledTask<List<QuoteNotificationRequest>, List<NotificationMessage>> {

    @Override
    public List<QuoteNotificationRequest> doRead() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                getServiceFactory().create(QuoteNotificationRequestService.class);
        ServiceResponse<List<QuoteNotificationRequest>> response = service
                .findAllByLogicalOperator(ValueLogical.MINIMUM.getOperator());
        if (!response.isSuccess()) {
            Log.error(this, response.getMessage());
        }
        return response.getObject();
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
                    if (notifReq.getExchange().getBaseCurrency() == null
                            || notifReq.getExchange().getQuoteCurrency() == null) {
                        Log.warn(this, (Messages.get("error.ccy.not.set",
                                "Base or quote", "notification request")));
                        return;
                    }

                    ServiceResponse<Exchange> respLastExch = exchService.findLast(
                            notifReq.getExchange().getBaseCurrency(),
                            notifReq.getExchange().getQuoteCurrency());
                    if (!respLastExch.isSuccess()) {
                        Log.warn(this, respLastExch.getMessage());
                        return;
                    }

                    Exchange lastExchange = respLastExch.getObject();

                    ServiceResponse<Exchange> respMinRateExch = exchService.findWithMinRate(
                            startDate, endDate);
                    if (!respLastExch.isSuccess()) {
                        Log.warn(this, respLastExch.getMessage());
                        return;
                    }

                    Exchange minRateExch = respMinRateExch.getObject();

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
        Log.info(this, Messages.get("notify.total.to.send", String.valueOf(list.size())));
        List<String> errorMsgs = new ArrayList<>();
        AtomicInteger countSent = new AtomicInteger();

        list.forEach(notifMessage -> { // For each
            NotificationRequest notifRequest = notifMessage.getNotificationRequest();
            UserContact contact = notifRequest.getUser().getContacts()
                    .stream().filter(userContact ->
                            notifRequest.getMeans().equals(userContact.getType().getMeans())
                    ).findFirst().orElse(null);

            if (contact != null) {
                NotificationSender sender = NotificationSenderFactory.create(
                        getContext(), notifRequest.getMeans());

                sender.setTo(contact.getValue());
                sender.setSubject(Messages.get("notify.quote.title"));
                sender.setBody(Messages.get("notify.target.value",
                        notifMessage.getMessage()));
                sender.send();
                countSent.getAndIncrement();
            } else {
                errorMsgs.add(Messages.get("error.contact.not.exists",
                        notifRequest.getMeans().name(),
                        String.valueOf(notifRequest.getUser().getId())));
            }
        });

        Log.info(this, Messages.get("notify.total.sent", countSent.toString()));

        if (!errorMsgs.isEmpty())
            Log.warn(this, Messages.get("error.list.notify.send",
                    errorMsgs.toString()));
    }
}
