package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Likes {
    private int userId;
    private int filmId;

    public Likes (Integer userId, Integer filmId) {
        this.userId = userId;
        this.filmId = filmId;
    }
}
