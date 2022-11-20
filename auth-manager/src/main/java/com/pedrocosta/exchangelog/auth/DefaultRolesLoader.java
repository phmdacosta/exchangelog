package com.pedrocosta.exchangelog.auth;

import com.pedrocosta.exchangelog.auth.permission.Permission;
import com.pedrocosta.exchangelog.auth.permission.utils.Permissions;
import com.pedrocosta.exchangelog.auth.role.Role;
import com.pedrocosta.exchangelog.auth.role.service.RoleService;
import com.pedrocosta.exchangelog.auth.role.utils.Roles;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.output.Log;
import javassist.NotFoundException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultRolesLoader implements ApplicationRunner {

    private final RoleService roleService;

    public DefaultRolesLoader(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //Create default roles
        for (Roles roleEnum : Roles.values()) {
            Log.info(this, String.format("Loading role %s", roleEnum.name()));
            try {
                roleService.findByName(roleEnum.name());
            } catch (NotFoundException e) {
                createRole(roleEnum);
            }
        }
    }

    private Role createRole(final Roles roleEnum) {
        Log.info(this, String.format("Creating role %s", roleEnum.name()));

        Role newRole = new Role();
        newRole.setName(roleEnum.name());
        Permissions permissionEnum = Permissions.NONE;
        //By default, ADMIN has all permissions
        if (roleEnum == Roles.ADMIN) {
            permissionEnum = Permissions.ALL;
            Permission permission = permissionEnum.getObject();
            newRole.setPermissions(List.of(permission));
        }

        try {
            return roleService.save(newRole);
        } catch (SaveDataException e) {
            Log.error(this, e);
        }

        return null;
    }
}
