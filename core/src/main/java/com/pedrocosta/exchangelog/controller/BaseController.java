package com.pedrocosta.exchangelog.controller;

import com.pedrocosta.exchangelog.ServiceFactory;
import com.pedrocosta.exchangelog.api.RestResponse;
import com.pedrocosta.exchangelog.utils.JsonUtils;
import com.pedrocosta.springutils.output.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Base controller.
 * @author Pedro H. M . da Costa
 * @since 1.0
 */
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

    protected String toJson(RestResponse<?> response) {
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
