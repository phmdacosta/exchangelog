package com.pedrocosta.exchangelog.auth.user;

import com.pedrocosta.exchangelog.annotation.View;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.viewmapper.ViewMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
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

        User user = service.find(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserDto dto = ViewMapper.map(user, UserDto.class);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/user/new",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> newUser(@RequestBody @View(UserCreationDto.class) User user) {

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<UserDto> response;
        try {
            User saved = service.save(user);
            UserDto view = ViewMapper.map(saved, UserDto.class);
            response = ResponseEntity.ok(view);
        } catch (SaveDataException e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return response;
    }

}
