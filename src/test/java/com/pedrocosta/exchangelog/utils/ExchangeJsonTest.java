package com.pedrocosta.exchangelog.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedrocosta.exchangelog.adapters.ExchangeAdapter;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class ExchangeJsonTest {

    private Exchange exchange;

    @BeforeEach
    public void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        exchange = new Exchange(
                new Currency("EUR", ""),
                new Currency("USD", ""),
                BigDecimal.valueOf(2D),
                calendar.getTime());
    }

    @Test
    public void testJsonCreationFromObject() {
        GsonUtils gsonUtils = new GsonUtils(null, null);
        String generateJson = gsonUtils.toJson(exchange, new ExchangeAdapter());
        assert exchangeJson(exchange).equals(generateJson);
    }

    @Test
    public void testObjectCreationFromJson() {
        GsonUtils gsonUtils = new GsonUtils(null, null);
        Exchange newExch = gsonUtils.fromJson(exchangeJson(exchange),
                Exchange.class, new ExchangeAdapter());
        assert exchange.equals(newExch);
    }

    private String exchangeJson(Exchange exchange) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(exchange.getClass(), new ExchangeAdapter());
        Gson gson = builder.create();
        return gson.toJson(exchange);
    }

}
