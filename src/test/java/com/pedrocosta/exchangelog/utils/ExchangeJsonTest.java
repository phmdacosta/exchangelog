package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.adapters.AdapterFactory;
import com.pedrocosta.exchangelog.adapters.ExchangeAdapter;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class ExchangeJsonTest {

    private GsonUtils gsonUtils;
    private Exchange exchange;

    @BeforeEach
    public void setUp() {
        gsonUtils = new GsonUtils(new AdapterFactory());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.JUNE, 8);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        exchange = new Exchange(
                new Currency("EUR", ""),
                new Currency("USD", ""),
                BigDecimal.valueOf(0.82D),
                calendar.getTime())
                .setId(67);
    }

    @Test
    public void testJsonCreationFromObject() {
        String generateJson = gsonUtils.toJson(exchange);
        assert jsonForExchangeSerialize().equals(generateJson);
    }

    @Test
    public void testObjectCreationFromJson() {
        Exchange newExch = gsonUtils.fromJson(jsonForExchangeDeserialize(),
                Exchange.class);
        assert isExact(exchange, newExch);
    }

    private String jsonForExchangeSerialize() {
        String result =
                "{" +
                        "\"base\":\"EUR\"" +
                        ",\"quote\":\"USD\"" +
                        ",\"rate\":0.82" +
                        ",\"date\":\"2021-06-08\"" +
                "}";

        return result;
    }

    private String jsonForExchangeDeserialize() {
        String result =
                "{" +
                        "\"id\":67" +
                        ",\"base\":\"EUR\"" +
                        ",\"quote\":\"USD\"" +
                        ",\"rate\":0.82" +
                        ",\"date\":\"2021-06-08\"" +
                "}";

        return result;
    }

    private boolean isExact(Exchange o1, Exchange o2) {
        return o1.getId() == o2.getId()
                && o1.getBaseCurrency().getCode().equals(o2.getBaseCurrency().getCode())
                && o1.getQuoteCurrency().getCode().equals(o2.getQuoteCurrency().getCode())
                && o1.getRate().equals(o2.getRate())
                && o1.getValueDate().equals(o2.getValueDate());
    }

}
