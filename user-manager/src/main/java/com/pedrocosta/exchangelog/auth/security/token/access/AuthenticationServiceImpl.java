package com.pedrocosta.exchangelog.auth.security.token.access;

import com.pedrocosta.exchangelog.auth.security.token.access.dto.TokenDto;
import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.auth.user.UserService;
import javassist.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtHandler jwtHandler;
    private final UserService userService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtHandler jwtHandler, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtHandler = jwtHandler;
        this.userService = userService;
    }

    @Override
    public TokenDto authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String accessToken = jwtHandler.generateAccessToken(user);
        String refreshTokenString = jwtHandler.generateRefreshToken(user, "");
        return new TokenDto(user.getId(), accessToken, refreshTokenString);
    }

    @Override
    public void logout(String token) {
        if (jwtHandler.validateRefreshToken(token)) {
            // logout
        }
    }

    @Override
    public String renewAccessToken(String refreshToken) throws IllegalArgumentException {
        if (jwtHandler.validateRefreshToken(refreshToken)) {
            try {
                User user = userService.find(jwtHandler.getUserIdFromRefreshToken(refreshToken));
                return jwtHandler.generateAccessToken(user);
            } catch (NotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }
        throw new IllegalArgumentException("Refresh token invalid.");
    }

    @Override
    public TokenDto renewTokens(String refreshToken) throws IllegalArgumentException {
        if (jwtHandler.validateRefreshToken(refreshToken)) {
            try {
                User user = userService.find(jwtHandler.getUserIdFromRefreshToken(refreshToken));
                String accessToken = jwtHandler.generateAccessToken(user);
                String newRefreshToken = jwtHandler.generateRefreshToken(user, "");
                return new TokenDto(user.getId(), accessToken, newRefreshToken);
            } catch (NotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }
        throw new IllegalArgumentException("Refresh token invalid.");
    }
}
