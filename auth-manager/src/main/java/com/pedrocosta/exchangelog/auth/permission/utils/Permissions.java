package com.pedrocosta.exchangelog.auth.permission.utils;

import com.pedrocosta.exchangelog.auth.permission.Permission;

public enum Permissions {

    ALL("all", Route.API),
    NONE("none", "none");

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
