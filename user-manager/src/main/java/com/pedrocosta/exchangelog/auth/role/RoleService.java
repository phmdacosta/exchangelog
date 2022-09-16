package com.pedrocosta.exchangelog.auth.role;

import com.pedrocosta.exchangelog.RepositoryService;
import com.pedrocosta.exchangelog.auth.permission.Permission;
import com.sun.istack.Nullable;
import javassist.NotFoundException;

import java.util.List;

public interface RoleService extends RepositoryService<Role> {
    Role findByName(String name) throws NotFoundException;
    List<Permission> getPermissionsByRole(Role role);
}
