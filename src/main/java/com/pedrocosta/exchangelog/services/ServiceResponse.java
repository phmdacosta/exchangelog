package com.pedrocosta.exchangelog.services;

import com.google.gson.annotations.Expose;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class ServiceResponse<T> {

    private HttpStatus code;
    private String message;
    private T object;
    private Date execTime;
    private boolean success;

    @Expose(serialize = false)
    private Exception exception;

    private ServiceResponse() {
        this.setSuccess(true);
        this.setCode(HttpStatus.OK);
        this.setMessage("");
        this.setExecTime(new Date());
    }

    private ServiceResponse(HttpStatus code) {
        this();
        this.setCode(code);
    }

    /**
     * Create a a {@linkplain HttpStatus#OK OK}
     * success response with empty message.
     *
     * @param <T>   {@link ServiceResponse} type
     * @return  Success response
     */
    public static <T> ServiceResponse<T> createSuccess() {
        return createSuccess(HttpStatus.OK, "");
    }

    /**
     * Create a success response.
     *
     * @param code  {@link HttpStatus} code
     * @param <T>   {@link ServiceResponse} type
     * @return  Success response
     */
    public static <T> ServiceResponse<T> createSuccess(HttpStatus code) {
        return createSuccess(code, "");
    }

    /**
     * Create a success response.
     *
     * @param code      {@link HttpStatus} code
     * @param message   Success message
     * @param <T>       {@link ServiceResponse} type
     * @return  Success response
     */
    public static <T> ServiceResponse<T> createSuccess(HttpStatus code, String message) {
        return new ServiceResponse<T>(code).setMessage(message);
    }

    /**
     * Create a {@linkplain HttpStatus#BAD_REQUEST BAD_REQUEST}
     * error response with empty message.
     *
     * @param <T>   {@link ServiceResponse} type
     * @return  Error response
     */
    public static <T> ServiceResponse<T> createError() {
        return createError(HttpStatus.BAD_REQUEST, "");
    }

    /**
     * Create a error response with empty message.
     *
     * @param code  {@link HttpStatus} code
     * @param <T>   {@link ServiceResponse} type
     * @return  Error response
     */
    public static <T> ServiceResponse<T> createError(HttpStatus code) {
        return createError(code, "");
    }

    /**
     * Create a error response.
     *
     * @param code      {@link HttpStatus} code
     * @param message   Error message
     * @param <T>       {@link ServiceResponse} type
     * @return  Error response
     */
    public static <T> ServiceResponse<T> createError(HttpStatus code, String message) {
        return new ServiceResponse<T>(code)
                .setSuccess(false)
                .setMessage(message);
    }

    /**
     * Update http code and message with a new error object.
     *
     * @param error {@link ServiceResponse} error object with new info.
     *
     * @return
     *          {@link ServiceResponse} object with error info.
     *          If parameter is not a error, it will return previous object without changes.
     *          If parameter is {@code null}, it will return previous object without changes.
     */
    public ServiceResponse<T> fromError(ServiceResponse<?> error) {
        if (error == null) return this;
        if (error.isSuccess()) return this;
        this.setSuccess(false);
        setCode(error.getCode());
        setMessage(error.getMessage());
        setException(error.getException());
        setExecTime((Date) error.getExecTime().clone());
        return this;
    }

    public Exception getException() {
        return exception;
    }

    public ServiceResponse<T> setException(Exception ex) {
        this.exception = ex;
        return this;
    }

    public HttpStatus getCode() {
        return code;
    }

    public ServiceResponse<T> setCode(HttpStatus code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ServiceResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getObject() {
        return object;
    }

    public ServiceResponse<T> setObject(T object) {
        this.object = object;
        return this;
    }

    public Date getExecTime() {
        return execTime;
    }

    public ServiceResponse<T> setExecTime(Date execTime) {
        this.execTime = execTime;
        return this;
    }

    /**
     * Check if service request was successful.
     *
     * @return
     *          True if it is on of the next http codes:
     *              [{@linkplain HttpStatus#OK OK},
     *               {@linkplain HttpStatus#CREATED CREATED},
     *               {@linkplain HttpStatus#ACCEPTED ACCEPTED},
     *               {@linkplain HttpStatus#NON_AUTHORITATIVE_INFORMATION NON_AUTHORITATIVE_INFORMATION},
     *               {@linkplain HttpStatus#NO_CONTENT NO_CONTENT},
     *               {@linkplain HttpStatus#RESET_CONTENT RESET_CONTENT},
     *               {@linkplain HttpStatus#PARTIAL_CONTENT PARTIAL_CONTENT},
     *               {@linkplain HttpStatus#MULTI_STATUS MULTI_STATUS},
     *               {@linkplain HttpStatus#ALREADY_REPORTED ALREADY_REPORTED},
     *               {@linkplain HttpStatus#IM_USED IM_USED}],<br>
     *          false otherwise.
     */
    public boolean isSuccess() {
        return success;
//        switch (getCode()) {
//            case OK:
//            case CREATED:
//            case ACCEPTED:
//            case NON_AUTHORITATIVE_INFORMATION:
//            case NO_CONTENT:
//            case RESET_CONTENT:
//            case PARTIAL_CONTENT:
//            case MULTI_STATUS:
//            case ALREADY_REPORTED:
//            case IM_USED: return true;
//            default: return false;
//        }
    }

    public ServiceResponse<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
