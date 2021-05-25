package com.pedrocosta.exchangelog.models;

import com.pedrocosta.exchangelog.utils.NotificationMeans;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationRequest that = (NotificationRequest) o;
        return id == that.id &&
                enabled == that.enabled &&
                Objects.equals(name, that.name) &&
                means == that.means;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, means, enabled);
    }

    @Override
    protected NotificationRequest clone() throws CloneNotSupportedException {
        NotificationRequest cloned = (NotificationRequest) super.clone();
        cloned.setId(this.getId())
                .setName(this.getName())
                .setMeans(this.getMeans())
                .setEnabled(this.isEnabled());
        return cloned;
    }
}