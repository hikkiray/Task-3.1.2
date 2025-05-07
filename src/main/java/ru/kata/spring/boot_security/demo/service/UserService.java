package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void saveUser(User user, List<Long> roleIds);
    void updateUser(User user);
    void updateUser(Long id, String name, String username, String password, String email, int age);
    void deleteUser(Long id);
    List<Role> getAllRoles();
    void saveRole(Role role);
    User findByUsername(String username);
}
