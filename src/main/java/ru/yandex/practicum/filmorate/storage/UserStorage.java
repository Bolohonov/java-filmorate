package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getUsers();

    User addUser(User user);

    void deleteUser(Integer id);

    void updateUser(User user);

    User getUser(Integer id);
}
