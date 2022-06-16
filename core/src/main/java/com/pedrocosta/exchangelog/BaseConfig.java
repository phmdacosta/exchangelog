package com.pedrocosta.exchangelog;

import com.pedrocosta.exchangelog.utils.JsonUtils;
import com.pedrocosta.springutils.jsonmanager.adapter.AdapterFinder;
import com.pedrocosta.springutils.jsonmanager.adapter.UtilsTypeAdapter;
import com.pedrocosta.springutils.jsonmanager.adapter.UtilsTypeAdapterFactory;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.MessageLocaleResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;

import java.util.ArrayList;
import java.util.List;

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
}
