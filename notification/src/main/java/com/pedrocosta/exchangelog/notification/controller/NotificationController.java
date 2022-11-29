package com.pedrocosta.exchangelog.notification.controller;

import com.pedrocosta.exchangelog.ServiceFactory;
import com.pedrocosta.exchangelog.annotation.View;
import com.pedrocosta.exchangelog.api.RestResponse;
import com.pedrocosta.exchangelog.api.response.RestResponseEntity;
import com.pedrocosta.exchangelog.controller.BaseController;
import com.pedrocosta.exchangelog.api.exceptions.RestApiException;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.notification.Mean;
import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.exchangelog.notification.dto.NotificationDto;
import com.pedrocosta.exchangelog.notification.service.NotificationService;
import com.pedrocosta.exchangelog.utils.JsonUtils;
import com.pedrocosta.exchangelog.web.validation.WebValidator;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import com.pedrocosta.springutils.viewmapper.ViewMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class NotificationController extends BaseController {

    private final NotificationService notificationService;
    private final ViewMapper mapper;

    public NotificationController(ServiceFactory serviceFactory, JsonUtils jsonUtils, ViewMapper mapper) {
        super(serviceFactory, jsonUtils);
        this.notificationService = serviceFactory.create(NotificationService.class);
        this.mapper = mapper;
    }

    @GetMapping(value = "${route.notification.get.means}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMeans() {
        List<String> meanNames = new ArrayList<>(Mean.values().length);
        for (Mean mean : Mean.values()) {
            meanNames.add(mean.name());
        }
        return RestResponseEntity.ok(meanNames);
    }

    @PostMapping(value = "${route.notification.push}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> push(@RequestBody NotificationDto notificationDto) throws RestApiException {
        if (!WebValidator.acceptParameters(notificationDto)) {
            return RestResponseEntity.badRequest(Messages.get("request.body.bad.format"));
        }

        ResponseEntity<?> response;

        try {
            Notification notification = mapper.map(notificationDto, Notification.class);
            notification = notificationService.saveAndNotify(notification);
            response = RestResponseEntity.ok(notification);
        } catch (SaveDataException e) {
            Log.error(this, e);
            response = RestResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return response;
    }

    @SuppressWarnings("unchecked")
    @PostMapping(value = "${route.notification.push.all}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> pushAll(@RequestBody List<NotificationDto> notificationDtoList) {
        if (!WebValidator.acceptParameters(notificationDtoList)) {
            return RestResponseEntity.badRequest(Messages.get("request.body.bad.format"));
        }

        ResponseEntity<?> response; //TODO: success msg

        try {
            //TODO: fix mapping with collection
            List<Notification> notifications = (List<Notification>) mapper.map(notificationDtoList, Notification.class);
            notifications = notificationService.saveAndNotifyAll(notifications);
            response = RestResponseEntity.ok(notifications);
        } catch (SaveDataException e) {
            Log.error(this, e);
            List<Notification> notSaved = (List<Notification>) e.getData();
            response = RestResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return response;
    }
}
