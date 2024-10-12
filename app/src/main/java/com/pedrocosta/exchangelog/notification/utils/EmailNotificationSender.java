package com.pedrocosta.exchangelog.notification.utils;

import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailNotificationSender extends NotificationSender {

    private final JavaMailSender mailSender;
    private final Environment environment;

    public EmailNotificationSender(JavaMailSender mailSender, Environment environment) {
        super(NotificationMeans.EMAIL);
        this.mailSender = mailSender;
        this.environment = environment;
    }

    @Override
    public void send() {
        SimpleMailMessage message = new SimpleMailMessage();
        if (getFrom() == null) {
            setFrom(environment.getProperty("mail.default.address"));
        }
        message.setFrom(getFrom());
        message.setTo(getTo());
        message.setSubject(getSubject());
        message.setText(getBody());
        mailSender.send(message);
    }
}
