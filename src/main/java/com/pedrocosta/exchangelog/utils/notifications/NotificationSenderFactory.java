package com.pedrocosta.exchangelog.utils.notifications;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class NotificationSenderFactory {

    private static final String SUFFIX = "NotificationSender";

    private ApplicationContext context;

    public NotificationSenderFactory(ApplicationContext context) {
        this.context = context;
    }
    public NotificationSender create(NotificationMeans means) {
        String className = StringUtils.capitalize(means.name()).concat(SUFFIX);
        return (NotificationSender) this.context.getBean(className);
    }

    public static NotificationSender create(ApplicationContext context,
                                            NotificationMeans means) {
        String className = StringUtils.capitalize(means.name()).concat(SUFFIX);
        return (NotificationSender) context.getBean(className);
    }
}
