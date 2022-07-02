package com.pedrocosta.exchangelog.auth.user.contacts;

import com.pedrocosta.exchangelog.auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {
    UserContact findByName(String name);
}
