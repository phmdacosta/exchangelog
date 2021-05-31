package com.pedrocosta.exchangelog.utils.notifications;

public abstract class NotificationSender {

    private final NotificationMeans means;
    private String from;
    private String to;
    private String subject;
    private String body;

    public NotificationSender(NotificationMeans means) {
        this.means = means;
    }

    public NotificationMeans getMeans() {
        return means;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public abstract void send();
}
