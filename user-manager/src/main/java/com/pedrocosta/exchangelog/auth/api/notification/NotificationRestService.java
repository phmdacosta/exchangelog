package com.pedrocosta.exchangelog.auth.api.notification;

import com.pedrocosta.exchangelog.api.GetServiceRequest;
import com.pedrocosta.exchangelog.api.PostServiceRequest;
import com.pedrocosta.exchangelog.api.RestServiceRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Class to connect to external notification service.
 */
@Service
public interface NotificationRestService extends GetServiceRequest<String>, PostServiceRequest<String> {

}
