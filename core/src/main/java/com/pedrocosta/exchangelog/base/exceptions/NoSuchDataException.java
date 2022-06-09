package com.pedrocosta.exchangelog.base.exceptions;

/**
 * Exception for not found database data.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class NoSuchDataException extends Exception {
    private static final long serialVersionUID = 3345031936963014261L;

    public NoSuchDataException() {
        super();
    }

    public NoSuchDataException(String message) {
        super(message);
    }

    public NoSuchDataException(Exception e) {
        super(e);
    }

    public NoSuchDataException(String message, Exception e) {
        super(message, e);
    }
}
