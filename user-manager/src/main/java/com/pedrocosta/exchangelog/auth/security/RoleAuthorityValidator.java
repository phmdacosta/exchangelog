package com.pedrocosta.exchangelog.auth.security;

import com.pedrocosta.exchangelog.auth.permission.Permission;
import com.pedrocosta.exchangelog.auth.role.Role;
import com.pedrocosta.exchangelog.auth.role.RoleService;
import javassist.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@Component
public class RoleAuthorityValidator {
    private final RoleService roleService;

    RoleAuthorityValidator(RoleService roleService) {
        this.roleService = roleService;
    }

    public boolean accept(Authentication authentication, HttpServletRequest request) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            try {
                Role role = roleService.findByName(authority.getAuthority());
                List<Permission> permissions = role.getPermissions();
                return permissions.stream().anyMatch(permission ->
                        permission.getTarget().equals(request.getRequestURI()));
            } catch (NotFoundException ignored) {}
        }

        return false;
    }
}
