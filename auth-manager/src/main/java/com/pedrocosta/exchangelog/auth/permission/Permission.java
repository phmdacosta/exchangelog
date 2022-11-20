package com.pedrocosta.exchangelog.auth.permission;

import com.pedrocosta.exchangelog.auth.role.Role;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "PERMISSION")
public class Permission implements Cloneable {

    @Id
    @SequenceGenerator( name = "ROLE_PERM_SEQ",
            sequenceName = "ROLE_PERM_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_PERM_SEQ")
    private long id;
    private String name;
    private String route;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

    public long getId() {
        return id;
    }

    public Permission setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Permission setName(String name) {
        this.name = name;
        return this;
    }

    public String getRoute() {
        return route;
    }

    public Permission setRoute(String route) {
        this.route = route;
        return this;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Permission setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return getId() == that.getId()
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getRoute(), that.getRoute())
                && Objects.equals(getRoles(), that.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRoute(), getRoles());
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public Permission clone() throws CloneNotSupportedException {
        Permission clone = (Permission) super.clone();
        try {
            clone.setRoles(this.getRoles().stream().map(role -> {
                try {
                    return role.clone();
                } catch (CloneNotSupportedException e) {
                    return null;
                }
            }).collect(Collectors.toList()));
        } catch (Exception ignored) {
        }
        return clone;
    }
}
