package com.pedrocosta.exchangelog.exceptions;

import com.pedrocosta.exchangelog.ServiceResponse;
import org.springframework.http.HttpStatus;

public class RestApiException extends RuntimeException {

    private ServiceResponse<Object> responseMessage;

    public RestApiException(HttpStatus httpStatus, String message) {
        super(message);
        setResponseMessage(ServiceResponse.createError(httpStatus, message));
    }

    public RestApiException(HttpStatus httpStatus, Object obj) {
        super("");
        setResponseMessage(ServiceResponse.createError(httpStatus));
        getResponseMessage().setObject(obj);
    }

    public RestApiException(ServiceResponse<Object> serviceResponse) {
        super(serviceResponse.getMessage());
        setResponseMessage(serviceResponse);
    }

    public ServiceResponse<Object> getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ServiceResponse<Object> responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String getMessage() {
        return this.responseMessage.getMessage();
    }
}
