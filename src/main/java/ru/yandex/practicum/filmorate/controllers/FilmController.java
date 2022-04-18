package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
        if (validationId(film) && validation(film)) {
            films.put(film.getId(), film);
            log.info("Film has been added");
        }
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (validation(film)) {
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
        return true;
    }

    private static boolean validationId(Film film) {
        int id = film.getId();
        if (films.containsKey(film.getId())) {
            film.setId(id + 1);
            validationId(film);
        }
        log.info("ID has been checked");
        return true;
    }
}
