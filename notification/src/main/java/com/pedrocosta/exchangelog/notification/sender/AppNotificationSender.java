package com.pedrocosta.exchangelog.notification.sender;

import com.pedrocosta.utils.output.Log;
import org.springframework.stereotype.Component;

@Component
public class AppNotificationSender extends NotificationSender {

    @Override
    public void execute() {
        Log.info(this, "Push notification to App");
        // TODO Implement app notification send
    }
}
