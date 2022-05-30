package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Integer userId, Integer filmId) {
        String sqlQuery = "insert into likes (user_id, film_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                filmId);
    }

    @Override
    public void removeLike(Integer userId, Integer filmId) {
        String sqlQuery = "delete from likes where user_id = ? AND film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }
}
