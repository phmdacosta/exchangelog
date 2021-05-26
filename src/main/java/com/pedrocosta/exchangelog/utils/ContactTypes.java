package com.pedrocosta.exchangelog.utils;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
public enum ContactTypes {
    EMAIL("email"), PHONE("phone");

    private String name;

    ContactTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
