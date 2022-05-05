package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {
        user.setId(appointId());
        users.put(user.getId(), user);
        log.info("User has been added to storage");
        return user;
    }

    @Override
    public void deleteUser(Integer userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            log.info("User with ID %d has been deleted", userId);
        } else {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", userId));
        }
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
        log.info("User has been updated in storage");
    }

    @Override
    public User getUser(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", id));
        }
    }

    private boolean checkIdNotDuplicated(int id) {
        if (users.containsKey(id)) {
            log.info("Id exists");
            throw new ValidationException("ID уже существует.");
        }
        log.info("ID has been checked");
        return true;
    }

    private int appointId() {
        ++id;
        if ((this.checkIdNotDuplicated(id)) && (id != 0)) {
            return id;
        } else {
            return appointId();
        }
    }
}
