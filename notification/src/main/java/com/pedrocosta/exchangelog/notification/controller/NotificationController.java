package com.pedrocosta.exchangelog.notification.controller;

import com.pedrocosta.exchangelog.ServiceFactory;
import com.pedrocosta.exchangelog.api.RestResponse;
import com.pedrocosta.exchangelog.controller.BaseController;
import com.pedrocosta.exchangelog.api.exceptions.RestApiException;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.notification.Mean;
import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.exchangelog.notification.service.NotificationService;
import com.pedrocosta.exchangelog.utils.JsonUtils;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class NotificationController extends BaseController {

    private final NotificationService notificationService;

    public NotificationController(ServiceFactory serviceFactory, JsonUtils jsonUtils) {
        super(serviceFactory, jsonUtils);
        this.notificationService = serviceFactory.create(NotificationService.class);
    }

    @GetMapping(value = "/notificationMeans", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getNotificationMeans() {
        List<String> meanNames = new ArrayList<>(Mean.values().length);
        for (Mean mean : Mean.values()) {
            meanNames.add(mean.name());
        }
        return toJson(RestResponse.createSuccess().setObject(meanNames));
    }

    @PostMapping(value = "/pushNotification",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String pushNotification(@RequestBody String json) throws RestApiException {
        RestResponse<Object> response = RestResponse.createSuccess();
        Notification notification;
        try {
            notification = fromJson(json, Notification.class);
        } catch (Exception e) {
            Log.error(this, e);
            throw new RestApiException(HttpStatus.BAD_REQUEST,
                    Messages.get("request.body.bad.format"));
        }

        try {
            notificationService.saveAndNotify(notification);
        } catch (SaveDataException e) {
            Log.error(this, e);
            throw new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage());
        }

        return toJson(response);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "/pushNotifications",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String pushNotifications(@RequestBody String json) {
        RestResponse<Object> response = RestResponse.createSuccess();
        List<Notification> notifications;
        try {
            notifications = Arrays.asList(fromJson(json, Notification[].class));
        } catch (Exception e) {
            Log.error(this, e);
            throw new RestApiException(HttpStatus.BAD_REQUEST,
                    Messages.get("request.body.bad.format"));
        }

        try {
            notificationService.saveAndNotifyAll(notifications);
        } catch (SaveDataException e) {
            Log.error(this, e);
            List<Notification> notSaved = (List<Notification>) e.getData();
            response = RestResponse.createError(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage());
            response.setObject(notSaved);
            throw new RestApiException(response);
        }

        return toJson(response);
    }
}
