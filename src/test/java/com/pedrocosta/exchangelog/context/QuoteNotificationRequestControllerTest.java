package com.pedrocosta.exchangelog.context;

import com.pedrocosta.exchangelog.ExchangelogApplication;
import com.pedrocosta.exchangelog.controller.NotificationRequestController;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import com.pedrocosta.exchangelog.utils.Messages;
import com.pedrocosta.exchangelog.utils.PeriodType;
import com.pedrocosta.exchangelog.utils.ValueLogical;
import com.pedrocosta.exchangelog.utils.notifications.NotificationMeans;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
@SpringBootTest(classes = ExchangelogApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuoteNotificationRequestControllerTest {

    private final String NOTIFICATION_NAME = "Test Notification";
//    private final long NOTIFICATION_ID = 123;
    private long notificationId = 0;

    @Autowired
    private ApplicationContext context;
    @Autowired
    private NotificationRequestController controller;
    @Autowired
    private GsonUtils gsonUtils;
    private QuoteNotificationRequest quoteNotificationRequest;

    @BeforeEach
    public void setUp() {
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
//        quoteNotificationRequest.setId(NOTIFICATION_ID);
        quoteNotificationRequest.setMeans(NotificationMeans.EMAIL);
        quoteNotificationRequest.setName(NOTIFICATION_NAME);
        quoteNotificationRequest.setEnabled(true);
    }

    @Test
    @Order(1)
    public void test_saveQuoteNotification_success() {
        String resp = controller.saveQuoteNotificationRequest(mockJson());
        ServiceResponse<?> servResp =
                gsonUtils.fromJson(resp, ServiceResponse.class);
        assert servResp.isSuccess();
        notificationId = ((QuoteNotificationRequest) servResp.getObject()).getId();
    }

    @Test
    @Order(1)
    public void test_saveQuoteNotification_errorEmptyParam() {
        String resp = controller.saveQuoteNotificationRequest("");
        ServiceResponse<?> servResp =
                gsonUtils.fromJson(resp, ServiceResponse.class);
        assert !servResp.isSuccess();
        assert HttpStatus.BAD_REQUEST.equals(servResp.getCode());
        assert Messages.get("error.request.body").equals(servResp.getMessage());
    }

    @Test
    @Order(1)
    public void test_saveQuoteNotification_errorNullParam() {
        String resp = controller.saveQuoteNotificationRequest(null);
        ServiceResponse<?> servResp =
                gsonUtils.fromJson(resp, ServiceResponse.class);
        assert !servResp.isSuccess();
        assert HttpStatus.BAD_REQUEST.equals(servResp.getCode());
        assert Messages.get("error.request.body").equals(servResp.getMessage());
    }

    @Test
    @Order(2)
    public void test_getQuoteNotificationById_success() {
        String resp = controller.getQuoteNotification(notificationId,
                null, null);
        ServiceResponse<?> servResp =
                gsonUtils.fromJson(resp, ServiceResponse.class);
        assert servResp.isSuccess();
        assert quoteNotificationRequest.equals(servResp.getObject());
    }

    @Test
    @Order(2)
    public void test_getQuoteNotificationByName_success() {
        String resp = controller.getQuoteNotification(0, NOTIFICATION_NAME, null);
        ServiceResponse<?> servResp =
                gsonUtils.fromJson(resp, ServiceResponse.class);
        assert servResp.isSuccess();
        assert quoteNotificationRequest.equals(servResp.getObject());
    }

    @Test
    @Order(2)
    public void test_getQuoteNotificationByLogicalOperator_success() {
        String resp = controller.getQuoteNotification(0, null,
                ValueLogical.GREATER_AND_EQUALS.getOperator());
        ServiceResponse<?> servResp =
                gsonUtils.fromJson(resp, ServiceResponse.class);
        assert servResp.isSuccess();
        assert quoteNotificationRequest.equals(servResp.getObject());
    }

    private String mockJson() {
        return "{" +
                    "\"name\":\"Test Notification\"," +
                    "\"means\":\"email\"," +
                    "\"enabled\":true," +
                    "\"quote_value\":2.5," +
                    "\"logical_operator\":\"\\u003e\\u003d\"," +
                    "\"exchange\":" +
                    "{" +
                        "\"base\":\"EUR\"," +
                        "\"quote\":\"USD\"" +
                    "}," +
                    "\"start_date\":\"2021-06-08\"," +
                    "\"end_date\":\"2021-06-08\"," +
                    "\"period\":1," +
                    "\"period_type\":\"YEARS\"" +
                "}";
    }
}
