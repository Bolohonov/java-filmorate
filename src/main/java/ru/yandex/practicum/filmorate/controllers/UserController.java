package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

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
        log.warn("Get all users");
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public Optional<User> findUser(@PathVariable("userId") Integer userId) {
        log.warn("Get user with ID {}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        userService.deleteUser(userId);
        log.warn("Delete user with ID {}", userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        log.warn("Add friend with ID {}", friendId);
        return userService.addToFriends(userService.getUserById(id).get(), friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        log.warn("Remove friend with ID {}", friendId);
        return userService.removeFriend(userService.getUserById(id).get(), friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable("id") Integer id) {
        log.warn("Get friends of user with ID {}", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMatchingFriends(@PathVariable("id") Integer id,
                                               @PathVariable("otherId") Integer otherId) {
        log.warn("Get matching friends of user with ID {} and {}", id, otherId);
        return userService.getMatchingFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.warn("Add new user");
        return userService.addUser(user);
    }

    @PutMapping
    public Optional<User> put(@Valid @RequestBody User user) {
        log.warn("Update user");
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}/recommendations")
    public Collection<Film> getRecommendations(@PathVariable("userId") Integer userId) {
        log.warn("Add recommendations to user ID {}", userId);
        return userService.getRecommendations(userId);
    }
}
