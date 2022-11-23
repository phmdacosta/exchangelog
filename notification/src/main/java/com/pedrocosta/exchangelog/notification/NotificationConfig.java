package com.pedrocosta.exchangelog.notification;

import com.pedrocosta.exchangelog.BaseConfig;
import com.pedrocosta.exchangelog.batch.BatchSchedulerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BaseConfig.class, BatchSchedulerConfig.class})
public class NotificationConfig {
}
