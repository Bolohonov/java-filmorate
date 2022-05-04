package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
    public void deleteUser(User user) {
        users.remove(user.getId());
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
        log.info("User has been updated in storage");
    }

    @Override
    public User getUser(Integer id) {
        return users.
                entrySet()
                .stream()
                .filter(u -> u.getKey().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь № %d не найден", id)))
                .getValue();
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
