package com.pedrocosta.exchangelog.auth.token.access.service;

import com.pedrocosta.exchangelog.auth.exception.UnauthorizedException;
import com.pedrocosta.exchangelog.auth.token.access.dto.TokenDto;

/**
 * Business service to handle user authentication with tokens.
 *
 * @author Pedro H M da Costa
 * @since 1.0
 */
public interface AuthenticationService {
    /**
     * Executes authentication of a user, generates access and refresh tokens.
     *
     * @param username
     * @param password
     * @return  Generated tokens
     * @throws IllegalArgumentException Throws if user information is wrong or user doesn't exist
     * @throws UnauthorizedException    Throws if user is disabled
     */
    TokenDto authenticate(String username, String password) throws IllegalArgumentException, UnauthorizedException;

    /**
     * Executes logout of a user.
     *
     * @param token Refresh token
     */
    void logout(String token);

    /**
     * Generates new access token.
     *
     * @param refreshToken
     * @return  New access token
     * @throws IllegalArgumentException Throws if refresh token is invalid
     */
    String renewAccessToken(String refreshToken) throws IllegalArgumentException;

    /**
     * Generates new access and refresh token.
     *
     * @param refreshToken
     * @return  New access token
     * @throws IllegalArgumentException Throws if refresh token is invalid
     */
    TokenDto renewTokens(String refreshToken) throws IllegalArgumentException;
}
