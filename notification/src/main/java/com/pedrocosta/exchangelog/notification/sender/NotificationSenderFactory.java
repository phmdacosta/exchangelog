package com.pedrocosta.exchangelog.notification.sender;

import com.pedrocosta.exchangelog.CoreService;
import com.pedrocosta.exchangelog.notification.Mean;
import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.springutils.ClassFinder;
import com.pedrocosta.springutils.PackageUtils;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    public NotificationSender create(@NotNull final ApplicationContext context,
                                            @NotNull final Mean mean) {
        try {
            List<Class<?>> senderClasses = ClassFinder.findAllByAssignable(context, NotificationSender.class);
            Class<?> senderClass = senderClasses.stream().filter(
                    clazz -> clazz.getSimpleName().startsWith(mean.capitalizeName()))
                    .findFirst().orElse(null);
            if (senderClass != null) {
                return (NotificationSender) context.getBean(senderClass);
            }
        } catch (ClassNotFoundException e) {
            Log.error(this, e);
        }

        return null;
    }
}
