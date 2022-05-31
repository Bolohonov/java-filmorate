package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

public interface MpaStorage {
    Mpa getMpaById(Integer id);
}
