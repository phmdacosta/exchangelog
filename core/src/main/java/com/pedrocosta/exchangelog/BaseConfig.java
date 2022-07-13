package com.pedrocosta.exchangelog;

import com.pedrocosta.exchangelog.api.RestResponse;
import com.pedrocosta.exchangelog.utils.JsonUtils;
import com.pedrocosta.springutils.jsonmanager.adapter.AdapterFinder;
import com.pedrocosta.springutils.jsonmanager.adapter.UtilsTypeAdapter;
import com.pedrocosta.springutils.jsonmanager.adapter.UtilsTypeAdapterFactory;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.MessageLocaleResolver;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Base configuration for all common configs.
 *
 * @author Pedro H. M . da Costa
 * @since 1.0
 */
@Configuration
public class BaseConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new MessageLocaleResolver();
    }

    @Bean
    public <T> UtilsTypeAdapterFactory typeAdapterFactory() {
        UtilsTypeAdapterFactory factory = new UtilsTypeAdapterFactory();

        List<UtilsTypeAdapter<T>> initAdapters = new ArrayList<>();
        try {
            initAdapters.addAll(AdapterFinder.findAllAnnotated(this.getClass().getPackageName()));
        } catch (Exception e) {
            Log.error(this, e);
        }

        for (UtilsTypeAdapter<T> initAdapter : initAdapters) {
            factory.addAdapterName(initAdapter.getTypeClass(),
                    initAdapter.getClass().getName());
        }
        return factory;
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

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }
}
