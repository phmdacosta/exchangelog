package com.pedrocosta.exchangelog.auth.permission.utils;

import com.pedrocosta.exchangelog.auth.permission.Permission;
import org.springframework.beans.factory.annotation.Value;

public enum Permissions {

    ALL("all", "route.api"),
    NONE("none", "none");

    @Value("${}")
    private final String allRoute;
    private final Permission object;

    private Permissions(String name, String target) {
        this.object = new Permission();
        this.object.setName(name);
        this.object.setRoute(target);
    }

    public Permission getObject() {
        try {
            return object.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return object.getName();
    }

    public String getRoute() {
        return object.getRoute();
    }
}
