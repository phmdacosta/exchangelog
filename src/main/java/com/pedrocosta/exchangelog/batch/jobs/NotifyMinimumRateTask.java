package com.pedrocosta.exchangelog.batch.jobs;

import com.pedrocosta.exchangelog.batch.ScheduledTask;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.NotificationMessage;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.services.ExchangeService;
import com.pedrocosta.exchangelog.services.QuoteNotificationRequestService;
import com.pedrocosta.exchangelog.utils.DateUtils;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.ValueLogical;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

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

                    /*
                    Only send notifications if minimum rate exchange is not the last
                     */
                    if (lastExchange != null
                            && !lastExchange.equals(minRateExch)
                            && ValueLogical.LESS_THEN.assertTrue(
                            lastExchange.getRate(), minRateExch.getRate())) {

                        NotificationMessage notifMsg = new NotificationMessage(notifReq);
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append("Dollar reached its lowest value ("+ 0 +") against the Euro since " + notifReq.getExchStartDate());
                        notifMsg.setMessage("");
                        notifToSend.add(notifMsg);
                    }
                }
            });
        }

        return notifToSend;
    }

    @Override
    public void doWrite(List<NotificationMessage> list) throws Exception {
        list.forEach(notifMessage -> { // For each
            switch (notifMessage.getNotificationRequest().getMeans()) {
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
