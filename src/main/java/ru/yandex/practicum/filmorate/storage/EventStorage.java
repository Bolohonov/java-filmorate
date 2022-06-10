package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

public interface EventStorage {

    void addEvent(int userId, int entityId, String eventType, String operation);

    Collection<Event> getFeedForUser(int id);
}
