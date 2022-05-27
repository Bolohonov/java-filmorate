package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Override
    public Collection<Film> getFilms() {
        return null;
    }

    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public void deleteFilm(Integer id) {

    }

    @Override
    public void updateFilm(Film film) {

    }

    @Override
    public Film getFilm(Integer id) {
        return null;
    }
}
