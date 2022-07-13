package com.pedrocosta.exchangelog.notification;

import com.pedrocosta.exchangelog.notification.attachment.Attachment;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
public class Notification implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String mean;
    @Column(name = "sender_service")
    private String from;
    @ElementCollection
    @CollectionTable(name = "recipients", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "mail_address")
    private Set<String> to;
    private String subject;
    //TODO: body may be big, must be saved as BLOB
    private String message;
    @OneToMany(mappedBy = "notification",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Attachment> attachments;
    private boolean sent;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public Notification clone() throws CloneNotSupportedException {
        return (Notification) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return getId() == that.getId()
                && Objects.equals(getMean(), that.getMean())
                && Objects.equals(getFrom(), that.getFrom())
                && Objects.equals(getTo(), that.getTo())
                && Objects.equals(getSubject(), that.getSubject())
                && Objects.equals(getMessage(), that.getMessage())
                && Objects.equals(getAttachments(), that.getAttachments())
                && isSent() == that.isSent();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getMean(),
                getFrom(),
                getTo(),
                getSubject(),
                getMessage(),
                getAttachments(),
                isSent());
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", mean=" + mean +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", attachments=" + attachments +
                ", sent=" + sent +
                '}';
    }
}
