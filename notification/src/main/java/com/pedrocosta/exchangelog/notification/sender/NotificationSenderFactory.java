package com.pedrocosta.exchangelog.notification.sender;

import com.pedrocosta.exchangelog.CoreService;
import com.pedrocosta.exchangelog.notification.Mean;
import com.pedrocosta.exchangelog.notification.Notification;
import com.pedrocosta.springutils.PackageUtils;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
import com.sun.istack.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

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
        String className = mean.capitalizeName().concat(SUFFIX);

        try {
            // Look for all subpackages into service
            Package projectPackage = PackageUtils.getProjectPackage(context);
            List<Package> subPackages = PackageUtils.getSubPackages(projectPackage.getName());

            for (Package pack : subPackages) {
                Class<?> senderClass = Class.forName(pack.getName() + "." + className);
                return (NotificationSender) context.getBean(senderClass);
            }
        } catch (ClassNotFoundException e) {
            Log.error(this, e);
        }

        return null;
    }
}
