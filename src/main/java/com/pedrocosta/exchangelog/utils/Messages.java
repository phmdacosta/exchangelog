package com.pedrocosta.exchangelog.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Class that centralizes the retrieval of
 * system messages in the properties file.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class Messages {

    public static final String PATH = "label/messages";

    private MessageSource source;

    public Messages() {
        init();
    }

    private void init() {
        var source = new ResourceBundleMessageSource();
        source.setBasenames(PATH);
        source.setUseCodeAsDefaultMessage(true);
        this.setSource(source);
    }

    private Messages setSource(MessageSource source) {
        this.source = source;
        return this;
    }

    /**
     * Get message by its key in properties file.
     *
     * @param key   Key name of message in properties file
     * @param args  Arguments to include in message
     * @return String with message
     */
    public String getMessage(String key, String ... args) {
        return source.getMessage(key, args, Defaults.LOCALE);
    }

    /**
     * Get message by its key in properties file.
     *
     * @param key   Key name of message in properties file
     * @param args  Arguments to include in message
     * @return String with message
     */
    public static String get(String key, String ... args) {
        return new Messages().getMessage(key, args);
    }
}
