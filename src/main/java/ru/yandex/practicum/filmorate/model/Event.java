package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;

import java.time.LocalDateTime;

@Data
@Builder
public class Event {

    private int id;
    private LocalDateTime timestamp;
    private int userId;
    private EventType eventType;
    private OperationType operation;
    private int entityId;
}
