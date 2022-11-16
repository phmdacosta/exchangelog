package com.pedrocosta.exchangelog.api.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class BaseResponseBody {
    private LocalDateTime timestamp;
    private int status;
    private boolean success;

    public BaseResponseBody(HttpStatus status, boolean success) {
        this(status.value(), success);
    }

    public BaseResponseBody(int status, boolean success) {
        this(LocalDateTime.now(), status, success);
    }

    public BaseResponseBody(LocalDateTime timestamp, HttpStatus status, boolean success) {
        this(timestamp, status.value(), success);
    }

    public BaseResponseBody(LocalDateTime timestamp, int status, boolean success) {
        this.timestamp = timestamp;
        this.status = status;
        this.success = success;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setStatus(HttpStatus status) {
        this.status = status.value();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
