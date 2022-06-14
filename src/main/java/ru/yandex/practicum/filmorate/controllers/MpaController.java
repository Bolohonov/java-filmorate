package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final FilmService filmService;

    @Autowired
    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Mpa> findAll() {
        log.info("Get all mpa");
        return filmService.getAllMpa();
    }

    @GetMapping("/{mpaId}")
    public Optional<Mpa> findMpa(@PathVariable("mpaId") Integer mpaId) {
        log.info("Get MPA with ID {}", mpaId);
        return filmService.getMpaById(mpaId);
    }

}
