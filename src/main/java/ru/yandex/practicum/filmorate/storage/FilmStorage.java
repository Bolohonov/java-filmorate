package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getFilms();

    void addFilm(Film film);

    void deleteFilm(Film film);

    void updateFilm(Film film);
}
