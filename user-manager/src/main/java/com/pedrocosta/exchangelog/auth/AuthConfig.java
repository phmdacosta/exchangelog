package com.pedrocosta.exchangelog.auth;

import com.pedrocosta.exchangelog.BaseConfig;
import com.pedrocosta.utils.mailsender.Email;
import com.pedrocosta.utils.mailsender.MimeEmailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class AuthConfig extends BaseConfig {
//    @Bean
//    public MimeEmailSender mimeEmailSender(JavaMailSender javaMailSender, Environment environment) {
//        Email email = new Email();
//        email.setFrom(environment.getProperty("mail.default.address"));
//        return new MimeEmailSender(javaMailSender, email);
//    }
}
