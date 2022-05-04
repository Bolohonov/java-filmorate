package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Get all users");
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User findUser(@PathVariable("userId") Integer userId) {
        log.info("Get film with ID " + userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Add new user");
        return userService.addUser(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.info("Update user");
        return userService.updateUser(user);
    }

}
