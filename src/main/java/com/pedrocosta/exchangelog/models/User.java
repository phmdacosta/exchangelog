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
    private String password;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<UserContact> contacts;
    @OneToOne(fetch = FetchType.LAZY)
    private Person person;
    @OneToOne(fetch = FetchType.LAZY)
    private Client client;

    public User() {
        contacts = new ArrayList<>();
    }

    public User(String name, String password) {
        this();
        this.name = name;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<UserContact> getContacts() {
        return contacts;
    }

    public User setContacts(List<UserContact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public User addContact(UserContact contact) {
        this.contacts.add(contact);
        return this;
    }

    public Person getPerson() {
        return person;
    }

    public User setPerson(Person person) {
        this.person = person;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public User setClient(Client client) {
        this.client = client;
        return this;
    }

    @Transient
    public boolean isClient() {
        return client != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id
                || Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contacts);
    }

    @Override
    protected User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", client=" + isClient() +
                '}';
    }
}
