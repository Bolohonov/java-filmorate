package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getUsers();

    User addUser(User user);

    void deleteUser(Integer id);

    User updateUser(User user);

    Optional<User> findUserById(Integer id);
}
