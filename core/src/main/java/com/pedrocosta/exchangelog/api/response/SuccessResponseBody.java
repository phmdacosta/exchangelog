package com.pedrocosta.exchangelog.api.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class SuccessResponseBody<T> extends BaseResponseBody {
    private T data;

    public SuccessResponseBody(HttpStatus status) {
        super(status, true);
    }

    public SuccessResponseBody(int status) {
        super(status, true);
    }

    public SuccessResponseBody(T data) {
        this(HttpStatus.OK, data);
    }

    public SuccessResponseBody(HttpStatus status, T data) {
        this(status.value(), data);
    }

    public SuccessResponseBody(int status, T data) {
        super(status, true);
        setData(data);
    }

    public SuccessResponseBody(LocalDateTime timestamp, T data) {
        this(timestamp, HttpStatus.OK, data);
    }

    public SuccessResponseBody(LocalDateTime timestamp, HttpStatus status, T data) {
        this(timestamp, status.value(), data);
    }

    public SuccessResponseBody(LocalDateTime timestamp, int status, T data) {
        super(timestamp, status, true);
        setData(data);
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
