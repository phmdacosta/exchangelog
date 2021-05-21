package com.pedrocosta.exchangelog.models;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class NotificationMessage {

    private NotificationRequest notificationRequest;
    private String message;

    public NotificationMessage() {
        this(null);
    }

    public NotificationMessage(NotificationRequest notificationRequest) {
        this(notificationRequest, null);
    }

    public NotificationMessage(NotificationRequest notificationRequest, String message) {
        this.notificationRequest = notificationRequest;
        this.message = message;
    }

    public NotificationRequest getNotificationRequest() {
        return notificationRequest;
    }

    public NotificationMessage setNotificationRequest(NotificationRequest notificationRequest) {
        this.notificationRequest = notificationRequest;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public NotificationMessage setMessage(String message) {
        this.message = message;
        return this;
    }
}
