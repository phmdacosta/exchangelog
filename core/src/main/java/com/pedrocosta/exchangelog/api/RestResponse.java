package com.pedrocosta.exchangelog.api;

import com.google.gson.annotations.Expose;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Response for REST API service controllers.
 * @param <T>   Type of object to return in rest service.
 *
 * @author Pedro H. M . da Costa
 * @since 1.0
 */
public class RestResponse<T> {

    private LocalDateTime timestamp;
    private HttpStatus code;
    private String message;
    private T object;
    private Date execTime;
    private boolean success;

    @Expose(serialize = false)
    private Exception exception;

    private RestResponse() {
        this.setTimestamp(LocalDateTime.now());
        this.setSuccess(true);
        this.setCode(HttpStatus.OK);
        this.setMessage("");
        this.setExecTime(new Date());
    }

    private RestResponse(HttpStatus code) {
        this();
        this.setCode(code);
    }

    /**
     * Create a a {@linkplain HttpStatus#OK OK}
     * success response with empty message.
     *
     * @param <T>   {@link RestResponse} type
     * @return  Success response
     */
    public static <T> RestResponse<T> createSuccess() {
        return createSuccess(HttpStatus.OK, "");
    }

    /**
     * Create a success response.
     *
     * @param code  {@link HttpStatus} code
     * @param <T>   {@link RestResponse} type
     * @return  Success response
     */
    public static <T> RestResponse<T> createSuccess(HttpStatus code) {
        return createSuccess(code, "");
    }

    /**
     * Create a success response.
     *
     * @param code      {@link HttpStatus} code
     * @param message   Success message
     * @param <T>       {@link RestResponse} type
     * @return  Success response
     */
    public static <T> RestResponse<T> createSuccess(HttpStatus code, String message) {
        return new RestResponse<T>(code).setMessage(message);
    }

    /**
     * Create a {@linkplain HttpStatus#BAD_REQUEST BAD_REQUEST}
     * error response with empty message.
     *
     * @param <T>   {@link RestResponse} type
     * @return  Error response
     */
    public static <T> RestResponse<T> createError() {
        return createError(HttpStatus.BAD_REQUEST, "");
    }

    /**
     * Create a error response with empty message.
     *
     * @param code  {@link HttpStatus} code
     * @param <T>   {@link RestResponse} type
     * @return  Error response
     */
    public static <T> RestResponse<T> createError(HttpStatus code) {
        return createError(code, "");
    }

    /**
     * Create a error response.
     *
     * @param code      {@link HttpStatus} code
     * @param message   Error message
     * @param <T>       {@link RestResponse} type
     * @return  Error response
     */
    public static <T> RestResponse<T> createError(HttpStatus code, String message) {
        return new RestResponse<T>(code)
                .setSuccess(false)
                .setMessage(message);
    }

    /**
     * Update http code and message with a new error object.
     *
     * @param error {@link RestResponse} error object with new info.
     *
     * @return
     *          {@link RestResponse} object with error info.
     *          If parameter is not a error, it will return previous object without changes.
     *          If parameter is {@code null}, it will return previous object without changes.
     */
    public RestResponse<T> fromError(RestResponse<?> error) {
        if (error == null) return this;
        if (error.isSuccess()) return this;
        this.setSuccess(false);
        setCode(error.getCode());
        setMessage(error.getMessage());
        setException(error.getException());
        setExecTime((Date) error.getExecTime().clone());
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Exception getException() {
        return exception;
    }

    public RestResponse<T> setException(Exception ex) {
        this.exception = ex;
        return this;
    }

    public HttpStatus getCode() {
        return code;
    }

    public RestResponse<T> setCode(HttpStatus code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RestResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getObject() {
        return object;
    }

    public RestResponse<T> setObject(T object) {
        this.object = object;
        return this;
    }

    public Date getExecTime() {
        return execTime;
    }

    public RestResponse<T> setExecTime(Date execTime) {
        this.execTime = execTime;
        return this;
    }

    /**
     * Check if service request was successful.
     */
    public boolean isSuccess() {
        return success;
    }

    public RestResponse<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
