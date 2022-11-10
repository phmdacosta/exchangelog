package com.pedrocosta.exchangelog.auth.security.token.access;

import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.auth.user.UserService;
import com.pedrocosta.springutils.output.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AccessTokenFilter extends OncePerRequestFilter {

    private final JwtHandler jwtHandler;
    private final UserService userService;

    public AccessTokenFilter(JwtHandler jwtHandler, UserService userService) {
        this.jwtHandler = jwtHandler;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> accessToken = parseToken(httpServletRequest);
            if (accessToken.isPresent() && jwtHandler.validateAccessToken(accessToken.get())) {
                String userId = jwtHandler.getUserIdFromAccessToken(accessToken.get());
                User user = userService.find(Long.parseLong(userId));
                UsernamePasswordAuthenticationToken userNamePassAuthToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                userNamePassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(userNamePassAuthToken);
            }
        } catch (Exception e) {
            Log.error(this, e);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Optional<String> parseToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.replace("Bearer ", ""));
        }

        return Optional.empty();
    }
}
