package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikesStorage likesStorage;
    private final EventStorage eventStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    private static final LocalDate FIRST_FILM_DATE
            = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService,
                       LikesStorage likesStorage, EventStorage eventStorage, MpaStorage mpaStorage,
                       GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likesStorage = likesStorage;
        this.eventStorage = eventStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
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
        return filmStorage.getFilmById(id);
    }

    public Optional<Film> addLike(Integer filmId, Integer userId) {
        if (userService.getUserById(userId).isPresent() && filmStorage.getFilmById(filmId).isPresent()) {
            likesStorage.addLike(filmId, userId);
            eventStorage.addEvent(userId, filmId, EventType.LIKE, OperationType.ADD);
            log.warn("User {} likes film with ID {}", userId, filmId);
        }
        return filmStorage.getFilmById(filmId);
    }

    public Optional<Film> removeLike(Integer filmId, Integer userId) {
        if (userService.getUserById(userId).isPresent() && filmStorage.getFilmById(filmId).isPresent()) {
            likesStorage.removeLike(filmId, userId);
            eventStorage.addEvent(userId, filmId, EventType.LIKE, OperationType.REMOVE);
            log.warn("User {} remove like from film with ID {}", userId, filmId);
        }
        return filmStorage.getFilmById(filmId);
    }

    public Collection<Film> getFilmsByLikes(Integer count, Integer genreId, Integer year) {
        return likesStorage.getFilmsByLikes(count, genreId, year);
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


    public Collection<Film> search(String query, String by) {
            return filmStorage.search(query, by);
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

    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Optional<Mpa> getMpaById(Integer mpaId) {
        if (mpaId > 0 && mpaId <= mpaStorage.getAllMpa().size()) {
            log.warn("Получение объекта mpa");
            return mpaStorage.getMpaById(mpaId);
        } else {
            throw new MpaNotFoundException("Такого mpa_id не существует");
        }
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        log.warn("Получение genre");
        return genreStorage.getGenreById(genreId);
    }
}
