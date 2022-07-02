package com.pedrocosta.exchangelog.auth.user;

import com.pedrocosta.exchangelog.auth.role.Role;
import com.pedrocosta.exchangelog.auth.role.RoleRepository;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.output.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User save(User user) throws SaveDataException {
        return userRepository.save(user);
    }

    @Override
    public List<User> saveAll(Collection<User> col) throws SaveDataException {
        return userRepository.saveAll(col);
    }

    @Override
    public User find(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User find(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(User user, Role role) {
        Log.info(this, String.format("Adding role %s to user %s", role.getName(), user.getUsername()));
        user.getRoles().add(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        this.addRoleToUser(user, role);
    }
}
