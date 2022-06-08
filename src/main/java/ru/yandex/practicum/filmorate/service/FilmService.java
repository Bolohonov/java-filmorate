package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    private final LikesStorage likesStorage;
    private static final LocalDate FIRST_FILM_DATE
            = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService,
                       LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likesStorage = likesStorage;
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
        if (validateFilm(film)) {
            filmStorage.getFilmById(film.getId());
            filmStorage.updateFilm(film);
            log.warn("Film has been updated");
        }
        return film;
    }

    public void deleteFilm(Integer filmId) {
        filmStorage.deleteFilm(filmId);
        log.warn("Film with ID {} has been deleted", filmId);
    }

    public Optional<Film> getFilmById(Integer id) {
        if (!filmStorage.getFilmById(id).isPresent()) {
            throw new UserNotFoundException("Фильм не найден");
        }
        log.warn("Get user with ID {}", id);
        return filmStorage.getFilmById(id);
    }

    public Optional<Film> addLike(Integer filmId, Integer userId) {
        if (userService.getUserById(userId).isPresent() && filmStorage.getFilmById(filmId).isPresent()) {
            likesStorage.addLike(filmId, userId);
            log.warn("User {} likes film with ID {}", userId, filmId);
        }
        return filmStorage.getFilmById(filmId);
    }

    public Optional<Film> removeLike(Integer filmId, Integer userId) {
        if (userService.getUserById(userId).isPresent() && filmStorage.getFilmById(filmId).isPresent()) {
            likesStorage.removeLike(filmId, userId);
            log.warn("User {} remove like from film with ID {}", userId, filmId);
        }
        return filmStorage.getFilmById(filmId);
    }

    public Collection<Film> getFilmsByLikes(Integer count) {
        return likesStorage.getFilmsByLikes(count);
    }

    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        Optional<User> user = userService.getUserById(userId);
        Optional<User> friend = userService.getUserById(friendId);
        if (user.isPresent() && friend.isPresent()) {
            return filmStorage
                    .getCommonFilmsBetweenTwoUsers(userId, friendId)
                    .stream()
                    .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                    .collect(toList());
        }
        return new ArrayList<>();
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

    public List<Film> getFilmsByDirectorSortedByLikeOrYear(Integer directorId, String sortBy) {
        Collection<Film> films = filmStorage.findFilmsByDirectorId(directorId);
        switch (sortBy) {
            case "likes":
                return films
                        .stream()
                        .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                        .collect(toList());
            case "year":
                return films
                        .stream()
                        .sorted((o1, o2) -> o2.getReleaseDate().getYear() - o1.getReleaseDate().getYear())
                        .collect(toList());
        }
        log.warn("Кто-то пытается отсортировать фильмы режиссера не по году или лайкам");
        throw new IllegalArgumentException("Oops! Сортирует только по year или likes.");
    }
}
