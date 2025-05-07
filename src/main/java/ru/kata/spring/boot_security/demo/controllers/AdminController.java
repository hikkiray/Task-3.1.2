package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPanel(Model model) {
        try {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("allRoles", userService.getAllRoles());
            return "admin";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Произошла ошибка: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/add")
    public String addUser(@RequestParam String name,
                          @RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String email,
                          @RequestParam int age,
                          @RequestParam(required = false) List<Long> roles) {
        try {
            User user = new User();
            user.setName(name);
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setAge(age);

            userService.saveUser(user, roles);
            return "redirect:/admin";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin?error=" + e.getMessage();
        }
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam String username,
                             @RequestParam(required = false) String password,
                             @RequestParam String email,
                             @RequestParam int age,
                             @RequestParam List<Long> roles) {
        try {
            User user = userService.getUserById(id);
            user.setName(name);
            user.setUsername(username);
            if (password != null && !password.isEmpty()) {
                user.setPassword(password);
            }
            user.setEmail(email);
            user.setAge(age);
            
            Set<Role> userRoles = roles.stream()
                    .map(roleId -> userService.getAllRoles().stream()
                            .filter(role -> role.getId().equals(roleId))
                            .findFirst()
                            .orElse(null))
                    .filter(role -> role != null)
                    .collect(Collectors.toSet());
            user.setRoles(userRoles);
            
            userService.updateUser(user);
            return "redirect:/admin";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin?error=" + e.getMessage();
        }
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        try {
            userService.deleteUser(id);
            return "redirect:/admin";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin?error=" + e.getMessage();
        }
    }
}
