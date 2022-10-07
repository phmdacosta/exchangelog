package com.pedrocosta.exchangelog.auth.user;

import com.pedrocosta.exchangelog.auth.user.contacts.UserContact;
import com.pedrocosta.exchangelog.auth.person.Person;
import com.pedrocosta.exchangelog.auth.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "USER")
public class User extends Person implements UserDetails, Cloneable {
    private String username;
    private String password;
    private boolean expired = false;
    private boolean locked = false;
    private boolean enabled = false;

    //Foreign objects
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserContact> contacts;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Client client;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles;

    public User() {
        this.contacts = new ArrayList<>();
        this.roles = new HashSet<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return authorities;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
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

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isExpired() {
        return expired;
    }

    public User setExpired(boolean expired) {
        this.expired = expired;
        return this;
    }

    public boolean isLocked() {
        return locked;
    }

    public User setLocked(boolean locked) {
        this.locked = locked;
        return this;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        User clone = (User) super.clone();
        return clone.setContacts(new ArrayList<>(this.getContacts()))
                .setRoles(new HashSet<>(this.getRoles()))
                .setClient(this.getClient().clone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return isExpired() == user.isExpired()
                && isLocked() == user.isLocked()
                && isEnabled() == user.isEnabled()
                && Objects.equals(getUsername(), user.getUsername())
                && Objects.equals(getPassword(), user.getPassword())
                && Objects.equals(getContacts(), user.getContacts())
                && Objects.equals(isClient(), user.isClient())
                && Objects.equals(getRoles(), user.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(), getUsername(), getPassword(), isExpired(),
                isLocked(), isEnabled(), getContacts(), isClient(), getRoles());
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", username='" + username + '\'' +
                ", expired=" + expired +
                ", locked=" + locked +
                ", enabled=" + enabled +
                '}';
    }
}
