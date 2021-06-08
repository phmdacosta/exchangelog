package com.pedrocosta.exchangelog.controller;

import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.services.QuoteNotificationRequestService;
import com.pedrocosta.exchangelog.services.ServiceFactory;
import com.pedrocosta.exchangelog.services.ServiceResponse;
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

    @RequestMapping(value = "/quote-notif-request/new/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveQuoteNotificationRequest(@RequestBody String notReqJson) {
        QuoteNotificationRequest quoteNotificationRequest =
                gsonUtils.fromJson(notReqJson, QuoteNotificationRequest.class);
        QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                serviceFactory.create(QuoteNotificationRequest.class);
        ServiceResponse<QuoteNotificationRequest> response = service.save(quoteNotificationRequest);
        return gsonUtils.toJson(response);
    }
}
