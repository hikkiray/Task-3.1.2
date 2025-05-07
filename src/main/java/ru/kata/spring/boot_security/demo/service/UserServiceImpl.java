package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           RoleDao roleDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional
    public void saveUser(User user, List<Long> roleIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = roleIds.stream()
                .map(roleDao::findById)
                .collect(Collectors.toSet());
        user.setRoles(roles);

        userDao.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        User existingUser = userDao.findById(user.getId());
        if (!user.getPassword().isEmpty() && !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }
        user.setRoles(existingUser.getRoles());
        userDao.update(user);
    }
    @Override
    @Transactional
    public void updateUser(Long id, String name, String username,
                           String password, String email, int age) {
        User existingUser = userDao.findById(id);
        existingUser.setName(name);
        existingUser.setUsername(username);
        existingUser.setEmail(email);
        existingUser.setAge(age);

        if (password != null && !password.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(password));
        }

        userDao.update(existingUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        userDao.delete(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        roleDao.save(role);
    }

    @PostConstruct
    @Transactional
    public void init() {
        if (userDao.findByUsername("admin") == null) {
            Role adminRole = roleDao.findAll().stream()
                    .filter(role -> role.getName().equals("ROLE_ADMIN"))
                    .findFirst()
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_ADMIN");
                        roleDao.save(role);
                        return role;
                    });

            Role userRole = roleDao.findAll().stream()
                    .filter(role -> role.getName().equals("ROLE_USER"))
                    .findFirst()
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_USER");
                        roleDao.save(role);
                        return role;
                    });

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setName("Admin");
            admin.setEmail("admin@mail.ru");
            admin.setAge(30);

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
            admin.setRoles(roles);

            userDao.save(admin);
            String encoded = passwordEncoder.encode("admin");
            System.out.println("Encoded password for admin: " + encoded);
            System.out.println("Matches: " + passwordEncoder.matches("admin", encoded));
        }
    }
    // Шифр паролей

}
