package com.pedrocosta.exchangelog.api.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * {
 *     "timestamp": "2022-11-15T17:59:21.245+00:00",
 *     "status": 500,
 *     "error": "Internal Server Error",
 *     "message": "",
 *     "path": "/api/registration"
 * }
 */
public class ErrorResponseBody extends BaseResponseBody {
    private String error;
    private String message;

    public ErrorResponseBody() {
        this(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR, "");
    }

    public ErrorResponseBody(HttpStatus status) {
        this(status, "");
    }

    public ErrorResponseBody(HttpStatus status, String message) {
        this(LocalDateTime.now(), status, message);
    }

    public ErrorResponseBody(LocalDateTime timestamp, HttpStatus status, String message) {
        super(timestamp, status.value(), false);
        this.error = status.getReasonPhrase();
        this.message = message;
    }

    public ErrorResponseBody(LocalDateTime timestamp, int status, String message) {
        super(timestamp, status, false);
        this.error = HttpStatus.valueOf(status).getReasonPhrase();
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
