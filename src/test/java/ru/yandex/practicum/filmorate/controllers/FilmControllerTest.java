//package ru.yandex.practicum.filmorate.controllers;
//
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.exceptions.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.service.FilmService;
//
//import java.time.Duration;
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class FilmControllerTest {
//    private final FilmService
//    private static final FilmController controller = new FilmController();
//
//    @Test
//    void postStandardBehavior() {
//        Film film = new Film();
//        film.setName("TestName");
//        film.setDescription("TestDescription");
//        film.setReleaseDate(LocalDate.of(2015, 10, 5));
//        film.setDuration(Duration.ofMinutes(105));
//        controller.post(film);
//        assertTrue(controller.findAll().contains(film));
//    }
//
//    @Test
//    void postLongDescription() {
//        Film film = new Film();
//        film.setName("TestName");
//        String str = "Test";
//        str = str.repeat(51);
//        film.setDescription(str);
//        film.setReleaseDate(LocalDate.of(2015, 10, 5));
//        film.setDuration(Duration.ofMinutes(105));
//        final ValidationException exception = assertThrows(
//                ValidationException.class,
//                () -> controller.post(film)
//        );
//        assertEquals("Описание слишком длинное.",
//                exception.getMessage());
//    }
//
//    @Test
//    void postReleaseBeforeFirstDate() {
//        Film film = new Film();
//        film.setName("TestName");
//        film.setDescription("TestDescription");
//        film.setReleaseDate(LocalDate.of(1895, 10, 5));
//        film.setDuration(Duration.ofMinutes(105));
//        final ValidationException exception = assertThrows(
//                ValidationException.class,
//                () -> controller.post(film)
//        );
//        assertEquals("Указана неверная дата релиза.",
//                exception.getMessage());
//    }
//
//    @Test
//    void putStandardBehavior() {
//        Film film = new Film();
//        film.setName("TestName6");
//        film.setDescription("TestDescription6");
//        film.setReleaseDate(LocalDate.of(2016, 10, 5));
//        film.setDuration(Duration.ofMinutes(106));
//        controller.post(film);
//        film.setName("New");
//        controller.put(film);
//        assertTrue(controller.findAll().contains(film));
//    }
//
//    @Test
//    void putWrongId() {
//        Film film = new Film();
//        film.setName("TestName6");
//        film.setDescription("TestDescription6");
//        film.setReleaseDate(LocalDate.of(2016, 10, 5));
//        film.setDuration(Duration.ofMinutes(106));
//        controller.post(film);
//        film.setId(-5);
//        final ValidationException exception = assertThrows(
//                ValidationException.class,
//                () -> controller.put(film)
//        );
//        assertEquals("ID должен быть положительным.",
//                exception.getMessage());
//    }
//}