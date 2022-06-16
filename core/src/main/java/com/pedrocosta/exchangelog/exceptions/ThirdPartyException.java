package com.pedrocosta.exchangelog.exceptions;

/**
 * Exception for any error that happens in api requesters.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class ThirdPartyException extends RuntimeException {
    private static final long serialVersionUID = 5664883406782864426L;

    public ThirdPartyException() {
        super();
    }

    public ThirdPartyException(String message) {
        super(message);
    }

    public ThirdPartyException(Exception e) {
        super(e);
    }

    public ThirdPartyException(String message, Exception e) {
        super(message, e);
    }
}
