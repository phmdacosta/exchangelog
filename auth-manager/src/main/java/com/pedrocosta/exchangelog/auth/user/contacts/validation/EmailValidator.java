package com.pedrocosta.exchangelog.auth.user.contacts.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {

    private final String regex;

    public EmailValidator (Environment environment) {
        this.regex = environment.getProperty("validation.email.regex");
    }

    @Override
    public boolean test(String email) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
