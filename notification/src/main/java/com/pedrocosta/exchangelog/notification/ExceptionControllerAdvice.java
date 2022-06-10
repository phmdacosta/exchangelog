package com.pedrocosta.exchangelog.notification;

import com.pedrocosta.exchangelog.exceptions.BaseControllerAdvice;
import com.pedrocosta.utils.jsonmanager.JsonUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice extends BaseControllerAdvice {
    public ExceptionControllerAdvice(JsonUtils jsonUtils) {
        super(jsonUtils);
    }
}
