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

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sqlQuery = "select id " +
                "from mpa where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
    }

    @Override
    public Mpa getNewMpaObject(Integer id) {
        if (id >0 && id <= this.getNumberOfMpa()) {
            return new Mpa(id);
        } else {
            throw new MpaNotFoundException("Такого mpa_id не существует");
        }
    }

    private int getNumberOfMpa() {
        String sqlQuery = "select count(id) " +
                "from mpa ";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = null;
        mpa.setId(resultSet.getInt("id"));
        return mpa;
    }
}
