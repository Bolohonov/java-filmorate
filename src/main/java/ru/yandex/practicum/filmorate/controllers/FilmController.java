package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film post(@Valid @RequestBody Film film) {
        if (validationId(film) && checkIdDuplication(film) && validation(film)) {
            films.put(film.getId(), film);
            log.info("Film has been added");
        }
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (validationId(film) && validation(film)) {
            films.put(film.getId(), film);
            log.info("Film has been updated");
        }
        return film;
    }

    private static boolean validation(Film film) {
        if(film.getDescription().length() > 200) {
            log.info("Description is too long");
            throw new ValidationException("Описание слишком длинное.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("ReleaseDate isBefore 1985.12.28");
            throw new ValidationException("Указана неверная дата релиза.");
        }
        if (film.getDuration().isNegative()) {
            log.info("Duration is negative");
            throw new ValidationException("Указана отрицательная продолжительность.");
        }
        return true;
    }

    private static boolean validationId(Film film) {
        try {
            film.getId();
        } catch (NullPointerException exp) {
            film.setId(1);
            log.info("ID has been changed");
        }
        int id = film.getId();
        if (id <= 0) {
            film.setId(1);
            log.info("ID has been changed");
        }
        return true;
    }

    private static boolean checkIdDuplication(Film film) {
        if (films.containsKey(film.getId())) {
            film.setId(film.getId() + 1);
            checkIdDuplication(film);
        }
        log.info("ID has been checked");
        return true;
    }
}
