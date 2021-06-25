package com.pedrocosta.exchangelog.models;

import com.pedrocosta.exchangelog.utils.ContactTypes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class UserContact implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String value;
    private ContactTypes type;

    public long getId() {
        return id;
    }

    public UserContact setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserContact setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UserContact setValue(String value) {
        this.value = value;
        return this;
    }

    public ContactTypes getType() {
        return type;
    }

    public UserContact setType(ContactTypes type) {
        this.type = type;
        setName(type.name().toLowerCase());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserContact that = (UserContact) o;
        return id == that.id ||
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value);
    }

    @Override
    protected UserContact clone() throws CloneNotSupportedException {
        return (UserContact) super.clone();
    }

    @Override
    public String toString() {
        return "UserContact{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
