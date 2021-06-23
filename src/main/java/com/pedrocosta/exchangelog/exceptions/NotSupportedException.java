package com.pedrocosta.exchangelog.exceptions;

/**
 * Exception for not supported component.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class NotSupportedException extends RuntimeException {

    public NotSupportedException() {
        super();
    }

    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(Exception e) {
        super(e);
    }

    public NotSupportedException(String message, Exception e) {
        super(message, e);
    }
}
