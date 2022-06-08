package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaDbStorage;
    private final DirectorDbStorage directorDbStorage;

    private static final String SQL_INSERT =
            "insert into likes (film_id, user_id) " +
                    "values (?, ?)";
    private static final String SQL_DELETE =
            "delete from likes where film_id = ? AND user_id = ?";
    private static final String SQL_SELECT_FILMS_BY_LIKES =
            "select * from film " +
                    "LEFT JOIN " +
                    "(SELECT f.id, count(l.USER_ID) as likes_COUNT " +
                    "FROM film AS f " +
                    "LEFT JOIN Likes AS l ON f.id = l.film_id " +
                    "GROUP BY f.id) as LC oN FILM.ID = LC.ID " +
                    "GROUP BY film.id order by likes_COUNT desc limit ?";
    private static final String SQL_SELECT =
            "select user_id from likes where film_id = ? and user_id = ?";
    private static final String SQL_FIND_ALL_LIKES =
           "SELECT user_id " +
           "FROM likes " +
           "WHERE film_id = ?;";

    public LikesDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaDbStorage, DirectorDbStorage directorDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.directorDbStorage = directorDbStorage;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        if (!this.isLLikeExist(filmId, userId)) {
            jdbcTemplate.update(SQL_INSERT,
                    filmId,
                    userId);
        } else {
            log.warn("Пользователь с id {} уже поставил лайк фильму с id {}", userId, filmId);
        }
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(SQL_DELETE, filmId, userId);
    }

    @Override
    public Collection<Film> getFilmsByLikes(Integer count) {
        return jdbcTemplate.query(SQL_SELECT_FILMS_BY_LIKES, this::mapRowToFilm, count);
    }

    private boolean isLLikeExist(Integer filmId, Integer userId) {
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(SQL_SELECT, Integer.class, filmId, userId);
        } catch (Exception exp) {
            log.warn(exp.getMessage());
        }
        return count > 0;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film =  Film.builder()
                .id(resultSet.getInt("id"))
                .rate(resultSet.getInt("rate"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(Duration.ofSeconds(resultSet.getInt("duration")))
                .mpa(mpaDbStorage.getNewMpaObject(resultSet.getInt("mpa")))
                .director(directorDbStorage.findDirectorById(resultSet.getInt("director_id")).orElse(null))
                .build();

        SqlRowSet likesAsRowSet = jdbcTemplate.queryForRowSet(SQL_FIND_ALL_LIKES, film.getId());
        Set<Integer> likes = new HashSet<>();
        while (likesAsRowSet.next()) {
            Integer likeId = likesAsRowSet.getInt("user_id");

            likes.add(likeId);
        }
        film.setLikes(likes);
        return film;
    }
}
