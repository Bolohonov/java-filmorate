package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SELECT_ALL = "select * from mpa ";
    private static final String SQL_SELECT_MPA_BY_ID = "select * from mpa where id = ?";

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return jdbcTemplate.query(SQL_SELECT_ALL, this::mapRowToMpa);
    };

    @Override
    public Optional<Mpa> getMpaById(Integer mpaId) {
        Optional<Mpa> mpa;
        try {
            mpa = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_MPA_BY_ID,
                    this::mapRowToMpa, mpaId));
        } catch (Exception exp) {
            log.warn("Mpa с id {} не найден", mpaId);
            throw new FilmNotFoundException(exp.getMessage());
        }
        return mpa;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("id"), resultSet.getString("name"));
    }
}
