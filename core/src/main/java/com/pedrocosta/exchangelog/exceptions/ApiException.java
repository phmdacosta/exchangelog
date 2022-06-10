package com.pedrocosta.exchangelog.exceptions;

/**
 * Exception for any error that happens in api requesters.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 5664883406782864426L;

    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Exception e) {
        super(e);
    }

    public ApiException(String message, Exception e) {
        super(message, e);
    }
}
