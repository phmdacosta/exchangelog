package com.pedrocosta.exchangelog.auth.user.contacts.utils;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public enum ContactType {
    EMAIL, PHONE, MOBILE_APP;
    public static ContactType get(String name) {
        return valueOf(name.toUpperCase());
    }
    public boolean matches(String name) {
        return this.name().equalsIgnoreCase(name);
    }
}
