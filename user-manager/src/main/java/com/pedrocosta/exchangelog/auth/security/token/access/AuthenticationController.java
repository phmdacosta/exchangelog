package com.pedrocosta.exchangelog.auth.security.token.access;

import com.pedrocosta.exchangelog.auth.security.token.access.dto.LoginDto;
import com.pedrocosta.exchangelog.auth.security.token.access.dto.TokenDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "${route.auth.login}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        TokenDto tokenDto = authenticationService.authenticate(loginDto.getUsername(), loginDto.getPassword());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping(value = "${route.auth.logout}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@RequestBody TokenDto tokenDto) {
        authenticationService.logout(tokenDto.getRefreshToken());
        return ResponseEntity.ok("");
    }

    @PostMapping(value = "${route.auth.accessToken}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> renewAccessToken(@RequestBody TokenDto tokenDto) {
        ResponseEntity<?> result;
        try {
            String accessToken = authenticationService.renewAccessToken(tokenDto.getRefreshToken());
            result = ResponseEntity.ok(new TokenDto(tokenDto.getUserId(), accessToken, tokenDto.getRefreshToken()));
        } catch (IllegalArgumentException e) {
            result = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return result;
    }
}
