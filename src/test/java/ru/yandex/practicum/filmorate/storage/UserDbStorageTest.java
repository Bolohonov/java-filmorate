package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserStorage userDbStorage;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userDbStorage.findUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void testGetUsers() {
        Collection<User> users = userDbStorage.getUsers();
        assertThat(users).contains(userDbStorage.findUserById(1).get(), userDbStorage.findUserById(2).get(),
                userDbStorage.findUserById(3).get(), userDbStorage.findUserById(4).get(),
                userDbStorage.findUserById(5).get());
    }

    @Test
    void testAddUser() {
//        User newUser = new User(6,"Test6@yandex.ru", "Test6Login", "Test6Name",
//                LocalDate.of(2010,11,5));
//        userDbStorage.addUser(newUser);
//        assertThat(userDbStorage.findUserById(6))
//                .isPresent()
//                .isEqualTo(Optional.of(newUser));
    }

    @Test
    void testDeleteUser() {
        userDbStorage.deleteUser(5);
        assertThrows(
                UserNotFoundException.class,
                () -> userDbStorage.findUserById(5)
        );
    }

    @Test
    void testUpdateUser() {
        User newUser = userDbStorage.findUserById(1).get();
        newUser.setName("NewTestName");
        userDbStorage.updateUser(newUser);
        assertThat(userDbStorage.findUserById(1))
                .isPresent()
                .isEqualTo(Optional.of(newUser));
    }
}