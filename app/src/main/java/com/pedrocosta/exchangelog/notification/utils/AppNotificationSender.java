package com.pedrocosta.exchangelog.notification.utils;

import com.pedrocosta.springutils.output.Log;

public class AppNotificationSender extends NotificationSender {

    public AppNotificationSender() {
        super(NotificationMeans.APP);
    }

    @Override
    public void send() {
        Log.info(this, "Push notification to App");
        // TODO Implement app notification send
    }
}
