package com.pedrocosta.exchangelog.controller;

import com.pedrocosta.exchangelog.services.ServiceFactory;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
@Controller
public class NotificationRequestController {

    private final ServiceFactory serviceFactory;
    private final GsonUtils gsonUtils;

    public NotificationRequestController(ServiceFactory serviceFactory, GsonUtils gsonUtils) {
        this.serviceFactory = serviceFactory;
        this.gsonUtils = gsonUtils;
    }

    @RequestMapping(value = "/notif-request/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public String save(@RequestBody String notReqJson) {
        return null;
    }
}
