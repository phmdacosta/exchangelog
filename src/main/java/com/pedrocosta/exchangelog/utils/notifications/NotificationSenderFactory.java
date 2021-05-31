package com.pedrocosta.exchangelog.utils.notifications;

import com.sun.istack.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class NotificationSenderFactory {

    private static final String SUFFIX = "NotificationSender";

    private final ApplicationContext context;

    public NotificationSenderFactory(ApplicationContext context) {
        this.context = context;
    }
    public NotificationSender create(@NotNull NotificationMeans means) {
        String className = StringUtils.capitalize(means.name()).concat(SUFFIX);
        return (NotificationSender) this.context.getBean(className);
    }

    public static NotificationSender create(@NotNull ApplicationContext context,
                                            @NotNull NotificationMeans means) {
        String className = StringUtils.capitalize(means.name()).concat(SUFFIX);
        return (NotificationSender) context.getBean(className);
    }
}
