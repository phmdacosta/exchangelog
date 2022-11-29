package com.pedrocosta.exchangelog.notification.dto;

import com.pedrocosta.exchangelog.annotation.Required;
import com.pedrocosta.exchangelog.notification.attachment.dto.AttachmentDto;
import com.pedrocosta.springutils.viewmapper.annotation.View;
import java.util.Set;

@View
public class NotificationDto {
    @Required
    private String mean;
    @Required
    private String from;
    @Required
    private Set<String> to;
    @Required
    private String subject;
    private String message;
    private Set<AttachmentDto> attachments;

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

    public Set<AttachmentDto> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<AttachmentDto> attachments) {
        this.attachments = attachments;
    }
}
