package com.pedrocosta.exchangelog.notification;

import com.pedrocosta.exchangelog.base.ServiceFactory;
import com.pedrocosta.exchangelog.base.ServiceResponse;
import com.pedrocosta.exchangelog.base.controller.BaseController;
import com.pedrocosta.exchangelog.base.exceptions.BaseControllerAdvice;
import com.pedrocosta.exchangelog.base.exceptions.RestApiException;
import com.pedrocosta.exchangelog.base.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.notification.persistence.NotificationService;
import com.pedrocosta.exchangelog.notification.sender.NotificationSender;
import com.pedrocosta.exchangelog.notification.sender.NotificationSenderFactory;
import com.pedrocosta.utils.jsonmanager.JsonUtils;
import com.pedrocosta.utils.output.Log;
import com.pedrocosta.utils.output.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class NotificationController extends BaseController {

    private final NotificationService notificationService;
    private final NotificationSenderFactory notificationSenderFactory;

    public NotificationController(ServiceFactory serviceFactory,
                                  JsonUtils jsonUtils,
                                  NotificationSenderFactory notificationSenderFactory, BaseControllerAdvice ca) {
        super(serviceFactory, jsonUtils);
        this.notificationService = serviceFactory.create(NotificationService.class);
        this.notificationSenderFactory = notificationSenderFactory;
    }

    @GetMapping(value = "/notificationMeans", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getNotificationMeans() {
        List<String> meanNames = new ArrayList<>(Mean.values().length);
        for (Mean mean : Mean.values()) {
            meanNames.add(mean.name());
        }
        return toJson(ServiceResponse.<List<String>>createSuccess().setObject(meanNames));
    }

    @PostMapping(value = "/pushNotification",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String pushNotification(@RequestBody String json) throws RestApiException {
        ServiceResponse<Object> response = ServiceResponse.createSuccess();
        Notification notification;
        try {
            notification = fromJson(json, Notification.class);
        } catch (Exception e) {
            Log.error(this, e);
            throw new RestApiException(HttpStatus.BAD_REQUEST,
                    Messages.get("request.body.bad.format"));
        }

        try {
            Notification saved = notificationService.save(notification);
            //Try to send
            NotificationSender sender = notificationSenderFactory.create(
                    Mean.get(notification.getMean()));
            sender.setNotification(saved);
            sender.send();
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
        ServiceResponse<Object> response = ServiceResponse.createSuccess();
        List<Notification> notifications;
        try {
            notifications = Arrays.asList(fromJson(json, Notification[].class));
        } catch (Exception e) {
            Log.error(this, e);
            throw new RestApiException(HttpStatus.BAD_REQUEST,
                    Messages.get("request.body.bad.format"));
        }

        try {
            List<Notification> saved = notificationService.saveAll(notifications);
            //Try to send
            for (Notification toSend : saved) {
                NotificationSender sender = notificationSenderFactory.create(
                        Mean.valueOf(toSend.getMean()));
                sender.setNotification(toSend);
                sender.send();
            }
        } catch (SaveDataException e) {
            Log.error(this, e);
            List<Notification> notSaved = (List<Notification>) e.getData();
            response = ServiceResponse.createError(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage());
            response.setObject(notSaved);
            throw new RestApiException(response);
        }

        return toJson(response);
    }
}
