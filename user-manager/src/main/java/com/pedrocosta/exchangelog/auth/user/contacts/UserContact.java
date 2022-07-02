package com.pedrocosta.exchangelog.auth.user.contacts;

import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.auth.utils.ContactType;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class UserContact implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String value;

    //Foreign objects
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;

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

    public UserContact setName(ContactType type) {
        this.setName(type.name());
        return this;
    }

    public String getValue() {
        return value;
    }

    public UserContact setValue(String value) {
        this.value = value;
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
