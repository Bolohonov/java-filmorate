package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if(validationId(user) && validationEmail(user) && validation(user)) {
            users.put(user.getId(), user);
            log.info("User has been added");
        }
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if(validation(user)) {
            users.put(user.getId(), user);
            log.info("User has been updated");
        }
        return user;
    }

    private static boolean validation(User user) {
        if (user.getLogin().contains(" ")) {
            log.info("Whitespaces in login");
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Name is blank");
            user.setName(user.getLogin());
        }
        if (user.getBirthDate().isAfter(LocalDate.now())) {
            log.info("BirthDate is incorrect");
            throw new ValidationException("Указана неверная дата рождения.");
        }
        return true;
    }

    private static boolean validationEmail(User user) {
        for (User u : users.values()) {
            if(u.getEmail().equals(user.getEmail())) {
                log.info("Duplicated email");
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
        }
        return true;
    }

    private static boolean validationId(User user) {
        int id = user.getId();
        if (users.containsKey(user.getId())) {
            user.setId(id + 1);
            validationId(user);
        }
        log.info("ID has been checked");
        return true;
    }
}
