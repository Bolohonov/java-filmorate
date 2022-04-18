package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController controller = new FilmController();

    @Test
    void postStandardBehavior() {
        Film film = new Film();
        film.setId(1);
        film.setName("TestName");
        film.setDescription("TestDescription");
        film.setReleaseDate(LocalDate.of(2015,10,5));
        film.setDuration(Duration.ofMinutes(105));
        controller.post(film);
        assertTrue(controller.findAll().contains(film));
    }

    @Test
    void postLongDescription() {
        Film film = new Film();
        film.setId(2);
        film.setName("TestName");
        String str = "Test";
        str = str.repeat(51);
        film.setDescription(str);
        film.setReleaseDate(LocalDate.of(2015,10,5));
        film.setDuration(Duration.ofMinutes(105));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.post(film)
        );
        assertEquals("Описание слишком длинное.",
                exception.getMessage());
    }

    @Test
    void postReleaseBeforeFirstDate() {
        Film film = new Film();
        film.setId(3);
        film.setName("TestName");
        film.setDescription("TestDescription");
        film.setReleaseDate(LocalDate.of(1895,10,5));
        film.setDuration(Duration.ofMinutes(105));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.post(film)
        );
        assertEquals("Указана неверная дата релиза.",
                exception.getMessage());
    }

    @Test
    void postDuplicatedId() {
        Film film = new Film();
        film.setId(4);
        film.setName("TestName");
        film.setDescription("TestDescription");
        film.setReleaseDate(LocalDate.of(2015,10,5));
        film.setDuration(Duration.ofMinutes(105));
        controller.post(film);
        Film film2 = new Film();
        film2.setId(4);
        film2.setName("TestName2");
        film2.setDescription("TestDescription2");
        film2.setReleaseDate(LocalDate.of(2016,10,5));
        film2.setDuration(Duration.ofMinutes(107));
        controller.post(film2);
        Film film3 = new Film();
        film3.setId(5);
        film3.setName("TestName2");
        film3.setDescription("TestDescription2");
        film3.setReleaseDate(LocalDate.of(2016,10,5));
        film3.setDuration(Duration.ofMinutes(107));
        assertTrue(controller.findAll().contains(film3));
    }

    @Test
    void putStandardBehavior() {
        Film film = new Film();
        film.setId(6);
        film.setName("TestName6");
        film.setDescription("TestDescription6");
        film.setReleaseDate(LocalDate.of(2016,10,5));
        film.setDuration(Duration.ofMinutes(106));
        controller.post(film);
        film.setName("New");
        controller.put(film);
        assertTrue(controller.findAll().contains(film));
    }
}