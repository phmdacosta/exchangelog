package com.pedrocosta.exchangelog.controller;

import com.pedrocosta.exchangelog.ServiceFactory;
import com.pedrocosta.exchangelog.ServiceResponse;
import com.pedrocosta.utils.jsonmanager.JsonUtils;
import com.pedrocosta.utils.output.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

    private final ServiceFactory serviceFactory;
    private final JsonUtils jsonUtils;

    public BaseController(ServiceFactory serviceFactory, JsonUtils jsonUtils) {
        this.serviceFactory = serviceFactory;
        this.jsonUtils = jsonUtils;
    }

    @RequestMapping("/")
    public String index() {
        return "";
    }

    protected ServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    protected String toJson(ServiceResponse<?> response) {
        String json = jsonUtils.toJson(response);
        Log.info(this, "Generated: " + json);
        return json;
    }

    protected <T> T fromJson(String json, Class<T> tClass) {
        Log.info(this, "Loading: " + json);
        return jsonUtils.fromJson(json, tClass);
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
