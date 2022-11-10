package com.pedrocosta.exchangelog.auth.security.token.access;

import com.pedrocosta.exchangelog.auth.security.token.access.dto.TokenDto;

public interface AuthenticationService {
    TokenDto authenticate(String username, String password);
    void logout(String token);
    String renewAccessToken(String refreshToken) throws IllegalArgumentException;
    TokenDto renewTokens(String refreshToken) throws IllegalArgumentException;
}
