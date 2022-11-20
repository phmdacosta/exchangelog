package com.pedrocosta.exchangelog.auth.user.contacts.repository;

import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.auth.user.contacts.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Long> {
    UserContact findByName(String name);
}
