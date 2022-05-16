package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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

    public User getUserById(Integer userId) {
        log.warn("Get user with ID {}", userId);
        return userStorage.getUser(userId);
    }

    public User updateUser(User user) {
        if (validateId(user) && validateUser(user)) {
            userStorage.updateUser(user);
            log.warn("User has been updated");
        }
        return user;
    }

    public void deleteUser(Integer userId) {
        userStorage.deleteUser(userId);
        log.warn("User with ID {} has been deleted", userId);
    }

    public User addToFriends(User user, Integer friendId) {
        User friend = userStorage.getUser(friendId);
        user.addFriend(friendId);
        friend.addFriend(user.getId());
        log.warn("User with ID {} and ID {} is friends now", friendId, user.getId());
        return user;
    }

    public User removeFriend(User user, Integer friendId) {
        User friend = userStorage.getUser(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(user.getId());
        log.warn("User with ID {} and ID {} is NOT friends now", friendId, user.getId());
        return user;
    }

    public Collection<User> getUserFriends(Integer userId) {
        log.warn("User with ID {} get friends", userId);
        List<User> friends = new ArrayList<>();
        for (Integer i : userStorage.getUser(userId).getFriends()) {
            friends.add(userStorage.getUser(i));
        }
        return friends;
    }

    public Collection<Integer> getMatchingFriends(Integer id, Integer otherId) {
        log.warn("User with ID {} get matching friends with user {}", id, otherId);
        Set<Integer> intersection = new HashSet<>(userStorage.getUser(id).getFriends());
        intersection.retainAll(userStorage.getUser(otherId).getFriends());
        return intersection;

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
