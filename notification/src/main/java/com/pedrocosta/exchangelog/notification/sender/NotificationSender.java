package com.pedrocosta.exchangelog.notification.sender;

import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.utils.AppProperties;

public abstract class NotificationSender {

    private Notification notification;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public abstract void execute();

    public void send() {
        if (Boolean.parseBoolean(AppProperties.get("send.notification"))) {
            execute();
        }
    }
}
