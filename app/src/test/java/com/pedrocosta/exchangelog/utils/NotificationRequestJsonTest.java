package com.pedrocosta.exchangelog.utils;

import com.pedrocosta.exchangelog.notification.NotificationRequest;
import com.pedrocosta.exchangelog.notification.utils.NotificationMeans;
import com.pedrocosta.exchangelog.services.api.utils.ContactTypes;
import com.pedrocosta.exchangelog.user.User;
import com.pedrocosta.exchangelog.user.UserContact;
import com.pedrocosta.exchangelog.user.models.Person;
import com.pedrocosta.json.manager.adapter.AdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

public class NotificationRequestJsonTest {

    private JsonUtils jsonUtils;
    private NotificationRequest notificationRequest;

    @BeforeEach
    public void setUp() {
        jsonUtils = new JsonUtils(new AdapterFactory());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.JUNE, 8);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        notificationRequest = new NotificationRequest()
                .setId(123)
                .setMeans(NotificationMeans.EMAIL)
                .setName("Test Notification")
                .setEnabled(true);
        notificationRequest.setUser(new User()
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
        String generateJson = jsonUtils.toJson(notificationRequest);
        Log.info(this, generateJson);
        assert jsonForNotificationRequestSerialize().equals(generateJson);
    }

    @Test
    public void testCreateObjectFromJson() {
        NotificationRequest deserialized = jsonUtils.fromJson(
                jsonForNotificationRequestDeserialize(), NotificationRequest.class);
        assert isExact(notificationRequest, deserialized);
    }

    private String jsonForNotificationRequestSerialize() {
        String result =
        "{" +
            "\"name\":\"Test Notification\"" +
            ",\"means\":\"email\"" +
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

    private String jsonForNotificationRequestDeserialize() {
        String result =
                "{" +
                        "\"id\":123" +
                        ",\"name\":\"Test Notification\"" +
                        ",\"means\":\"EMAIL\"" +
                        ",\"enabled\":true" +
                        ",\"user\":{" +
                            "\"id\":123" +
                            ",\"username\":\"persontest\"" +
                            ",\"password\":\"123456test!\"" +
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

    private boolean isExact(NotificationRequest o1, NotificationRequest o2) {
        return o1.getId() == o2.getId()
                && o1.getName().equals(o2.getName())
                && o1.getMeans().equals(o2.getMeans())
                && o1.isEnabled() == o2.isEnabled()
                && o1.getUser().equals(o2.getUser());
    }
}
