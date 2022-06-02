package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserStorage userDbStorage;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userDbStorage.findUserById(10);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 10)
                );
    }

    @Test
    void testGetUsers() {
        Collection<User> users = userDbStorage.getUsers();
        assertThat(users).contains(userDbStorage.findUserById(10).get(), userDbStorage.findUserById(20).get(),
                userDbStorage.findUserById(30).get(), userDbStorage.findUserById(40).get(),
                userDbStorage.findUserById(50).get());
    }

    @Test
    void testAddUser() throws Exception {
        User newUser = User.builder()
                .email("Test6@yandex.ru")
                .login("Test6Login")
                .name("Test6Name")
                .birthday(LocalDate.of(2010, 11, 5))
                .build();
        userDbStorage.addUser(newUser);
        assertThat(userDbStorage.findUserById(1))
                .isPresent()
                .isEqualTo(Optional.of(newUser));
    }

    @Test
    void testDeleteUser() {
        userDbStorage.deleteUser(60);
        assertThrows(
                UserNotFoundException.class,
                () -> userDbStorage.findUserById(60)
        );
    }

    @Test
    void testUpdateUser() {
        User newUser = userDbStorage.findUserById(10).get();
        newUser.setName("NewTestName");
        userDbStorage.updateUser(newUser);
        assertThat(userDbStorage.findUserById(10))
                .isPresent()
                .isEqualTo(Optional.of(newUser));
    }
}