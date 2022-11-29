package com.pedrocosta.exchangelog.notification.attachment.dto;

import com.pedrocosta.exchangelog.annotation.Required;
import com.pedrocosta.springutils.viewmapper.annotation.View;

@View
public class AttachmentDto {
    @Required
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
