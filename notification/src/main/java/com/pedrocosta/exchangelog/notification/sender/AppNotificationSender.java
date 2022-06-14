package com.pedrocosta.exchangelog.notification.sender;

import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.springutils.output.Log;
import org.springframework.stereotype.Component;

@Component
public class AppNotificationSender implements NotificationSender {

    @Override
    public void send(Notification notification) {
        Log.info(this, "Push notification to App");
        // TODO Implement app notification send
    }
}
