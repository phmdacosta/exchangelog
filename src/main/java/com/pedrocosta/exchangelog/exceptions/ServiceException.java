package com.pedrocosta.exchangelog.exceptions;

import com.pedrocosta.exchangelog.services.CoreService;

public class ServiceException extends Exception {

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(CoreService service, String message) {
        super(service.getClass().getSimpleName() + ": " + message);
    }

    public ServiceException(Class<CoreService> serviceClass, String message) {
        super(serviceClass.getSimpleName() + ": " + message);
    }
}
