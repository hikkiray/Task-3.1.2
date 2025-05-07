package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import java.util.List;

public interface RoleDao {
    Role findById(Long id);
    List<Role> findAll();
    void save(Role role);
}