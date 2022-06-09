package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getFilms();

    Film addFilm(Film film);

    void deleteFilm(Integer id);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(Integer id);

    Collection<Film> search(String query);

    Collection<Film> findFilmsByDirectorId(Integer directorId);

    Collection<Film> getCommonFilmsBetweenTwoUsers(Integer userId, Integer friendId);

}
