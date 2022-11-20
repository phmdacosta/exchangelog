package com.pedrocosta.exchangelog.auth.permission.service.impl;

import com.pedrocosta.exchangelog.auth.permission.Permission;
import com.pedrocosta.exchangelog.auth.permission.repository.PermissionRepository;
import com.pedrocosta.exchangelog.auth.permission.service.PermissionService;
import com.pedrocosta.exchangelog.auth.role.Role;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.output.Messages;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository repository;

    PermissionServiceImpl(PermissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Permission save(Permission permission) throws SaveDataException {
        return repository.save(permission);
    }

    @Override
    public List<Permission> saveAll(Collection<Permission> col) throws SaveDataException {
        return repository.saveAll(col);
    }

    @Override
    public Permission find(long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Messages.get("not.found",
                String.format("permission with ID %s", id))));
    }

    @Override
    public List<Permission> findAll() {
        return repository.findAll();
    }

    @Override
    public Permission findByName(String name) throws NotFoundException {
        return repository.findByName(name).orElseThrow(() -> new NotFoundException(Messages.get("not.found",
                String.format("permission %s", name))));
    }

    @Override
    public List<Permission> findAllByRole(Role role) throws NotFoundException {
        return repository.findAllByRoles(role).orElseThrow(() -> new NotFoundException(Messages.get("not.found",
                String.format("any permission for role %s", role.getName()))));
    }
}
