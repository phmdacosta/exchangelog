package com.pedrocosta.exchangelog.auth.token.access.controller;

import com.pedrocosta.exchangelog.api.response.RestResponseEntity;
import com.pedrocosta.exchangelog.auth.exception.UnauthorizedException;
import com.pedrocosta.exchangelog.auth.token.access.dto.LoginDto;
import com.pedrocosta.exchangelog.auth.token.access.dto.TokenDto;
import com.pedrocosta.exchangelog.auth.token.access.service.AuthenticationService;
import com.pedrocosta.springutils.output.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Authentication controller to handle all route points involving user authentication.
 *
 * @author Pedro H M da Costa
 * @since 1.0
 */
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
        ResponseEntity<?> result;
        try {
            TokenDto tokenDto = authenticationService.authenticate(loginDto.getUsername(), loginDto.getPassword());
            result = RestResponseEntity.ok(tokenDto);
        } catch (UnauthorizedException e) {
            result = RestResponseEntity.error(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            result = RestResponseEntity.error(HttpStatus.BAD_REQUEST, Messages.get("user.info.wrong"));
        }
        return result;
    }

    @PostMapping(value = "${route.auth.logout}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@RequestBody TokenDto tokenDto) {
        authenticationService.logout(tokenDto.getRefreshToken());
        return RestResponseEntity.ok("");
    }

    @PostMapping(value = "${route.auth.accessToken}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> renewAccessToken(@RequestBody TokenDto tokenDto) {
        ResponseEntity<?> result;
        try {
            String accessToken = authenticationService.renewAccessToken(tokenDto.getRefreshToken());
            result = RestResponseEntity.ok(new TokenDto(accessToken, tokenDto.getRefreshToken()));
        } catch (IllegalArgumentException e) {
            result = RestResponseEntity.error(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        return result;
    }
}
