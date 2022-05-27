package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    @Override
    public Collection<User> getUsers() {
        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public User getUser(Integer id) {
        return null;
    }
}
