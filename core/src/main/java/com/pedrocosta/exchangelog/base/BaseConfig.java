package com.pedrocosta.exchangelog.base;

import com.pedrocosta.utils.jsonmanager.JsonUtils;
import com.pedrocosta.utils.jsonmanager.adapter.UtilsTypeAdapterFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BaseConfig {

    @Bean
    public UtilsTypeAdapterFactory typeAdapterFactory() {
        return new UtilsTypeAdapterFactory();
    }

    @Bean
    public JsonUtils jsonUtils(UtilsTypeAdapterFactory typeAdapterFactory) {
        return new JsonUtils()
                .setTypeAdapterFactory(typeAdapterFactory);
    }

    @Bean
    public ServiceFactory serviceFactory(ApplicationContext context, Environment env) {
        return new ServiceFactory(context, env);
    }
}
