package com.pedrocosta.exchangelog.auth;

import com.pedrocosta.exchangelog.BaseConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:routes.properties")
public class AuthConfig extends BaseConfig {
//    @Bean
//    public MimeEmailSender mimeEmailSender(JavaMailSender javaMailSender, Environment environment) {
//        Email email = new Email();
//        email.setFrom(environment.getProperty("mail.default.address"));
//        return new MimeEmailSender(javaMailSender, email);
//    }
}
