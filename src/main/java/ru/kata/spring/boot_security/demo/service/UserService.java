package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void saveUser(UserDto userDto); // Для создания
    void updateUser(UserDto userDto); // Для обновления
    void deleteUser(Long id);
    User findByUsername(String username);
}
