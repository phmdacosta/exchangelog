package com.pedrocosta.exchangelog.notification.sender;

import com.pedrocosta.exchangelog.notification.Notification;

public interface NotificationSender {
    void send(Notification notification);
}
