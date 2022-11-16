package com.pedrocosta.exchangelog.auth.person.repository;

import com.pedrocosta.exchangelog.auth.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByFirstName(String firstName);
}
