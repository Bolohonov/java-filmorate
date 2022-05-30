package ru.yandex.practicum.filmorate.storage;

public interface FriendsStorage {
    void addToFriends(Integer firstUserId, Integer secondUserId);

    void removeFriend(Integer firstUserId, Integer secondUserId);

}
