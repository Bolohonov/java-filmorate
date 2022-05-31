package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collection;

@Slf4j
@Component
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaDbStorage;

    public LikesDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        if (!this.isLLikeExist(filmId, userId)) {
            String sqlQuery = "insert into likes (film_id, user_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    filmId,
                    userId);
        } else {
            log.warn("Пользователь с id {} уже поставил лайк фильму с id", userId, filmId);
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from likes where film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public Collection<Film> getFilmsByLikes(Integer count) {
        String sql = "select id, rate, name, description, release_date, duration, mpa " +
                "from " +
                "(select *, count(l.user_id) as likes_count " +
                "from film as f " +
                "left join likes as l on f.id = l.film_id " +
                "group by f.id " +
                "order by likes_count desc " +
                "limit ?)";
        return jdbcTemplate.query(sql, this::mapRowToFilm, count);
    }

    private boolean isLLikeExist(Integer filmId, Integer userId) {
        String sqlQuery = "select user_id from likes where film_id = ? and user_id = ?";
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, userId);
        } catch (Exception exp) {
            log.warn(exp.getMessage());
        }
        return count > 0;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .rate(resultSet.getInt("rate"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(Duration.ofSeconds(resultSet.getInt("duration")))
                .mpa(mpaDbStorage.getNewMpaObject(resultSet.getInt("mpa")))
                .build();
    }
}
