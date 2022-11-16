package com.pedrocosta.exchangelog.auth.user.dto;

import com.pedrocosta.springutils.viewmapper.annotation.MappingField;
import com.pedrocosta.springutils.viewmapper.annotation.View;

@View
public class UserCreationDto extends UserDto {
    @MappingField(name = "password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
