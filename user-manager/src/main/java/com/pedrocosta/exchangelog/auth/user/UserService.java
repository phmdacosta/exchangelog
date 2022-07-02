package com.pedrocosta.exchangelog.auth.user;

import com.pedrocosta.exchangelog.RepositoryService;
import com.pedrocosta.exchangelog.auth.role.Role;

public interface UserService extends RepositoryService<User> {
    User find(String username);
    Role saveRole(Role role);
    void addRoleToUser(User user, Role role);
    void addRoleToUser(String username, String roleName);
}
