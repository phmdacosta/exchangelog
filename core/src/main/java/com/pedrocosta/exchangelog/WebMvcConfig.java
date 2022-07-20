package com.pedrocosta.exchangelog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrocosta.exchangelog.web.processor.ViewMapperProcessor;
import com.pedrocosta.springutils.viewmapper.ViewMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;
    private final ViewMapper mapper;

    public WebMvcConfig(ApplicationContext applicationContext, ViewMapper mapper) {
        this.applicationContext = applicationContext;
        this.mapper = mapper;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().applicationContext(this.applicationContext).build();
        resolvers.add(new ViewMapperProcessor(objectMapper, this.mapper));
    }
}
