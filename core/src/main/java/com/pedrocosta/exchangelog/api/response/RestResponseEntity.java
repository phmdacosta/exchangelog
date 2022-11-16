package com.pedrocosta.exchangelog.api.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestResponseEntity {
    public static <T> ResponseEntity<SuccessResponseBody<T>> ok(T body) {
        return success(HttpStatus.OK, body);
    }

    public static ResponseEntity<ErrorResponseBody> notFound(String message) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return error(httpStatus, message);
    }

    public static ResponseEntity<ErrorResponseBody> badRequest(String message) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return error(httpStatus, message);
    }

    public static ResponseEntity<ErrorResponseBody> error(HttpStatus status, String message) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody(status, message);
        return ResponseEntity.status(status).body(errorResponseBody);
    }

    public static <T> ResponseEntity<SuccessResponseBody<T>> success(HttpStatus status, T body) {
        SuccessResponseBody<T> successResponseBody = new SuccessResponseBody<>(status, body);
        return ResponseEntity.status(status).body(successResponseBody);
    }

    public static <T> ResponseEntity<T> build(HttpStatus status, T body) {
        return ResponseEntity.status(status).body(body);
    }
}
