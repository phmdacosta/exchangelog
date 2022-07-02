package com.pedrocosta.exchangelog.auth.user;

import com.pedrocosta.exchangelog.auth.user.contacts.UserContactDto;
import com.pedrocosta.exchangelog.auth.person.PersonDto;
import com.pedrocosta.exchangelog.auth.role.RoleDto;
import com.pedrocosta.springutils.viewmapper.annotation.MappingCollection;
import com.pedrocosta.springutils.viewmapper.annotation.MappingField;
import com.pedrocosta.springutils.viewmapper.annotation.View;

import java.util.List;
import java.util.Set;

@View
public class UserDto {
    @MappingField(name = "username")
    private String username;
    @MappingField(name = "person")
    private PersonDto person;
    @MappingCollection(name = "contacts", resultElementClass = UserContactDto.class)
    private List<UserContactDto> contacts;
    @MappingCollection(name = "roles", resultElementClass = RoleDto.class)
    private Set<RoleDto> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public List<UserContactDto> getContacts() {
        return contacts;
    }

    public void setContacts(List<UserContactDto> contacts) {
        this.contacts = contacts;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }
}
