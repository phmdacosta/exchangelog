package com.pedrocosta.exchangelog.utils.notifications;

import com.pedrocosta.exchangelog.utils.Log;

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
