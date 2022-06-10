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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserStorage userDbStorage;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userDbStorage.findUserById(1001);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1001)
                );
    }

    @Test
    void testGetUsers() {
        Collection<User> users = userDbStorage.getUsers();
        assertThat(users).contains(userDbStorage.findUserById(1001).get(), userDbStorage.findUserById(1002).get(),
                userDbStorage.findUserById(1003).get(), userDbStorage.findUserById(1004).get(),
                userDbStorage.findUserById(1005).get());
    }

    @Test
    void testAddUser() throws Exception {
        User newUser = User.builder()
                .email("ForAdd@yandex.ru")
                .login("Test6Login")
                .name("Test6Name")
                .birthday(LocalDate.of(2010, 11, 5))
                .build();
        userDbStorage.addUser(newUser);
        assertThat(userDbStorage.getUsers().contains(newUser));
    }

    @Test
    void testDeleteUser() {
        userDbStorage.deleteUser(1006);
        assertThrows(
                UserNotFoundException.class,
                () -> userDbStorage.findUserById(1006)
        );
    }

    @Test
    void testUpdateUser() {
        Optional<User> userOptional = userDbStorage.findUserById(1001);
        userOptional.get().setName("NewUpdateName");
        userDbStorage.updateUser(userOptional.get());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "NewUpdateName")
                );
    }
}