package com.pedrocosta.exchangelog.auth.user;

import com.pedrocosta.exchangelog.annotation.View;
import com.pedrocosta.exchangelog.auth.user.contacts.UserContact;
import com.pedrocosta.exchangelog.auth.utils.ContactType;
import com.pedrocosta.exchangelog.auth.utils.Route;
import com.pedrocosta.exchangelog.auth.validation.EmailValidator;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.viewmapper.ViewMapper;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = Route.API)
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> users = service.findAll();
        List<UserDto> userViews = new ArrayList<>();
        for (User user : users) {
            UserDto dto = ViewMapper.map(user, UserDto.class);
            userViews.add(dto);
        }
        return ResponseEntity.ok(userViews);
    }

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@RequestParam String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<UserDto> response;
        try {
            User user = service.find(username);
            UserDto dto = ViewMapper.map(user, UserDto.class);
            response = ResponseEntity.ok(dto);
        } catch (NotFoundException e) {
            Log.error(this, e);
            response = ResponseEntity.notFound().build();
        }

        return response;
    }

}
