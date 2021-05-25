package com.pedrocosta.exchangelog;

import java.time.Duration;

import com.pedrocosta.exchangelog.utils.MessageProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder
				.setConnectTimeout(Duration.ofMillis(3000))
				.setReadTimeout(Duration.ofMillis(3000))
				.build();
	}

	@Bean
	public static MessageProperties messageProperties() {
		var source = new ResourceBundleMessageSource();
		source.setBasenames("label/messages");
		source.setUseCodeAsDefaultMessage(true);
		MessageProperties properties = new MessageProperties();
		properties.setSource(source);
		return properties;
	}
}
