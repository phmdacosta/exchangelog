package com.pedrocosta.exchangelog.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pedrocosta.exchangelog.exceptions.NotSupportedException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ExchangeAdapter extends JsonAdapter<Exchange> {

    protected final String ID = "id";
    protected final String BASE = "base";
    protected final String QUOTE = "quote";
    protected final String RATE = "rate";
    protected final String DATE = "date";

    @Override
    protected void writeJson(JsonWriter writer, Exchange exchange) throws IOException, NotSupportedException {
        writer.name(BASE);
        writer.value(exchange.getBaseCurrency().getCode());
        writer.name(QUOTE);
        writer.value(exchange.getQuoteCurrency().getCode());
        writer.name(RATE);
        writer.value(exchange.getRate());
        writer.name(DATE);
        writer.value(DateUtils.dateToString(exchange.getValueDate()));
    }

    @Override
    protected Exchange readJson(JsonReader reader) throws IOException, NotSupportedException {
        Exchange exchange = new Exchange();
        String fieldName = "";
        while (reader.hasNext()) {
            JsonToken token = reader.peek();

            if (JsonToken.NAME.equals(token)) {
                fieldName = reader.nextName();
            }

            switch (fieldName) {
                case ID:
                    token = reader.peek();
                    exchange.setId(reader.nextLong());
                    break;
                case BASE:
                    token = reader.peek();
                    exchange.setBaseCurrency(
                            new Currency(reader.nextString(), null));
                    break;
                case QUOTE:
                    token = reader.peek();
                    exchange.setQuoteCurrency(
                            new Currency(reader.nextString(), null));
                    break;
                case RATE:
                    token = reader.peek();
                    exchange.setRate(reader.nextDouble());
                    break;
                case DATE:
                    token = reader.peek();
                    exchange.setValueDate(
                            DateUtils.stringToDate(reader.nextString()));
                    break;
                default: reader.skipValue();
            }
        }
        return exchange;
    }
}
