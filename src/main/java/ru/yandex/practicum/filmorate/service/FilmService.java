package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private static final LocalDate FIRST_FILM_DATE
            = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        if (validateFilm(film)) {
            filmStorage.addFilm(film);
            log.warn("Film has been added");
        }
        return film;
    }

    public Film updateFilm(Film film) {
        if (validateId(film) && validateFilm(film)) {
            filmStorage.updateFilm(film);
            log.warn("Film has been updated");
        }
        return film;
    }

    public void deleteFilm(Integer filmId) {
        filmStorage.deleteFilm(filmId);
        log.warn("Film with ID {} has been deleted", filmId);
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilm(id);
    }

    public Film addLike(Integer filmId, Integer userId) {
        if (userService.getUserById(userId) != null) {
            filmStorage.getFilm(filmId).addLike(userId);
            log.warn("User {} likes film with ID {}", userId, filmId);
        }
        return filmStorage.getFilm(filmId);
    }

    public Film removeLike(Integer filmId, Integer userId) {
        if (userService.getUserById(userId) != null) {
            filmStorage.getFilm(filmId).removeLike(userId);
            log.warn("User {} remove like from film with ID {}", userId, filmId);
        }
        return filmStorage.getFilm(filmId);
    }

    public Collection<Film> getFilmsByLikes(Integer count) {
        List<Film> filmsByLikes = filmStorage.getFilms()
                .stream()
                .sorted((o1, o2) -> o2.getRate() - o1.getRate())
                .limit(count)
                .collect(Collectors.toList());
        return filmsByLikes;
    }

    private boolean validateFilm(Film film) {
        if (film.getDescription().length() > 200) {
            log.warn("Description is too long");
            throw new ValidationException("Описание слишком длинное.");
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            log.warn("ReleaseDate isBefore 1985.12.28");
            throw new ValidationException("Указана неверная дата релиза.");
        }
        if (film.getDuration().isNegative()) {
            log.warn("Duration is negative");
            throw new ValidationException("Указана отрицательная продолжительность.");
        }
        return true;
    }

    private boolean validateId(Film film) {
        if (film.getId() <= 0) {
            log.warn("ID wrong format");
            throw new ValidationException("ID должен быть положительным.");
        }
        return true;
    }
}
