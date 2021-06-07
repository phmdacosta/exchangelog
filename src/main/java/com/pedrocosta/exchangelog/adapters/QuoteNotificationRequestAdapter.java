package com.pedrocosta.exchangelog.adapters;

import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.models.NotificationRequest;

import java.io.IOException;

public class QuoteNotificationRequestAdapter extends NotificationRequestAdapter {

    protected final String QUOTE_VALUE = "quote_value";
    protected final String LOGICAL_OPERATOR = "logical_operator";
    protected final String EXCHANGE = "exchange";
    protected final String STAR_DATE = "star_date";
    protected final String END_DATE = "end_date";
    protected final String PERIOD = "period";
    protected final String PERIOD_TYPE = "period_type";

    @Override
    public void write(JsonWriter writer, NotificationRequest notificationRequest) throws IOException {
        super.write(writer, notificationRequest);
    }

    @Override
    protected void writeObject(JsonWriter writer, NotificationRequest notificationRequest) throws IOException {
        NotificationRequest quoteNotifReq = (NotificationRequest) notificationRequest;
        super.writeObject(writer, notificationRequest);
        writer.name(QUOTE_VALUE);
        writer.value(notificationRequest.getName());
        writer.name(LOGICAL_OPERATOR);
        writer.value(notificationRequest.getMeans().name().toLowerCase());
        writer.name(STAR_DATE);
        writer.value(notificationRequest.isEnabled());
        writer.name(END_DATE);
        writer.value(notificationRequest.isEnabled());
        writer.name(PERIOD);
        writer.value(notificationRequest.isEnabled());
        writer.name(PERIOD_TYPE);
        writer.value(notificationRequest.isEnabled());
    }
}
