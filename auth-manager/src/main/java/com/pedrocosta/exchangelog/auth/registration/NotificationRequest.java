package com.pedrocosta.exchangelog.auth.registration;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NotificationRequest implements Serializable {
    private String mean;
    private String from;
    private Set<String> to;
    private String subject;
    private String body;

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Set<String> getTo() {
        return to;
    }

    public void setTo(Set<String> to) {
        this.to = to;
    }

    public void addTo(String ... to) {
        if (this.to == null) {
            this.to = new HashSet<>();
        }
        this.to.addAll(Arrays.asList(to));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationRequest)) return false;
        NotificationRequest that = (NotificationRequest) o;
        return Objects.equals(getMean(), that.getMean()) && Objects.equals(getFrom(), that.getFrom()) && Objects.equals(getTo(), that.getTo()) && Objects.equals(getSubject(), that.getSubject()) && Objects.equals(getBody(), that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMean(), getFrom(), getTo(), getSubject(), getBody());
    }

    @Override
    public String toString() {
        return "ConfirmationMailView{" +
                "mean='" + mean + '\'' +
                ", from='" + from + '\'' +
                ", to=" + to +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
