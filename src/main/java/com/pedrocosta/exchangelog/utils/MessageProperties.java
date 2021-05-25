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
public class MessageProperties {

    private MessageSource source;
    private static final String MSG_BEAN_NAME = "messageProperties";

    public MessageProperties setSource(MessageSource source) {
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
    public String get(String key, String ... args) {
        return source.getMessage(key, args, Defaults.LOCALE);
    }

    /**
     * Get message by its key in properties file.
     * It uses application context to retrieve properties file.
     * <br>
     * This method exists to be used when a {@link MessageProperties}
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
        MessageProperties msg = (MessageProperties) context.getBean(MSG_BEAN_NAME);
        return msg.get(key, args);
    }
}
