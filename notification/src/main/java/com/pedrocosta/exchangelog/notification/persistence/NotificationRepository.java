package com.pedrocosta.exchangelog.notification.persistence;

import com.pedrocosta.exchangelog.notification.Notification;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
