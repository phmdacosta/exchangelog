package com.pedrocosta.exchangelog.auth.security.token.access;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.springutils.output.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

@Component
public class JwtHandler {

    @Value("${token.access.expire.minutes}")
    private long accessTokenExpirationMinutes;
    @Value("${token.refresh.expire.minutes}")
    private long accessRefreshExpirationMinutes;

    private final String issuer;
    private final Algorithm accessTokenAlgorithm;
    private final Algorithm refreshTokenAlgorithm;
    private final JWTVerifier accessTokenVerifier;
    private final JWTVerifier refreshTokenVerifier;

    public JwtHandler(@Value("${token.access.issuer}") String issuer,
                      @Value("${token.access.secret}") String accessTokenSecret,
                      @Value("${token.refresh.secret}") String refreshTokenSecret) {
        this.issuer = issuer;
        this.accessTokenAlgorithm = Algorithm.HMAC512(Base64.getDecoder().decode(accessTokenSecret));
        this.refreshTokenAlgorithm = Algorithm.HMAC512(Base64.getDecoder().decode(refreshTokenSecret));
        this.accessTokenVerifier = JWT.require(accessTokenAlgorithm).withIssuer(issuer).build();
        this.refreshTokenVerifier = JWT.require(refreshTokenAlgorithm).withIssuer(issuer).build();
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(String.valueOf(user.getId()))
                .withIssuedAt(now)
                .withExpiresAt(now.plus(accessTokenExpirationMinutes, ChronoUnit.MINUTES))
                .sign(accessTokenAlgorithm);
    }

    public String generateRefreshToken(User user) {
        return generateRefreshToken(user, null);
    }

    public String generateRefreshToken(User user, String tokenId) {
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(String.valueOf(user.getId()))
                .withClaim("tokenId", tokenId)
                .withIssuedAt(now)
                .withExpiresAt(now.plus(accessRefreshExpirationMinutes, ChronoUnit.MINUTES))
                .sign(refreshTokenAlgorithm);
    }

    public boolean validateAccessToken(String token) {
        return decodeAccessToken(token).isPresent();
    }

    public boolean validateRefreshToken(String token) {
        return decodeRefreshToken(token).isPresent();
    }

    public String getUserIdFromAccessToken(String token) {
        return decodeAccessToken(token).get().getSubject();
    }

    public String getUserIdFromRefreshToken(String token) {
        return decodeRefreshToken(token).get().getSubject();
    }

    private Optional<DecodedJWT> decodeAccessToken(String token) {
        try {
            return Optional.of(accessTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            Log.error(this, e);
        }
        return Optional.empty();
    }

    private Optional<DecodedJWT> decodeRefreshToken(String token) {
        try {
            return Optional.of(refreshTokenVerifier.verify(token));
        } catch (JWTVerificationException e) {
            Log.error(this, e);
        }
        return Optional.empty();
    }
}
