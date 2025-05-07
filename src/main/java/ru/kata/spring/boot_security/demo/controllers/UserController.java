package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserPage(Model model, Authentication authentication) {
        // Получаем текущего аутентифицированного пользователя
        User currentUser = (User) authentication.getPrincipal();

        // Для админа может быть передан ID пользователя через параметр
        // Если нет - показываем данные текущего пользователя
        Long userId = currentUser.getId();

        // Получаем актуальные данные пользователя из БД
        User user = userService.getUserById(userId);

        model.addAttribute("user", user);
        return "user"; // имя шаблона user.html
    }
}