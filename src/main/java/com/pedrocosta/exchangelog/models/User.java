package com.pedrocosta.exchangelog.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @ElementCollection
    private List<UserContact> contacts;

    public User() {
        contacts = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<UserContact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contacts);
    }

    @Override
    protected User clone() throws CloneNotSupportedException {
        User cloned = (User) super.clone();
        cloned.setId(this.getId());
        cloned.setName(this.getName());

        if (!this.getContacts().isEmpty()) {
            List<UserContact> contactsClone = new ArrayList<>();
            for (UserContact contact : this.getContacts()) {
                contactsClone.add(contact.clone());
            }
            cloned.setContacts(contactsClone);
        }

        return cloned;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
