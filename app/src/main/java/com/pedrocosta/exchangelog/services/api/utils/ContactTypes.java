package com.pedrocosta.exchangelog.services.api.utils;

import com.pedrocosta.exchangelog.notification.utils.NotificationMeans;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public enum ContactTypes {
    EMAIL("email", NotificationMeans.EMAIL),
    PHONE("phone", NotificationMeans.SMS),
    MOBILE_APP("mobile_app", NotificationMeans.APP);

    private final String name;
    private final NotificationMeans means;

    ContactTypes(String name, NotificationMeans means) {
        this.name = name;
        this.means = means;
    }

    public String getName() {
        return name;
    }

    public NotificationMeans getMeans() {
        return means;
    }

    public static ContactTypes get(String name) {
        return valueOf(name.toUpperCase());
    }
}
