package com.pedrocosta.exchangelog.auth.role;

import com.pedrocosta.exchangelog.auth.permission.Permission;
import com.pedrocosta.exchangelog.auth.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ROLE")
public class Role implements Cloneable {

    @Id
    @SequenceGenerator( name = "USER_ROLE_SEQ",
            sequenceName = "USER_ROLE_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ROLE_SEQ")
    private long id;
    @Column(nullable = false, unique = true)
    private String name;

    //Foreign objects
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "ROLE_PERMISSIONS",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID")
    )
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
        clone.setPermissions(new ArrayList<>());
        for (Permission permission : this.getPermissions()) {
            clone.getPermissions().add(permission.clone());
        }
        return clone;
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
