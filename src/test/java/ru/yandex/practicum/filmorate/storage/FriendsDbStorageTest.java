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
        friendsDbStorage.addToFriends(40, 10);
        assertThat(friendsDbStorage.getUserFriends(40).contains(userDbStorage.findUserById(10)));
        assertThat(!friendsDbStorage.getUserFriends(10).contains(userDbStorage.findUserById(40)));
    }

    @Test
    void removeFriend() {
        friendsDbStorage.removeFriend(50, 40);
        assertThat(!friendsDbStorage.getUserFriends(10).contains(userDbStorage.findUserById(40)));
    }

    @Test
    void getUserFriends() {
        Collection<User> userFriends = friendsDbStorage.getUserFriends(20);
        assertThat(userFriends).contains(
                userDbStorage.findUserById(30).get(),
                userDbStorage.findUserById(40).get(),
                userDbStorage.findUserById(50).get()
        );
        assertThat(userFriends).doesNotContain(
                userDbStorage.findUserById(10).get(),
                userDbStorage.findUserById(20).get()
        );
    }

    @Test
    void getMatchingFriends() {
        Collection<User> userFriends = friendsDbStorage.getMatchingFriends(10, 20);
        assertTrue(userFriends.contains(
                userDbStorage.findUserById(30).get()
        ));
        assertThat(userFriends).doesNotContain(
                userDbStorage.findUserById(10).get(),
                userDbStorage.findUserById(20).get(),
                userDbStorage.findUserById(40).get(),
                userDbStorage.findUserById(50).get()
        );
    }
}