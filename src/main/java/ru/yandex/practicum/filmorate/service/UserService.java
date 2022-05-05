package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
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
            log.info("User has been added");
        }
        return user;
    }

    public User getUserById(Integer userId) {
        log.info("Get user with ID " + userId);
        return userStorage.getUser(userId);
    }

    public User updateUser(User user) {
        if (validateId(user) && validateUser(user)) {
            userStorage.updateUser(user);
            log.info("User has been updated");
        }
        return user;
    }

    public User addToFriends(User user, Integer friendId) {
        User friend = userStorage.getUser(friendId);
        user.addFriend(friendId);
        friend.addFriend(user.getId());
        log.info("User with ID " + friendId + " and ID " + user.getId() + " is friends now");
        return user;
    }

    public User removeFriend(User user, Integer friendId) {
        User friend = userStorage.getUser(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(user.getId());
        log.info("User with ID " + friendId + " and ID " + user.getId() + " is NOT friends now");
        return user;
    }

    public Collection<User> getUserFriends(Integer userId) {
        log.info("User with ID " + userId + " get friends");
        List<User> friends = new ArrayList<>();
        for (Integer i : userStorage.getUser(userId).getFriends()) {
            friends.add(userStorage.getUser(i));
        }
        return friends;
    }

    public Collection<Integer> getMatchingFriends(Integer id, Integer otherId) {
        log.info("User with ID " + id + " get matching friends with user " + otherId);
        Set<Integer> intersection = new HashSet<>(userStorage.getUser(id).getFriends());
        intersection.retainAll(userStorage.getUser(otherId).getFriends());
        return intersection;

    }

    private static boolean validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.info("Whitespaces in login");
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Name is blank");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("BirthDate is incorrect");
            throw new ValidationException("Указана неверная дата рождения.");
        }
        return true;
    }

    private boolean validateEmailNotDuplicated(User user) {
        for (User u : userStorage.getUsers()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.info("Duplicated email");
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
        }
        return true;
    }

    private boolean validateId(User user) {
        if (user.getId() <= 0) {
            log.info("ID wrong format");
            throw new ValidationException("ID должен быть положительным.");
        }
        return true;
    }
}
