package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.adapters.AdapterFactory;
import com.pedrocosta.exchangelog.utils.notifications.NotificationMeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Calendar;

public class QuoteNotificationRequestJsonTest {

    private JsonUtils jsonUtils;
    private QuoteNotificationRequest quoteNotificationRequest;

    @BeforeEach
    public void setUp() {
        jsonUtils = new JsonUtils(new AdapterFactory());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.JUNE, 8);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        quoteNotificationRequest = new QuoteNotificationRequest()
                .setQuoteValue(2.5D)
                .setExchange(new Exchange(new Currency("EUR", null),
                                            new Currency("USD", null),
                                            BigDecimal.valueOf(0.82D),
                                            calendar.getTime()).setId(67))
                .setExchStartDate(calendar.getTime())
                .setExchEndDate(calendar.getTime())
                .setLogicalOperator(ValueLogical.GREATER_AND_EQUALS.getOperator())
                .setPeriod(1)
                .setPeriodType(PeriodType.YEARS.name());
        quoteNotificationRequest.setId(123);
        quoteNotificationRequest.setMeans(NotificationMeans.EMAIL);
        quoteNotificationRequest.setName("Test Notification");
        quoteNotificationRequest.setEnabled(true);
        quoteNotificationRequest.setUser(new User()
                .setId(123)
                .setName("persontest")
                .setPassword("123456test!")
                .addContact(new UserContact()
                        .setId(1)
                        .setType(ContactTypes.EMAIL)
                        .setValue("test@test.com"))
                .addContact(new UserContact()
                        .setId(2)
                        .setType(ContactTypes.PHONE)
                        .setValue("11111111"))
                .setPerson(new Person()
                        .setId(55)
                        .setName("Person Test")));
    }

    @Test
    public void testCreateJson() {
        String generateJson = jsonUtils.toJson(quoteNotificationRequest);
        Log.info(this, generateJson);
        assert jsonForQuoteNotificationRequestSerialize().equals(generateJson);
    }

    @Test
    public void testCreateObjectFromJson() {
        QuoteNotificationRequest deserialized = jsonUtils.fromJson(
                jsonForQuoteNotificationRequestDeserialize(), QuoteNotificationRequest.class);
        assert isExact(quoteNotificationRequest, deserialized);
    }

    private String jsonForQuoteNotificationRequestSerialize() {
        String result =
        "{" +
            "\"name\":\"Test Notification\"" +
            ",\"means\":\"email\"" +
            ",\"enabled\":true" +
            ",\"quote_value\":2.5" +
            ",\"logical_operator\":\"\\u003e\\u003d\"" +
            ",\"exchange\":" +
            "{" +
                "\"base\":\"EUR\"" +
                ",\"quote\":\"USD\"" +
                ",\"rate\":0.82" +
                ",\"date\":\"2021-06-08\"" +
            "}" +
            ",\"start_date\":\"2021-06-08\"" +
            ",\"end_date\":\"2021-06-08\"" +
            ",\"period\":1" +
            ",\"period_type\":\"YEARS\"" +
            ",\"user\":{" +
                "\"username\":\"persontest\"" +
                ",\"contacts\":[" +
                    "{" +
                        "\"email\":\"test@test.com\"" +
                    "}" +
                    ",{" +
                        "\"phone\":\"11111111\"" +
                    "}" +
                "]" +
                ",\"name\":\"Person Test\"" +
            "}" +
        "}";

        return result;
    }

    private String jsonForQuoteNotificationRequestDeserialize() {
        String result =
                "{" +
                        "\"quote_value\":2.5" +
                        ",\"logical_operator\":\"\\u003e\\u003d\"" +
                        ",\"exchange\":" +
                        "{" +
                            "\"id\":67" +
                            ",\"base\":\"EUR\"" +
                            ",\"quote\":\"USD\"" +
                            ",\"rate\":0.82" +
                            ",\"date\":\"2021-06-08\"" +
                        "}" +
                        ",\"start_date\":\"2021-06-08\"" +
                        ",\"end_date\":\"2021-06-08\"" +
                        ",\"period\":1" +
                        ",\"period_type\":\"YEARS\"" +
                        ",\"id\":123" +
                        ",\"name\":\"Test Notification\"" +
                        ",\"means\":\"EMAIL\"" +
                        ",\"enabled\":true" +
                        ",\"user\":{" +
                            "\"username\":\"persontest\"" +
                            ",\"contacts\":[" +
                                "{" +
                                    "\"email\":\"test@test.com\"" +
                                "}" +
                                ",{" +
                                    "\"phone\":\"11111111\"" +
                                "}" +
                            "]" +
                            ",\"name\":\"Person Test\"" +
                        "}" +
                "}";

        return result;
    }

    private boolean isExact(QuoteNotificationRequest o1, QuoteNotificationRequest o2) {
        return o1.getId() == o2.getId()
                && o1.getName().equals(o2.getName())
                && o1.getMeans().equals(o2.getMeans())
                && o1.getQuoteValue().equals(o2.getQuoteValue())
                && o1.getExchStartDate().equals(o2.getExchStartDate())
                && o1.getExchEndDate().equals(o2.getExchEndDate())
                && o1.getLogicalOperator().equals(o2.getLogicalOperator())
                && o1.getPeriod() == o2.getPeriod()
                && o1.getPeriodType().equals(o2.getPeriodType())
                && o1.isEnabled() == o2.isEnabled()
                && (o1.getExchange() == o2.getExchange() || o1.getExchange().equals(o2.getExchange()))
                && (o1.getUser() == o2.getUser() || o1.getUser().equals(o2.getUser()));
    }
}
