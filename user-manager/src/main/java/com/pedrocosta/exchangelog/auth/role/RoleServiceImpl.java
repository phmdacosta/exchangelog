package com.pedrocosta.exchangelog.auth.role;

import com.pedrocosta.exchangelog.auth.permission.Permission;
import com.pedrocosta.exchangelog.auth.permission.PermissionService;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.output.Messages;
import com.sun.istack.Nullable;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final PermissionService permissionService;

    RoleServiceImpl(RoleRepository repository, PermissionService permissionService) {
        this.repository = repository;
        this.permissionService = permissionService;
    }

    @Override
    public Role save(Role role) throws SaveDataException {
        try {
            return repository.save(role);
        } catch (Exception e) {
            throw new SaveDataException(e);
        }
    }

    @Override
    public List<Role> saveAll(Collection<Role> col) throws SaveDataException {
        try {
            return repository.saveAll(col);
        } catch (Exception e) {
            throw new SaveDataException(e);
        }
    }

    @Override
    @Nullable
    public Role find(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Messages.get("not.found",
                String.format("role with ID %s", id))));
    }

    @Override
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Override
    public Role findByName(String name) throws NotFoundException {
        return repository.findByName(name).orElseThrow(() -> new NotFoundException(Messages.get("not.found",
                String.format("role %s", name))));
    }

    @Override
    public List<Permission> getPermissionsByRole(Role role) throws NotFoundException {
        return permissionService.findAllByRole(role);
    }
}
