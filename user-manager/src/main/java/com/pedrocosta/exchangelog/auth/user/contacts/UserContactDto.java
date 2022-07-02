package com.pedrocosta.exchangelog.auth.user.contacts;

import com.pedrocosta.springutils.viewmapper.annotation.MappingField;
import com.pedrocosta.springutils.viewmapper.annotation.View;

@View
public class UserContactDto {
    @MappingField(name = "name")
    private String name;
    @MappingField(name = "value")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
