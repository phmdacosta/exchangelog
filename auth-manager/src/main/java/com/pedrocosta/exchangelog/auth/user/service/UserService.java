package com.pedrocosta.exchangelog.auth.user.service;

import com.pedrocosta.exchangelog.auth.role.Role;
import com.pedrocosta.exchangelog.auth.user.User;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    /**
     * Create or update a user in database
     * @return  Saved user
     * @throws SaveDataException    Exception throw when something
     *                              goes wrong with business saving validation
     */
    User save (User user) throws SaveDataException;
    User find(long id) throws NotFoundException;

    /**
     * Find all users in database
     * @return List of users
     */
    List<User> findAll();

    /**
     * Find a user by its username
     * @throws NotFoundException    Throw if it doesn't exist
     */
    User find(String username) throws NotFoundException;
    Role saveRole(Role role) throws SaveDataException;

    /**
     * Add a role to a user
     * @throws SaveDataException    Exception throw when something
     *                              goes wrong with business saving validation
     */
    void addRoleToUser(User user, Role role) throws SaveDataException;
    void addRoleToUser(String username, String roleName) throws SaveDataException;

    /**
     * Register a new user
     * @return Generated token to be confirmed by user
     * @throws SaveDataException    Exception throw when something
     *                              goes wrong with business saving validation
     */
    String register(User user) throws SaveDataException, IllegalArgumentException;

    /**
     * Do confirmation of token by user
     * @throws SaveDataException    Exception throw when something
     *                              goes wrong with business saving validation
     */
    void confirmToken(String token) throws SaveDataException, IllegalArgumentException;
}
