package com.pedrocosta.exchangelog.notification;

import com.pedrocosta.exchangelog.controller.BaseControllerAdvice;
import com.pedrocosta.exchangelog.utils.JsonUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice extends BaseControllerAdvice {
    public ExceptionControllerAdvice(JsonUtils jsonUtils) {
        super(jsonUtils);
    }
}
