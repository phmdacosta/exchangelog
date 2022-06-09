package com.pedrocosta.exchangelog.notification.sender;

import com.pedrocosta.exchangelog.notification.Mean;
import com.pedrocosta.utils.output.Log;
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
