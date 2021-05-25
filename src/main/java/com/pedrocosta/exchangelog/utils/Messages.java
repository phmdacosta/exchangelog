package com.pedrocosta.exchangelog.utils;

import org.springframework.context.ApplicationContext;
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
    private static final String MSG_BEAN_NAME = "messageProperties";

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

    /**
     * Get message by its key in properties file.
     * It uses application context to retrieve properties file.
     * <br>
     * This method exists to be used when a {@link Messages}
     * object can not be get by Spring instantiation.
     * It calls bean with name <b>messageSource</b>,
     * if it changes this class need to be changed too.
     *
     * @param context   Application context
     * @param key       Key name of message in properties file
     * @param args      Arguments to include in message
     * @return String with message
     */
    public static String get(ApplicationContext context, String key, String ... args) {
        Messages msg = (Messages) context.getBean(MSG_BEAN_NAME);
        return msg.get(key, args);
    }
}
