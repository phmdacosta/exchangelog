package com.pedrocosta.exchangelog.models;

import com.pedrocosta.exchangelog.utils.notifications.NotificationMeans;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Pedro H M da Costa
 * @version 1.0
 */
@Entity(name = "notif_request")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class NotificationRequest implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private NotificationMeans means;
    private boolean enabled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public long getId() {
        return id;
    }

    public NotificationRequest setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public NotificationRequest setName(String name) {
        this.name = name;
        return this;
    }

    public NotificationMeans getMeans() {
        return means;
    }

    public NotificationRequest setMeans(NotificationMeans means) {
        this.means = means;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public NotificationRequest setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public User getUser() {
        return user;
    }

    public NotificationRequest setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationRequest that = (NotificationRequest) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                means == that.means;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, means, enabled, user);
    }

    @Override
    protected NotificationRequest clone() throws CloneNotSupportedException {
        return (NotificationRequest) super.clone();
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", means=" + means +
                ", enabled=" + enabled +
                ", user=" + user +
                '}';
    }
}
