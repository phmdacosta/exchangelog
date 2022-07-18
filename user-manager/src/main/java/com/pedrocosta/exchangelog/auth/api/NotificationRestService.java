package com.pedrocosta.exchangelog.auth.api;

import com.pedrocosta.exchangelog.api.RestServiceRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationRestService extends RestServiceRequest<String> {

    public NotificationRestService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    public void load() {
        getMapProp().put(REQUEST_URL_REF, "http://localhost:8089/");
    }
}
