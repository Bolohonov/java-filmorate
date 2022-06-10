package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendsDbStorageTest {
    private final FriendsStorage friendsDbStorage;
    private final UserStorage userDbStorage;

    @Test
    void addToFriends() {
        friendsDbStorage.addToFriends(1004, 1001);
        assertThat(friendsDbStorage.getUserFriends(1004).contains(userDbStorage.findUserById(1001)));
        assertThat(!friendsDbStorage.getUserFriends(1001).contains(userDbStorage.findUserById(1004)));
    }

    @Test
    void removeFriend() {
        friendsDbStorage.removeFriend(1005, 1004);
        assertThat(!friendsDbStorage.getUserFriends(1001).contains(userDbStorage.findUserById(1004)));
    }

    @Test
    void getUserFriends() {
        Collection<User> userFriends = friendsDbStorage.getUserFriends(1002);
        assertThat(userFriends).contains(
                userDbStorage.findUserById(1003).get(),
                userDbStorage.findUserById(1004).get(),
                userDbStorage.findUserById(1005).get()
        );
        assertThat(userFriends).doesNotContain(
                userDbStorage.findUserById(1001).get(),
                userDbStorage.findUserById(1002).get()
        );
    }

    @Test
    void getMatchingFriends() {
        Collection<User> userFriends = friendsDbStorage.getMatchingFriends(1001, 1002);
        assertTrue(userFriends.contains(
                userDbStorage.findUserById(1003).get()
        ));
        assertThat(userFriends).doesNotContain(
                userDbStorage.findUserById(1001).get(),
                userDbStorage.findUserById(1002).get(),
                userDbStorage.findUserById(1004).get(),
                userDbStorage.findUserById(1005).get()
        );
    }
}