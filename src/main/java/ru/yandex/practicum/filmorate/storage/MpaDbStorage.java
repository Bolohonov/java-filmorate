package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SELECT_COUNT_ID = "select count(id) from mpa ";

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getNewMpaObject(Integer mpaId) {
        if (mpaId > 0 && mpaId <= this.getNumberOfMpa()) {
            log.warn("Создание нового объекта mpa");
            return new Mpa(mpaId);
        } else {
            throw new MpaNotFoundException("Такого mpa_id не существует");
        }
    }

    private int getNumberOfMpa() {
        return jdbcTemplate.queryForObject(SQL_SELECT_COUNT_ID, Integer.class);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = null;
        mpa.setId(resultSet.getInt("id"));
        return mpa;
    }
}
