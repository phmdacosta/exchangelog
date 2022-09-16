package com.pedrocosta.exchangelog.auth.permission;

import com.pedrocosta.exchangelog.auth.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
//    Optional<List<Permission>> findAllByRoles(List<Role> roles);
//    Optional<List<Permission>> findAllByFkRoleId
}
