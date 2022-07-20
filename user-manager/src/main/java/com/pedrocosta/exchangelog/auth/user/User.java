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
public class User implements UserDetails, Cloneable {

    @Id
    @SequenceGenerator( name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private boolean expired = false;
    private boolean locked = false;
    private boolean enabled = false;

    //Foreign objects
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserContact> contacts;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Person person;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Client client;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
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

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id
                || Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, contacts);
    }

    @Override
    protected User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + username + '\'' +
                ", client=" + isClient() +
                '}';
    }
}
