package com.pedrocosta.exchangelog.auth.role;

import com.pedrocosta.exchangelog.auth.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Role {

    @Id
    @SequenceGenerator( name = "user_role_seq",
            sequenceName = "user_role_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_seq")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    //Foreign objects
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
