package com.pedrocosta.exchangelog.controller;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.QuoteNotificationRequest;
import com.pedrocosta.exchangelog.models.User;
import com.pedrocosta.exchangelog.services.QuoteNotificationRequestService;
import com.pedrocosta.exchangelog.services.ServiceFactory;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.utils.GsonUtils;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.InvalidParameterException;
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

        ServiceResponse<QuoteNotificationRequest> response =
                ServiceResponse.createSuccess(HttpStatus.CREATED);

        try {
            QuoteNotificationRequest quoteNotificationRequest =
                    gsonUtils.fromJson(notReqJson, QuoteNotificationRequest.class);

            QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                    serviceFactory.create(QuoteNotificationRequest.class);
            response.setObject(service.save(quoteNotificationRequest));

        } catch (NullPointerException | InvalidParameterException e) {
            response = ServiceResponse.<QuoteNotificationRequest>createError(
                    HttpStatus.INTERNAL_SERVER_ERROR).setException(e);
        } catch (SaveDataException e) {
            response = ServiceResponse.<QuoteNotificationRequest>createError(
                    HttpStatus.BAD_REQUEST).setException(e);
        } finally {
            if (!response.isSuccess()) {
                Log.error(this, response.getException());
                response.setMessage(Messages.get(
                        "error.not.saved", "quote notification"));
            }
        }

        return gsonUtils.toJson(response);
    }

    @RequestMapping(value = "/quoteNotifRequest/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQuoteNotification(@RequestParam(name = "id") long id,
                                       @RequestParam(name = "name") String name) {

        ServiceResponse<QuoteNotificationRequest> response = ServiceResponse
                .createSuccess(HttpStatus.OK);
        QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                serviceFactory.create(QuoteNotificationRequest.class);

        if (id > 0) {
            response.setObject(service.find(id));
            if (response.getObject() == null) {
                response = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                        Messages.get("error.notif.req.not.found", String.valueOf(id)));
            }
        }
        else if (name != null && !name.isBlank()) {
            response.setObject(service.find(name));
            if (response.getObject() == null) {
                response = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                        Messages.get("error.notif.req.not.found", name));
            }
        }
        else {
            response = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
                    Messages.get("could.not.find", "notification request"));
        }

        return gsonUtils.toJson(response);
    }

    @RequestMapping(value = "/quoteNotifRequest/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQuoteNotificationByLogicalOperator(
            @RequestParam(name = "logicalOperator") String logicalOperator) {

        ServiceResponse<List<QuoteNotificationRequest>> response = ServiceResponse
                .createSuccess(HttpStatus.OK);
        QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                serviceFactory.create(QuoteNotificationRequest.class);

        if (logicalOperator == null || logicalOperator.isBlank()) {
            response = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
                    Messages.get("could.not.find", "notification request"));
            return gsonUtils.toJson(response);
        }

        response.setObject(service.findAllByLogicalOperator(logicalOperator));
        if (response.getObject() == null) {
            response = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                    Messages.get("error.notif.req.not.found", logicalOperator));
        }

        return gsonUtils.toJson(response);
    }

    @RequestMapping(value = "/quoteNotifRequest/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllQuoteNotifications(@RequestBody String userJson) {
        Log.info(this, "Body: " + userJson);

        if (userJson == null || userJson.isBlank()) {
            return getMissingBodyResponseError();
        }

        ServiceResponse<List<QuoteNotificationRequest>> response =
                ServiceResponse.createSuccess();

        QuoteNotificationRequestService service = (QuoteNotificationRequestService)
                serviceFactory.create(QuoteNotificationRequest.class);

        try {
            User user = gsonUtils.fromJson(userJson, User.class);
            response.setObject(service.findAll(user));

            if (response.getObject().isEmpty())
                response = ServiceResponse.createError(HttpStatus.NOT_FOUND);
        }
        catch (NullPointerException | InvalidParameterException e) {
            Log.error(this, e);
            response = ServiceResponse.<List<QuoteNotificationRequest>>createError(
                    HttpStatus.INTERNAL_SERVER_ERROR).setException(e);
        }
        finally {
            if (!response.isSuccess())
                response.setMessage(Messages.get(
                        "could.not.find", "quote notification"));
        }

        return gsonUtils.toJson(response);
    }

    private String getMissingBodyResponseError() {
        return gsonUtils.toJson(ServiceResponse.createError(
                HttpStatus.BAD_REQUEST,
                Messages.get("error.request.body")));
    }
}
