package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {
        user.setId(appointId());
        users.put(user.getId(), user);
        log.warn("User has been added to storage");
        return user;
    }

    @Override
    public void deleteUser(Integer userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            log.warn("User with ID {} has been deleted", userId);
        } else {
            throw new UserNotFoundException(String.format("Пользователь № %d не найден", userId));
        }
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
        log.warn("User has been updated in storage");
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
        if (!users.containsKey(id)) {
            log.warn("ID has been checked");
            return true;
        } else {
            log.warn("Id exists");
            return false;
        }
    }

    private int appointId() {
        while (!this.checkIdNotDuplicated(id)) {
            ++id;
        }
        return id;
    }
}
