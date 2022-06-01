package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Test
    void testGetFilms() {
        Collection<Film> films = filmDbStorage.getFilms();
        assertThat(films).contains(filmDbStorage.getFilmById(10).get(), filmDbStorage.getFilmById(20).get(),
                filmDbStorage.getFilmById(30).get(), filmDbStorage.getFilmById(40).get(),
                filmDbStorage.getFilmById(50).get());
    }

    @Test
    void testAddFilm() {
        Film newFilm = Film.builder()
                .description("Test0Desc")
                .mpa(mpaDbStorage.getNewMpaObject(3))
                .rate(5)
                .name("Test")
                .releaseDate(LocalDate.of(2010,11,11))
                .duration(Duration.ofSeconds(500))
                .build();
        filmDbStorage.addFilm(newFilm);
        assertThat(filmDbStorage.getFilmById(1))
                .isPresent()
                .isEqualTo(Optional.of(newFilm));
    }

    @Test
    void testDeleteFilm() {
        filmDbStorage.deleteFilm(50);
        assertThrows(
                FilmNotFoundException.class,
                () -> filmDbStorage.getFilmById(50)
        );
    }

    @Test
    void testUpdateFilm() {
        Film newFilm = filmDbStorage.getFilmById(10).get();
        newFilm.setName("NewTestFilm");
        filmDbStorage.updateFilm(newFilm);
        assertThat(filmDbStorage.getFilmById(10))
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