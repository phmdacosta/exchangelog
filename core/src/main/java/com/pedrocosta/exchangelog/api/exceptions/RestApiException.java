package com.pedrocosta.exchangelog.api.exceptions;

import com.pedrocosta.exchangelog.api.RestResponse;
import org.springframework.http.HttpStatus;

/**
 * Exception for rest api errors.
 * <br>
 * It will be caught by {@linkplain com.pedrocosta.exchangelog.controller.BaseControllerAdvice BaseControllerAdvice}
 * that generates a JSON response.
 *
 * @author Pedro H. M . da Costa
 * @since 1.0
 */
public class RestApiException extends RuntimeException {

    private RestResponse<Object> responseMessage;

    public RestApiException(HttpStatus httpStatus, String message) {
        super(message);
        setResponseMessage(RestResponse.createError(httpStatus, message));
    }

    public RestApiException(HttpStatus httpStatus, Object obj) {
        super("");
        setResponseMessage(RestResponse.createError(httpStatus));
        getResponseMessage().setObject(obj);
    }

    public RestApiException(RestResponse<Object> restResponse) {
        super(restResponse.getMessage());
        setResponseMessage(restResponse);
    }

    public RestResponse<Object> getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(RestResponse<Object> responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String getMessage() {
        return this.responseMessage.getMessage();
    }
}
