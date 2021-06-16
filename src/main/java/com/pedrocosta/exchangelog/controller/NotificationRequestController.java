package com.pedrocosta.exchangelog.controller;

import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.models.User;
import com.pedrocosta.exchangelog.services.QuoteNotificationRequestService;
import com.pedrocosta.exchangelog.services.ServiceFactory;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @RequestMapping(value = "/quoteNotifRequest/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveQuoteNotificationRequest(@RequestBody String notReqJson) {
        Log.info(this, "Body: " + notReqJson);

        if (notReqJson == null || notReqJson.isBlank()) {
            return getMissingBodyResponseError();
        }

        ServiceResponse<QuoteNotificationRequest> response;

        try {
            QuoteNotificationRequest quoteNotificationRequest =
                    gsonUtils.fromJson(notReqJson, QuoteNotificationRequest.class);

            QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                    serviceFactory.create(QuoteNotificationRequest.class);
            response = service.save(quoteNotificationRequest);
        } catch (Exception e) {
            Log.error(this, e);
            response = ServiceResponse.createError(HttpStatus.INTERNAL_SERVER_ERROR,
                    Messages.get("error.not.saved", "quote notification"));
        }

        return gsonUtils.toJson(response);
    }

    @RequestMapping(value = "/quoteNotifRequest/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQuoteNotification(@RequestParam long id, @RequestParam String name,
                                        @RequestParam String logicalOperator) {
        QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                serviceFactory.create(QuoteNotificationRequest.class);

        if (id > 0) {
            return gsonUtils.toJson(service.find(id));
        }

        return gsonUtils.toJson(
                logicalOperator != null && !logicalOperator.isBlank()
                        ? service.findAllByLogicalOperator(logicalOperator)
                        : service.find(name));
    }

    @RequestMapping(value = "/quoteNotifRequest/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllQuoteNotifications(@RequestBody String userJson) {
        Log.info(this, "Body: " + userJson);

        if (userJson == null || userJson.isBlank()) {
            return getMissingBodyResponseError();
        }

        QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                serviceFactory.create(QuoteNotificationRequest.class);

        ServiceResponse<List<QuoteNotificationRequest>> response;
        try {
            User user = gsonUtils.fromJson(userJson, User.class);
            response = service.findAll(user);
        } catch (Exception e) {
            Log.error(this, e);
            response = ServiceResponse.createError(HttpStatus.INTERNAL_SERVER_ERROR,
                    Messages.get("could.not.find", "quote notification"));
        }

        return gsonUtils.toJson(response);
    }

    private String getMissingBodyResponseError() {
        return gsonUtils.toJson(ServiceResponse.createError(
                HttpStatus.BAD_REQUEST,
                Messages.get("error.request.body")));
    }
}
