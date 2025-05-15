package ru.kata.spring.boot_security.demo.dto;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserMapper(RoleService roleService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());

        if (userDto.getRoleIds() != null) {
            Set<Role> roles = userDto.getRoleIds().stream()
                    .map(roleService::findById)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        return user;
    }

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setRoles(user.getRoles());
        dto.setRoleIds(user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toList()));
        return dto;
    }
}
