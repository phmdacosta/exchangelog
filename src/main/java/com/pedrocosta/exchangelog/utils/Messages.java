package com.pedrocosta.exchangelog.utils;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Class that centralizes the retrieval of
 * system messages in the properties file.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class Messages {

    private static final String PATH = "label/messages";

    private static ResourceBundleMessageSource source;

    private static ResourceBundleMessageSource getSource() {
        if (source == null) {
            source = new ResourceBundleMessageSource();
            source.setBasenames(PATH);
            source.setUseCodeAsDefaultMessage(true);
        }
        return source;
    }

    /**
     * Get message by its key in properties file.
     *
     * @param key   Key name of message in properties file
     * @param args  Arguments to include in message
     * @return String with message
     */
    public String getMessage(String key, String ... args) {
        return getSource().getMessage(key, args, Defaults.LOCALE);
    }

    /**
     * Get message by its key in properties file.
     *
     * @param key   Key name of message in properties file
     * @param args  Arguments to include in message
     * @return String with message
     */
    public static synchronized String get(String key, String ... args) {
        return getSource().getMessage(key, args, Defaults.LOCALE);
    }
}
