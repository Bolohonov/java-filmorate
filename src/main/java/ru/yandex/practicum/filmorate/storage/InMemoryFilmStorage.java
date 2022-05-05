package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int id;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(appointId());
        films.put(film.getId(), film);
        log.info("Film has been added to storage");
        return film;
    }

    @Override
    public void deleteFilm(Integer filmId) {
        if (films.containsKey(filmId)) {
            films.remove(filmId);
            log.info("Film with ID %d has been deleted", filmId);
        } else {
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", filmId));
        }
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Film has been updated in storage");
    }

    @Override
    public Film getFilm(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", id));
        }
    }

    private boolean checkIdNotDuplicated(int id) {
        if (films.containsKey(id)) {
            log.info("Id exists");
            throw new ValidationException("ID уже существует.");
        }
        log.info("ID has been checked");
        return true;
    }

    private int appointId() {
        ++id;
        if ((this.checkIdNotDuplicated(id)) && (id != 0)) {
            return id;
        } else {
            return appointId();
        }
    }
}
