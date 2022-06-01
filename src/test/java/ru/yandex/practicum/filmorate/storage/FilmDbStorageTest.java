package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmStorage filmDbStorage;

    @Test
    void testGetFilms() {
        Collection<Film> films = filmDbStorage.getFilms();
        assertThat(films).contains(filmDbStorage.getFilmById(1).get(), filmDbStorage.getFilmById(2).get(),
                filmDbStorage.getFilmById(3).get(), filmDbStorage.getFilmById(4).get(),
                filmDbStorage.getFilmById(5).get());
    }

    @Test
    void testAddFilm() {
    }

    @Test
    void testDeleteFilm() {
        filmDbStorage.deleteFilm(5);
        assertThrows(
                FilmNotFoundException.class,
                () -> filmDbStorage.getFilmById(5)
        );
    }

    @Test
    void testUpdateFilm() {
        Film newFilm = filmDbStorage.getFilmById(1).get();
        newFilm.setName("NewTestFilm");
        filmDbStorage.updateFilm(newFilm);
        assertThat(filmDbStorage.getFilmById(1))
                .isPresent()
                .isEqualTo(Optional.of(newFilm));
    }

    @Test
    void testGetFilmById() {
        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }
}