package com.pedrocosta.exchangelog.notification.sender;

import com.pedrocosta.exchangelog.notification.Mean;
import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.springutils.output.Log;
import com.sun.istack.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NotificationSenderFactory {

    private static final String SUFFIX = "NotificationSender";

    private final ApplicationContext context;

    public NotificationSenderFactory(ApplicationContext context) {
        this.context = context;
    }

    public NotificationSender create(@NotNull Notification notification) {
        Mean mean = Mean.get(notification.getMean());
        if (mean == null) {
            return null;
        }
        return create(mean);
    }

    public NotificationSender create(@NotNull Mean mean) {
        return create(context, mean);
    }

    public static NotificationSender create(@NotNull ApplicationContext context,
                                            @NotNull Mean mean) {
        String className = mean.capitalizeName().concat(SUFFIX);
        try {
            Class<?> senderClass =  Class.forName(NotificationSenderFactory.class.getPackageName() + "." + className);
            return (NotificationSender) context.getBean(senderClass);
        } catch (ClassNotFoundException e) {
            Log.error(context, e);
        }
        return null;
    }
}
