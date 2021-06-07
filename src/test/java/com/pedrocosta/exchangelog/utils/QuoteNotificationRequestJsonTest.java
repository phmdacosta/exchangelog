package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.models.User;
import com.pedrocosta.exchangelog.utils.notifications.NotificationMeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class QuoteNotificationRequestJsonTest {

    private GsonUtils gsonUtils;
    private QuoteNotificationRequest quoteNotificationRequest;

    @BeforeEach
    public void setUp() {
        gsonUtils = new GsonUtils(null);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        quoteNotificationRequest = new QuoteNotificationRequest()
                .setQuoteValue(2.5D)
                .setExchange(new Exchange(new Currency("EUR", null),
                                            new Currency("USD", null),
                                            BigDecimal.valueOf(0.82D), calendar.getTime()))
                .setExchStartDate(calendar.getTime())
                .setExchEndDate(calendar.getTime())
                .setLogicalOperator(ValueLogical.GREATER_AND_EQUALS.getOperator())
                .setPeriod(1)
                .setPeriodType(PeriodType.YEARS.name());
        quoteNotificationRequest.setId(123);
        quoteNotificationRequest.setMeans(NotificationMeans.EMAIL);
        quoteNotificationRequest.setName("Test Notification");
        quoteNotificationRequest.setEnabled(true);
        quoteNotificationRequest.setUser(new User());
    }

    @Test
    public void testCreateJson() {
        String generateJson = gsonUtils.toJson(quoteNotificationRequest);
        assert jsonForQuoteNotificationRequestSerialize().equals(generateJson);
    }

    @Test
    public void testCreateObjectFromJson() {
        QuoteNotificationRequest deserialized = gsonUtils.fromJson(
                jsonForQuoteNotificationRequestDeserialize(), QuoteNotificationRequest.class);
        assert isExact(quoteNotificationRequest, deserialized);
    }

    private String jsonForQuoteNotificationRequestSerialize() {
        String result =
        "{" +
            "\"quote_value\":2.5," +
            "\"logical_operator\":\">=\"," +
            "\"exchange\":" +
            "{" +
                "\"base_currency\":" +
                "{" +
                    "\"code\":\"EUR\"" +
                "}," +
                "\"quote_currency\":" +
                "{" +
                    "\"code\":\"USD\"" +
                "}," +
                "\"value_date\":\"Jun 2, 2021, 00:00:00 AM\"," +
                "\"rate\":0.82" +
            "}," +
            "\"exch_start_date\":\"Jun 2, 2021, 00:00:00 AM\"," +
            "\"exch_end_date\":\"Jun 2, 2021, 00:00:00 AM\"," +
            "\"period\":1," +
            "\"period_type\":\"YEARS\"," +
            "\"name\":\"Test Notification\"," +
            "\"means\":\"EMAIL\"," +
            "\"enabled\":true," +
            "\"user\":" +
            "{" +
                "\"contacts\":[]" +
            "}" +
        "}";

        return result;
    }

    private String jsonForQuoteNotificationRequestDeserialize() {
        String result =
                "{" +
                        "\"quote_value\":2.5," +
                        "\"logical_operator\":\">=\"," +
                        "\"exchange\":" +
                        "{" +
                            "\"id\":0," +
                            "\"base_currency\":" +
                            "{" +
                                "\"id\":0," +
                                "\"code\":\"EUR\"" +
                            "}," +
                            "\"quote_currency\":" +
                            "{" +
                                "\"id\":0," +
                                "\"code\":\"USD\"" +
                            "}," +
                            "\"value_date\":\"Jun 2, 2021, 00:00:00 AM\"," +
                            "\"rate\":0.82" +
                        "}," +
                        "\"exch_start_date\":\"Jun 2, 2021, 00:00:00 AM\"," +
                        "\"exch_end_date\":\"Jun 2, 2021, 00:00:00 AM\"," +
                        "\"period\":1," +
                        "\"period_type\":\"YEARS\"," +
                        "\"id\":123," +
                        "\"name\":\"Test Notification\"," +
                        "\"means\":\"EMAIL\"," +
                        "\"enabled\":true," +
                        "\"user\":" +
                        "{" +
                            "\"id\":0," +
                            "\"contacts\":[]" +
                        "}" +
                "}";

        return result;
    }

    private boolean isExact(QuoteNotificationRequest o1, QuoteNotificationRequest o2) {
        return o1.getId() == o2.getId()
                && o1.getName().equals(o2.getName())
                && o1.getMeans().equals(o2.getMeans())
                && o1.getUser().equals(o2.getUser())
                && o1.getQuoteValue().equals(o2.getQuoteValue())
                && o1.getExchange().equals(o2.getExchange())
                && o1.getExchStartDate().equals(o2.getExchStartDate())
                && o1.getExchEndDate().equals(o2.getExchEndDate())
                && o1.getLogicalOperator().equals(o2.getLogicalOperator())
                && o1.getPeriod() == o2.getPeriod()
                && o1.getPeriodType().equals(o2.getPeriodType())
                && o1.isEnabled() == o2.isEnabled();
    }
}
