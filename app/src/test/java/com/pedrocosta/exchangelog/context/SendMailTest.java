package com.pedrocosta.exchangelog.context;

import com.pedrocosta.exchangelog.ExchangelogApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
@SpringBootTest(classes = ExchangelogApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
public class SendMailTest {

    @Autowired
    private JavaMailSender emailSender;

    @Test
    public void sendSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("exchlog-test@gmail.com");
        message.setTo("exchlog-test@gmail.com");
        message.setSubject("Mail Sender Test");
        message.setText("This is a e-mail sending test.");
        emailSender.send(message);
    }
}
