package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    Optional<Director> findDirectorById(Integer id);

    List<Director> findAllDirectors();

    Director addDirector(Director director);

    Optional<Director> updateDirector(Integer id, Director director);

    boolean deleteDirector(Integer id);

}
