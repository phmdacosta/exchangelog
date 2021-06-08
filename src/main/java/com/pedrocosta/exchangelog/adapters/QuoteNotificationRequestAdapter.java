package com.pedrocosta.exchangelog.adapters;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.utils.DateUtils;
import com.pedrocosta.exchangelog.utils.GsonUtils;

import java.io.IOException;

public class QuoteNotificationRequestAdapter extends NotificationRequestAdapter<QuoteNotificationRequest> {

    protected final String QUOTE_VALUE = "quote_value";
    protected final String LOGICAL_OPERATOR = "logical_operator";
    protected final String EXCHANGE = "exchange";
    protected final String STAR_DATE = "start_date";
    protected final String END_DATE = "end_date";
    protected final String PERIOD = "period";
    protected final String PERIOD_TYPE = "period_type";

    public QuoteNotificationRequestAdapter(GsonUtils gsonUtils) {
        super(gsonUtils);
    }

    @Override
    protected void writeJson(JsonWriter writer, QuoteNotificationRequest quoteNotifReq) throws IOException {
        writer.name(QUOTE_VALUE);
        writer.value(quoteNotifReq.getQuoteValue());
        writer.name(LOGICAL_OPERATOR);
        writer.value(quoteNotifReq.getLogicalOperator());
        writer.name(EXCHANGE);
        writer.jsonValue(getGsonUtils().toJson(quoteNotifReq.getExchange()));
        writer.name(STAR_DATE);
        writer.value(DateUtils.dateToString(quoteNotifReq.getExchStartDate()));
        writer.name(END_DATE);
        writer.value(DateUtils.dateToString(quoteNotifReq.getExchEndDate()));
        writer.name(PERIOD);
        writer.value(quoteNotifReq.getPeriod());
        writer.name(PERIOD_TYPE);
        writer.value(quoteNotifReq.getPeriodType());
    }

    @Override
    protected QuoteNotificationRequest readJson(JsonReader reader) throws IOException {
        QuoteNotificationRequest quoteNotifReq = new QuoteNotificationRequest();
        String fieldName = "";
        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (JsonToken.NAME.equals(token)) {
                fieldName = reader.nextName();
            }

            switch (fieldName) {
                case QUOTE_VALUE:
                    token = reader.peek();
                    quoteNotifReq.setQuoteValue(reader.nextDouble());
                    break;
                case LOGICAL_OPERATOR:
                    token = reader.peek();
                    quoteNotifReq.setLogicalOperator(reader.nextString());
                    break;
                case STAR_DATE:
                    token = reader.peek();
                    quoteNotifReq.setExchStartDate(DateUtils
                            .stringToDate(reader.nextString()));
                    break;
                case END_DATE:
                    token = reader.peek();
                    quoteNotifReq.setExchEndDate(DateUtils
                            .stringToDate(reader.nextString()));
                    break;
                case PERIOD:
                    token = reader.peek();
                    quoteNotifReq.setPeriod(reader.nextInt());
                    break;
                case PERIOD_TYPE:
                    token = reader.peek();
                    quoteNotifReq.setPeriodType(reader.nextString());
                    break;
                case EXCHANGE:
                    token = reader.peek();
                    Exchange exchange = getGsonUtils().fromJson(reader, Exchange.class);
                    quoteNotifReq.setExchange(exchange);
                    break;
                default:
                    readParent(reader, fieldName, quoteNotifReq);
            }
        }
        return quoteNotifReq;
    }
}
