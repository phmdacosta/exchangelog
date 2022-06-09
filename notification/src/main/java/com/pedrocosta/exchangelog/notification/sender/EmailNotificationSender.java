package com.pedrocosta.exchangelog.notification.sender;

import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender extends NotificationSender {

    private final JavaMailSender mailSender;
    private final Environment environment;

    public EmailNotificationSender(JavaMailSender mailSender, Environment environment) {
        this.mailSender = mailSender;
        this.environment = environment;
    }

    @Override
    public void execute() {
        SimpleMailMessage message = new SimpleMailMessage();
        if (getNotification().getFrom() == null) {
            getNotification().setFrom(environment.getProperty("mail.default.address"));
        }
        message.setFrom(getNotification().getFrom());
        message.setTo(getNotification().getTo().toArray(new String[0]));
        message.setSubject(getNotification().getSubject());
        message.setText(getNotification().getMessage());
        mailSender.send(message);
    }
}
