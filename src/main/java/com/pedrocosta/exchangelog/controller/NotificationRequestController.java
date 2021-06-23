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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
@Controller
public class NotificationRequestController extends BaseController {

    private QuoteNotificationRequestService quoteNotifReqService;

    public NotificationRequestController(ServiceFactory serviceFactory, GsonUtils gsonUtils) {
        super(serviceFactory, gsonUtils);
        this.quoteNotifReqService = getServiceFactory()
                .create(QuoteNotificationRequestService.class);
    }

    @PostMapping(value = "/quoteNotifRequest/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveQuoteNotificationRequest(@RequestBody String notReqJson) {
        Log.info(this, "Body: " + notReqJson);

        if (notReqJson == null || notReqJson.isBlank()) {
            return getMissingBodyResponseErrorJson();
        }

        ServiceResponse<QuoteNotificationRequest> response =
                ServiceResponse.createSuccess(HttpStatus.CREATED);

        try {
            QuoteNotificationRequest quoteNotificationRequest =
                    fromJson(notReqJson, QuoteNotificationRequest.class);
            response.setObject(quoteNotifReqService.save(quoteNotificationRequest));

        } catch (NullPointerException | InvalidParameterException e) {
            Log.error(this, e);
            response = ServiceResponse.<QuoteNotificationRequest>createError(
                    HttpStatus.INTERNAL_SERVER_ERROR).setException(e);
        } catch (SaveDataException e) {
            Log.error(this, e);
            response = ServiceResponse.<QuoteNotificationRequest>createError(
                    HttpStatus.BAD_REQUEST).setException(e);
        } finally {
            if (!response.isSuccess()) {
                Log.error(this, response.getException());
                response.setMessage(Messages.get(
                        "error.not.saved", "quote notification"));
            }
        }

        return toJson(response);
    }

    @GetMapping(value = "/quoteNotifRequest/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQuoteNotification(@RequestParam(name = "id") long id,
                                       @RequestParam(name = "name") String name) {

        ServiceResponse<QuoteNotificationRequest> response = ServiceResponse
                .createSuccess(HttpStatus.OK);

        if (id > 0) {
            response.setObject(quoteNotifReqService.find(id));
            if (response.getObject() == null) {
                response = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                        Messages.get("error.notif.req.not.found", String.valueOf(id)));
            }
        }
        else if (name != null && !name.isBlank()) {
            response.setObject(quoteNotifReqService.find(name));
            if (response.getObject() == null) {
                response = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                        Messages.get("error.notif.req.not.found", name));
            }
        }
        else {
            response = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
                    Messages.get("could.not.find", "notification request"));
        }

        return toJson(response);
    }

    @GetMapping(value = "/quoteNotifRequest/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQuoteNotificationByLogicalOperator(
            @RequestParam(name = "logicalOperator") String logicalOperator) {

        ServiceResponse<List<QuoteNotificationRequest>> response = ServiceResponse
                .createSuccess(HttpStatus.OK);

        if (logicalOperator == null || logicalOperator.isBlank()) {
            response = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
                    Messages.get("could.not.find", "notification request"));
            return toJson(response);
        }

        response.setObject(quoteNotifReqService.findAllByLogicalOperator(logicalOperator));
        if (response.getObject() == null) {
            response = ServiceResponse.createError(HttpStatus.NOT_FOUND,
                    Messages.get("error.notif.req.not.found", logicalOperator));
        }

        return toJson(response);
    }

    @GetMapping(value = "/quoteNotifRequest/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllQuoteNotifications(@RequestBody String userJson) {
        Log.info(this, "Body: " + userJson);

        if (userJson == null || userJson.isBlank()) {
            return getMissingBodyResponseErrorJson();
        }

        ServiceResponse<List<QuoteNotificationRequest>> response =
                ServiceResponse.createSuccess();

        try {
            User user = fromJson(userJson, User.class);
            response.setObject(quoteNotifReqService.findAll(user));

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

        return toJson(response);
    }
}
