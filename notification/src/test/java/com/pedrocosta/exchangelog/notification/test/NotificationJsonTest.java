package com.pedrocosta.exchangelog.notification.test;

import com.google.gson.TypeAdapter;
import com.pedrocosta.exchangelog.notification.Mean;
import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.exchangelog.notification.NotificationAdapter;
import com.pedrocosta.utils.jsonmanager.JsonUtils;
import com.pedrocosta.utils.jsonmanager.adapter.UtilsTypeAdapter;
import com.pedrocosta.utils.jsonmanager.adapter.UtilsTypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotificationJsonTest {

    private UtilsTypeAdapterFactory jsonAdapterFactory;
    private JsonUtils jsonUtils;
    private Notification obj;
    private final String expected = "{" +
                "\"id\":1," +
                "\"mean\":\"EMAIL\"," +
                "\"from\":\"from@mail.com\"," +
                "\"to\":[\"to_01@mail.com\",\"to_02@mail.com\",\"to_03@mail.com\"]," +
                "\"subject\":\"My subject\"," +
                "\"body\":\"My e-mail body\"" +
            "}";

    @BeforeEach
    public void setUp() {
        jsonAdapterFactory = new UtilsTypeAdapterFactory();
        jsonUtils = new JsonUtils().setTypeAdapterFactory(jsonAdapterFactory);

        obj = new Notification();
        obj.setId(1);
        obj.setMean(Mean.EMAIL.name());
        obj.setFrom("from@mail.com");
        obj.setTo(new TreeSet<>(Arrays.asList("to_01@mail.com", "to_02@mail.com", "to_03@mail.com")));
        obj.setSubject("My subject");
        obj.setMessage("My e-mail body");
    }

    @Test
    public void testJsonAdapterInstantiate_success() {
        TypeAdapter<?> typeAdapter = jsonAdapterFactory.create(Notification.class);
        assert typeAdapter != null;
        assert typeAdapter instanceof NotificationAdapter;
    }

    @Test
    public void testJsonSerialize_success() {
        String json = jsonUtils.toJson(obj);
        assert json != null;
        assertEquals(expected, json);
    }

    @Test
    public void testJsonDeserialize_success() {
        Notification notification = jsonUtils.fromJson(expected, Notification.class);
        assert notification != null;
        assertEquals(obj, notification);
    }
}
