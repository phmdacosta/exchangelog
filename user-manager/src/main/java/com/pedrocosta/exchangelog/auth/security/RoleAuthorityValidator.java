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

/**
 * Validator class to validate access permission.
 * @since v1.0 (2022)
 */
@Component
public class RoleAuthorityValidator {
    private final RoleService roleService;

    RoleAuthorityValidator(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Verifies if request URI access is permitted by user ROLE.
     * @param authentication
     * @param request
     * @return True if it is permitted, false otherwise.
     */
    public boolean accept(Authentication authentication, HttpServletRequest request) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            try {
                Role role = roleService.findByName(authority.getAuthority());
                List<Permission> permissions = role.getPermissions();
                /*
                Let's use startsWith to accept any sub URI under configured one.
                Ex: If a role is configured with URI '/abc/function', it must access any process under '/abc/function'
                (/abc/function/proc01/xpto or /abc/function/proc05/xpto2)
                 */
                return permissions.stream().anyMatch(permission ->
                        request.getRequestURI().startsWith(permission.getTarget()));
            } catch (NotFoundException ignored) {}
        }

        return false;
    }
}
