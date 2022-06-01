package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendsDbStorageTest {
    private final FriendsStorage friendsDbStorage;
    private final UserStorage userDbStorage;

    @Test
    void addToFriends() {
        friendsDbStorage.addToFriends(4, 1);
        assertThat(friendsDbStorage.getUserFriends(4).contains(userDbStorage.findUserById(1)));
        assertThat(!friendsDbStorage.getUserFriends(1).contains(userDbStorage.findUserById(4)));
    }

    @Test
    void removeFriend() {
        friendsDbStorage.removeFriend(2, 4);
        assertThat(!friendsDbStorage.getUserFriends(2).contains(userDbStorage.findUserById(4)));
    }

    @Test
    void getUserFriends() {
        Collection<User> userFriends = friendsDbStorage.getUserFriends(2);
        assertThat(userFriends).contains(
                userDbStorage.findUserById(3).get(),
                userDbStorage.findUserById(4).get(),
                userDbStorage.findUserById(5).get()
        );
        assertThat(userFriends).doesNotContain(
                userDbStorage.findUserById(1).get(),
                userDbStorage.findUserById(2).get()
        );
    }

    @Test
    void getMatchingFriends() {
        Collection<User> userFriends = friendsDbStorage.getMatchingFriends(2, 5);
        assertThat(userFriends).contains(
                userDbStorage.findUserById(4).get()
        );
        assertThat(userFriends).doesNotContain(
                userDbStorage.findUserById(2).get(),
                userDbStorage.findUserById(5).get(),
                userDbStorage.findUserById(1).get(),
                userDbStorage.findUserById(3).get()
        );
    }
}