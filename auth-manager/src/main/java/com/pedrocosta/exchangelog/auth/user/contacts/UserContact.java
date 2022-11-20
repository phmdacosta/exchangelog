package com.pedrocosta.exchangelog.auth.user.contacts;

import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.auth.utils.ContactType;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class UserContact implements Cloneable {

    @Id
    @SequenceGenerator( name = "user_contact_seq",
            sequenceName = "user_contact_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_contact_seq")
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

    public UserContact setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public UserContact clone() throws CloneNotSupportedException {
        UserContact userContact = (UserContact) super.clone();
        return userContact.setUser(this.getUser().clone());
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
    public String toString() {
        return "UserContact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
