package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaDbStorage;
    private static final String SQL_SELECT =
            "select id, rate, name, description, release_date, duration, mpa from film";
    private static final String SQL_INSERT =
            "insert into film (rate, name, description, release_date, duration, mpa) " +
                    "values (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE =
            "delete from film where id = ?";
    private static final String SQL_UPDATE =
            "update film set rate = ?, name = ?, description = ?, release_date = ? " +
                    ", duration = ?, mpa = ? where id = ?";
    private static final String SQL_SELECT_WITH_ID =
            "select id, rate, name, description, release_date, duration, mpa " +
                    "from film where id = ?";
    private static final String FIND_ALL_LIKES_SQL =
            "SELECT id " +
            "FROM user_filmorate " +
            "WHERE id IN (SELECT user_id " +
                         "FROM likes " +
                         "WHERE film_id = ?);";
    public static final String SQL_SELECT_COMMON_FILMS_IDS_BETWEEN_TWO_USERS =
            "SELECT film_id\n" +
                    "FROM likes\n" +
                    "WHERE user_id = ?\n" +
                    "INTERSECT\n" +
                    "SELECT film_id\n" +
                    "FROM likes\n" +
                    "WHERE user_id = ?;";

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public Collection<Film> getFilms() {
        return jdbcTemplate.query(SQL_SELECT, this::mapRowToFilm);
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT, new String[]{"id"});
            stmt.setInt(1, film.getRate());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, (int) film.getDuration().toSeconds());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(SQL_UPDATE,
                film.getRate(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toSeconds(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        Optional<Film> film;
        try {
            film = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_WITH_ID,
                    this::mapRowToFilm, id));
        } catch (Exception exp) {
            log.warn("Фильм с id {} не найден", id);
            throw new FilmNotFoundException(exp.getMessage());
        }
        return film;
    }

    @Override
    public Collection<Film> getCommonFilmsBetweenTwoUsers(Integer userId, Integer friendId) {
        var filmsAsRowSet = jdbcTemplate.queryForRowSet(SQL_SELECT_COMMON_FILMS_IDS_BETWEEN_TWO_USERS, userId, friendId);
        List<Film> films = new ArrayList<>();
        while (filmsAsRowSet.next()) {
            films.add(getFilmById(filmsAsRowSet.getInt("film_id")).orElseThrow());
        }
        return films;
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
                .build();

        SqlRowSet likesAsRowSet = jdbcTemplate.queryForRowSet(FIND_ALL_LIKES_SQL, film.getId());
        Set<Integer> likes = new HashSet<>();
        while (likesAsRowSet.next()) {
            Integer likeId = likesAsRowSet.getInt("id");
            likes.add(likeId);
        }
        film.setLikes(likes);
        return film;
    }
}
