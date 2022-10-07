package com.pedrocosta.exchangelog.auth.registration;

import com.pedrocosta.exchangelog.annotation.View;
import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.auth.user.UserCreationDto;
import com.pedrocosta.exchangelog.auth.user.UserService;
import com.pedrocosta.exchangelog.auth.user.contacts.UserContact;
import com.pedrocosta.exchangelog.auth.utils.ContactType;
import com.pedrocosta.exchangelog.auth.utils.Route;
import com.pedrocosta.exchangelog.auth.validation.EmailValidator;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.output.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = Route.API + Route.REGISTRATION)
public class RegistrationController {
    private final UserService userService;
    private final EmailValidator emailValidator;

    public RegistrationController(UserService userService, EmailValidator emailValidator) {
        this.userService = userService;
        this.emailValidator = emailValidator;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody @View(UserCreationDto.class) User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!user.getContacts().isEmpty()) {
            UserContact userContact = user.getContacts().stream().filter(
                            contact -> ContactType.EMAIL.name().equals(contact.getName()))
                    .findFirst().orElse(null);
            if (userContact != null && emailValidator.test(userContact.getValue())) {
                return ResponseEntity.badRequest().build();
            }
        }

        ResponseEntity<String> response;
        try {
            String token = userService.register(user);
            response = ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            Log.error(this, e);
            response = ResponseEntity.badRequest().body(e.getMessage());
        }

        return response;
    }

    @PostMapping(value = "/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> confirmToken(@RequestParam String token) {
        ResponseEntity<String> response = ResponseEntity.ok("confirmed");
        try {
            userService.confirmToken(token);
        } catch (IllegalArgumentException e) {
            Log.error(this, e);
            response = ResponseEntity.badRequest().body(e.getMessage());
        }
        return response;
    }
}
