package com.pedrocosta.exchangelog.auth.user;

import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.viewmapper.ViewMapper;
import javassist.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final UserService service;
    private final ViewMapper mapper;

    public UserController(UserService service, ViewMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(value = "${route.users.list}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> users = service.findAll();
        List<UserDto> userViews = new ArrayList<>();
        for (User user : users) {
            UserDto dto = mapper.map(user, UserDto.class);
            userViews.add(dto);
        }
        return ResponseEntity.ok(userViews);
    }

    @GetMapping(value = "${route.get.user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@RequestParam String username) {
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        ResponseEntity<UserDto> response;
        try {
            User user = service.find(username);
            UserDto dto = mapper.map(user, UserDto.class);
            response = ResponseEntity.ok(dto);
        } catch (NotFoundException e) {
            Log.error(this, e);
            response = ResponseEntity.notFound().build();
        }

        return response;
    }

}
