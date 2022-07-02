package com.pedrocosta.exchangelog.auth.user;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String apiKey;

    //Foreign objects
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;

    public Client() {
    }

    public Client(String apiKey) {
        this();
        this.apiKey = apiKey;
    }

    public long getId() {
        return id;
    }

    public Client setId(long id) {
        this.id = id;
        return this;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Client setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Client setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Objects.equals(apiKey, client.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), apiKey);
    }

    @Override
    protected Client clone() throws CloneNotSupportedException {
        return (Client) super.clone();
    }

    @Override
    public String toString() {
        return "Client{" +
                "apiKey='" + apiKey + '\'' +
                '}';
    }
}
