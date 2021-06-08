package com.pedrocosta.exchangelog.utils;

import com.sun.istack.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class AppProperties {

    private static Properties properties;

    @Nullable
    public static String get(String key) {
        if (properties == null) {
            properties = getProperties();
        }
        return properties.getProperty(key);
    }

    private static Properties getProperties() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
