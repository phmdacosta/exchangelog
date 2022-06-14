package com.pedrocosta.exchangelog.notification.sender;

import com.pedrocosta.exchangelog.notification.Notification;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender implements NotificationSender {

    private final JavaMailSender mailSender;
    private final Environment environment;

    public EmailNotificationSender(JavaMailSender mailSender, Environment environment) {
        this.mailSender = mailSender;
        this.environment = environment;
    }

    @Override
    public void send(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        if (notification.getFrom() == null) {
            notification.setFrom(environment.getProperty("mail.default.address"));
        }
        message.setFrom(notification.getFrom());
        message.setTo(notification.getTo().toArray(new String[0]));
        message.setSubject(notification.getSubject());
        message.setText(notification.getMessage());
        mailSender.send(message);
    }
}
