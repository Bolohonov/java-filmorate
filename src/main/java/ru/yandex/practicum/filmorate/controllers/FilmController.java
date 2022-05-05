package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Get all films");
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable("filmId") Integer filmId) {
        log.info("Get film with ID " + filmId);
        return filmService.getFilmById(filmId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable("filmId") Integer filmId) {
        filmService.deleteFilm(filmId);
        log.info("Delete film with ID " + filmId);
    }

    @PostMapping
    public Film post(@Valid @RequestBody Film film) {
        log.info("Add new film");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        log.info("Update film");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Add like to film with ID " + id);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Remove like from film with ID " + id);
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getFilmsByLikes(@RequestParam(value = "count", defaultValue = "10",
            required = false) Integer count) {
        if (count <= 0) {
            throw new IllegalArgumentException();
        }
        return filmService.getFilmsByLikes(count);
    }
}
