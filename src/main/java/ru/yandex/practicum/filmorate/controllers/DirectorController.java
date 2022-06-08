package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Director findDirectorById(@PathVariable Integer id) {
        return directorService.findDirectorById(id)
                .orElseThrow(() -> {
                    log.warn("director with id {} not found", id);
                    throw new ResponseStatusException(NOT_FOUND);
                });
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<Director> findAllDirectors() {
        return directorService.findAllDirectors();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Director addDirector(@RequestBody Director director) {
        return directorService.createDirector(director);
    }

    @PutMapping
    @ResponseStatus(OK)
    public Director updateDirector(@RequestBody Director director) {
        return directorService.updateDirector(director).orElseThrow(() -> {
            log.warn("director with id {} not found for updating", director.getId());
            throw new ResponseStatusException(NOT_FOUND);
        });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteDirector(@PathVariable Integer id) {
        if (!directorService.deleteDirector(id)) {
            log.warn("director with id {} not found for updating", id);
            throw new ResponseStatusException(NOT_FOUND);
        }
    }
}