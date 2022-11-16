package com.pedrocosta.exchangelog.auth.security.token.access;

import com.pedrocosta.exchangelog.auth.exception.UnauthorizedException;
import com.pedrocosta.exchangelog.auth.security.token.access.dto.TokenDto;

public interface AuthenticationService {
    TokenDto authenticate(String username, String password) throws IllegalArgumentException, UnauthorizedException;
    void logout(String token);
    String renewAccessToken(String refreshToken) throws IllegalArgumentException;
    TokenDto renewTokens(String refreshToken) throws IllegalArgumentException;
}
