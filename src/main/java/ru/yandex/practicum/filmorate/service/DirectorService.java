package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorDbStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorDbStorage directorDbStorage;

    public Director createDirector(@Valid Director director) {
        return directorDbStorage.addDirector(director);
    }

    public Optional<Director> findDirectorById(Integer id) {
        return directorDbStorage.findDirectorById(id);
    }

    public Optional<Director> updateDirector(Integer id, @Valid Director director) {
        return directorDbStorage.findDirectorById(id)
                .map(dir -> directorDbStorage.updateDirector(id, director))
                .orElseThrow();
    }

    public List<Director> findAllDirectors() {
        return directorDbStorage.findAllDirectors();
    }

    public boolean deleteDirector(Integer id) {
        return directorDbStorage.deleteDirector(id);
    }
}
