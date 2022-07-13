package com.pedrocosta.exchangelog.auth.user.registration.token;

import com.pedrocosta.exchangelog.auth.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ConfirmationToken {
    @Id
    @SequenceGenerator( name = "confirm_token_seq",
                        sequenceName = "confirm_token_seq",
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirm_token_seq")
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;
    @Column(name = "expired_time", nullable = false)
    private LocalDateTime expiredTime;
    @Column(name = "confirm_time")
    private LocalDateTime confirmationTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public LocalDateTime getConfirmationTime() {
        return confirmationTime;
    }

    public void setConfirmationTime(LocalDateTime confirmationTime) {
        this.confirmationTime = confirmationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
