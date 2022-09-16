package com.pedrocosta.exchangelog.auth.permission;

import com.pedrocosta.exchangelog.auth.role.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Permission {

    @Id
    @SequenceGenerator( name = "role_perm_seq",
            sequenceName = "role_perm_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_perm_seq")
    private long id;
    private String name;
    private String target;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

    public String getTarget() {
        return target;
    }

    public Permission setTarget(String target) {
        this.target = target;
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
                && Objects.equals(getTarget(), that.getTarget())
                && Objects.equals(getRoles(), that.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTarget(), getRoles());
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                '}';
    }
}
