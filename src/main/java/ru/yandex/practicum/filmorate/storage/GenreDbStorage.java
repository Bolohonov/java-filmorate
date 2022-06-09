package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SELECT_ALL = "select * from genre ";
    private static final String SQL_SELECT_GENRE_BY_ID = "select * from genre where id = ?";

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return jdbcTemplate.query(SQL_SELECT_ALL, this::mapRowToGenre);
    };

    @Override
    public Optional<Genre> getGenreById(Integer genreId) {
        Optional<Genre> genre;
        try {
            genre = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_GENRE_BY_ID,
                    this::mapRowToGenre, genreId));
        } catch (Exception exp) {
            log.warn("Genre с id {} не найден", genreId);
            throw new GenreNotFoundException(exp.getMessage());
        }
        return genre;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
