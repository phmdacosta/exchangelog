package com.pedrocosta.exchangelog.auth.security.token.access;

import com.pedrocosta.exchangelog.auth.exception.UnauthorizedException;
import com.pedrocosta.exchangelog.auth.security.token.access.dto.TokenDto;
import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.auth.user.UserService;
import com.pedrocosta.springutils.output.Log;
import com.pedrocosta.springutils.output.Messages;
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
    public TokenDto authenticate(String username, String password) throws IllegalArgumentException, UnauthorizedException {
        try {
            User user = userService.find(username);

            if (user.isLocked()) {
                throw new UnauthorizedException(Messages.get("user.locked"));
            }

            if (user.isExpired()) {
                throw new UnauthorizedException(Messages.get("user.expired"));
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtHandler.generateAccessToken(user);
            String refreshTokenString = generateRefreshToken(user);
            return new TokenDto(accessToken, refreshTokenString);

        } catch (NotFoundException e) {
            Log.error(this, e);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void logout(String token) {
        if (jwtHandler.validateRefreshToken(token)) {
            SecurityContextHolder.getContext().setAuthentication(null);
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
        throw new IllegalArgumentException(Messages.get("token.refresh.invalid"));
    }

    @Override
    public TokenDto renewTokens(String refreshToken) throws IllegalArgumentException {
        if (jwtHandler.validateRefreshToken(refreshToken)) {
            try {
                User user = userService.find(jwtHandler.getUserIdFromRefreshToken(refreshToken));
                String accessToken = jwtHandler.generateAccessToken(user);
                String newRefreshToken = generateRefreshToken(user);
                return new TokenDto(accessToken, newRefreshToken);
            } catch (NotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }
        throw new IllegalArgumentException(Messages.get("token.refresh.invalid"));
    }

    protected String generateRefreshToken(User user) {
        return jwtHandler.generateRefreshToken(user);
    }
}
