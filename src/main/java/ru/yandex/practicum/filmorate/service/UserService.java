package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        if (validateUser(user) && validateEmailNotDuplicated(user)) {
            userStorage.addUser(user);
            log.warn("User has been added");
        }
        return user;
    }

    public Optional<User> getUserById(Integer userId) {
        if (!userStorage.findUserById(userId).isPresent()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        log.warn("Get user with ID {}", userId);
        return userStorage.findUserById(userId);
    }

    public Optional<User> updateUser(User user) {
        if (validateUser(user)) {
            userStorage.findUserById(user.getId());
            userStorage.updateUser(user);
            log.warn("User has been updated");
        }
        return Optional.of(user);
    }

    public void deleteUser(Integer userId) {
        userStorage.deleteUser(userId);
        log.warn("User with ID {} has been deleted", userId);
    }

    public User addToFriends(User user, Integer friendId) {
        if (!userStorage.findUserById(friendId).isPresent()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        friendsStorage.addToFriends(user.getId(), friendId);
        if (friendsStorage.addToFriends(user.getId(), friendId)) {
            log.warn("User with ID {} and ID {} is friends now", friendId, user.getId());
        } else {
            log.warn("User with ID {} and ID {} is NOT friends yet", friendId, user.getId());
        }
        return user;
    }

    public User removeFriend(User user, Integer friendId) {
        friendsStorage.removeFriend(user.getId(), friendId);
        log.warn("User with ID {} and ID {} is NOT friends now", friendId, user.getId());
        return user;
    }

    public Collection<User> getUserFriends(Integer userId) {
        log.warn("User with ID {} get friends", userId);
        return friendsStorage.getUserFriends(userId);
    }

    public Collection<User> getMatchingFriends(Integer id, Integer otherId) {
        log.warn("User with ID {} get matching friends with user {}", id, otherId);
        return friendsStorage.getMatchingFriends(id, otherId);
    }

    private static boolean validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Whitespaces in login");
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Name is blank");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("BirthDate is incorrect");
            throw new ValidationException("Указана неверная дата рождения.");
        }
        return true;
    }

    private boolean validateEmailNotDuplicated(User user) {
        for (User u : userStorage.getUsers()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.warn("Duplicated email");
                throw new ValidationException(String.format("Пользователь с электронной почтой %s" +
                        " уже зарегистрирован.", user.getEmail()));
            }
        }
        return true;
    }

    private boolean validateId(User user) {
        if (user.getId() <= 0) {
            log.warn("ID wrong format");
            throw new ValidationException("ID должен быть положительным.");
        }
        return true;
    }
}
