package com.pedrocosta.exchangelog.auth.role;

import com.pedrocosta.exchangelog.auth.permission.Permission;
import com.pedrocosta.exchangelog.auth.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Role implements Cloneable {

    @Id
    @SequenceGenerator( name = "user_role_seq",
            sequenceName = "user_role_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_seq")
    private long id;
    @Column(nullable = false, unique = true)
    private String name;

    //Foreign objects
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Permission> permissions;

    public long getId() {
        return id;
    }

    public Role setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public Role setUsers(List<User> users) {
        this.users = users;
        return this;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public Role setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public Role clone() throws CloneNotSupportedException {
        Role clone = (Role) super.clone();
        return clone.setUsers(new ArrayList<>(this.getUsers()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return getId() == role.getId()
                && Objects.equals(getName(), role.getName())
                && Objects.equals(getUsers(), role.getUsers())
                && Objects.equals(getPermissions(), role.getPermissions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getUsers(), getPermissions());
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                '}';
    }
}
