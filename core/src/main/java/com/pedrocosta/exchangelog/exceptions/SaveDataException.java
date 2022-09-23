package com.pedrocosta.exchangelog.exceptions;

/**
 * Exception for save error in database.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class SaveDataException extends RuntimeException {
    private static final long serialVersionUID = 3343478871248217220L;

    private Object data;

    public SaveDataException() {
        super();
    }

    public SaveDataException(String message) {
        super(message);
    }

    public SaveDataException(Exception e) {
        super(e);
    }

    public SaveDataException(String message, Exception e) {
        super(message, e);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
