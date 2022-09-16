package com.pedrocosta.exchangelog.auth.permission;

import com.pedrocosta.exchangelog.RepositoryService;
import com.pedrocosta.exchangelog.auth.role.Role;

import java.util.List;

public interface PermissionService extends RepositoryService<Permission> {
    List<Permission> findAllByRole(Role role);
}
