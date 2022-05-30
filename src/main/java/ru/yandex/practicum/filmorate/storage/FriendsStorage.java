package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendsStorage {
    boolean addToFriends(Integer firstUserId, Integer secondUserId);

    void removeFriend(Integer firstUserId, Integer secondUserId);

    Collection<User> getUserFriends(Integer userId);

    Collection<User> getMatchingFriends(Integer id, Integer otherId);

}
