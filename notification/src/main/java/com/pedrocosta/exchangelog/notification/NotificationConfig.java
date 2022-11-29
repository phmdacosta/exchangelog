package com.pedrocosta.exchangelog.notification;

import com.pedrocosta.exchangelog.BaseConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:routes.properties")
@Import({BaseConfig.class})
public class NotificationConfig {
}
