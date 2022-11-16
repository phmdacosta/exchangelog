package com.pedrocosta.exchangelog.auth.permission.service;

import com.pedrocosta.exchangelog.RepositoryService;
import com.pedrocosta.exchangelog.auth.permission.Permission;
import com.pedrocosta.exchangelog.auth.role.Role;
import javassist.NotFoundException;

import java.util.List;

public interface PermissionService extends RepositoryService<Permission> {
    Permission findByName(String name) throws NotFoundException;
    List<Permission> findAllByRole(Role role) throws NotFoundException;
}
