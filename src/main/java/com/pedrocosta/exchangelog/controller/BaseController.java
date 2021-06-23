package com.pedrocosta.exchangelog.controller;

import com.pedrocosta.exchangelog.services.CoreService;
import com.pedrocosta.exchangelog.services.ServiceFactory;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

    private final ServiceFactory serviceFactory;
    private final GsonUtils gsonUtils;

    public BaseController(ServiceFactory serviceFactory, GsonUtils gsonUtils) {
        this.serviceFactory = serviceFactory;
        this.gsonUtils = gsonUtils;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    protected ServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    protected String toJson(ServiceResponse<?> response) {
        String json = gsonUtils.toJson(response);
        Log.info(this, "Generated: " + json);
        return json;
    }

    protected <T> T fromJson(String json, Class<T> tClass) {
        Log.info(this, "Loading: " + json);
        return gsonUtils.fromJson(json, tClass);
    }

    protected String getMissingBodyResponseErrorJson() {
        return toJson(ServiceResponse.createError(
                HttpStatus.BAD_REQUEST,
                Messages.get("error.request.body")));
    }

    protected String getWrongParamsResponseErrorJson() {
        return toJson(ServiceResponse.createError(
                HttpStatus.BAD_REQUEST,
                Messages.get("error.request.param")));
    }

    protected boolean isValidParameters(Object ... params) {
        for (Object p : params) {
            if (p == null)
                return false;
            if (p instanceof String && ((String) p).isBlank())
                return false;
        }
        return true;
    }
}
