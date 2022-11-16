package com.pedrocosta.exchangelog.auth.person.dto;

import com.pedrocosta.springutils.viewmapper.annotation.MappingField;
import com.pedrocosta.springutils.viewmapper.annotation.View;

@View
public class PersonDto {
    @MappingField(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
