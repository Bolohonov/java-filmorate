package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

public interface EventStorage {

    void addEvent(int userId, int entityId, EventType eventType, OperationType operation);

    Collection<Event> getFeedForUser(int id);
}
