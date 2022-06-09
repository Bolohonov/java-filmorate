package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Event {

    int id;
    LocalDateTime timestamp;
    int userId;
    String eventType;
    String operation;
    int entityId;
}
