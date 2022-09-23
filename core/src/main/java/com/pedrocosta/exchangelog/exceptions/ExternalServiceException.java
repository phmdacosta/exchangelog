package com.pedrocosta.exchangelog.exceptions;

/**
 * Exception for any error that happens in third party api requesters.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class ExternalServiceException extends RuntimeException {
    private static final long serialVersionUID = 5664883406782864426L;

    public ExternalServiceException() {
        super();
    }

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(Exception e) {
        super(e);
    }

    public ExternalServiceException(String message, Exception e) {
        super(message, e);
    }
}
