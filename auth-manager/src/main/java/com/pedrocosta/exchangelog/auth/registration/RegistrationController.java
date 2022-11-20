package com.pedrocosta.exchangelog.auth.registration;

import com.pedrocosta.exchangelog.annotation.View;
import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.auth.user.dto.UserCreationDto;
import com.pedrocosta.exchangelog.auth.user.service.UserService;
import com.pedrocosta.exchangelog.auth.user.contacts.UserContact;
import com.pedrocosta.exchangelog.auth.utils.ContactType;
import com.pedrocosta.exchangelog.auth.user.contacts.validation.EmailValidator;
import com.pedrocosta.exchangelog.api.response.RestResponseEntity;
import com.pedrocosta.springutils.output.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {
    private final UserService userService;
    private final EmailValidator emailValidator;

    public RegistrationController(UserService userService, EmailValidator emailValidator) {
        this.userService = userService;
        this.emailValidator = emailValidator;
    }

    @PostMapping(value = "${route.registration}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody @View(UserCreationDto.class) User user) {
        if (user == null) {
            return RestResponseEntity.badRequest("");
        }

        if (!user.getContacts().isEmpty()) {
            UserContact userContact = user.getContacts().stream().filter(
                            contact -> ContactType.EMAIL.name().equals(contact.getName()))
                    .findFirst().orElse(null);
            if (userContact != null && emailValidator.test(userContact.getValue())) {
                return ResponseEntity.badRequest().build();
            }
        }

        ResponseEntity<?> response;
        try {
            String token = userService.register(user);
            response = ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            Log.error(this, e);
            response = RestResponseEntity.badRequest(e.getMessage());
        }

        return response;
    }

    @PostMapping(value = "${route.registration.confirm}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> confirmToken(@RequestParam String token) {
        ResponseEntity<?> response = RestResponseEntity.ok("confirmed");
        try {
            userService.confirmToken(token);
        } catch (IllegalArgumentException e) {
            Log.error(this, e);
            response = RestResponseEntity.badRequest(e.getMessage());
        }
        return response;
    }
}
