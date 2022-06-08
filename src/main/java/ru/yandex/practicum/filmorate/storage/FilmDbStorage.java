package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
    private static final String SQL_INSERT_MPA =
            "insert into film_mpa (film_id, mpa_id) " +
                    "values (?, ?)";
    private static final String SQL_UPDATE_MPA =
            "update film_mpa set mpa_id = ? where film_id = ?";
    private static final String SQL_DELETE_MPA =
            "delete from film_mpa where film_id = ? ";
    
    private static final String SQL_SEARCH_TITLE =
            "select *" +
                    "from FILM " +
                    "LEFT JOIN " +
                    "    (SELECT f.id, count(l.USER_ID) as likes_COUNT " +
                    "           FROM film AS f " +
                    "           LEFT JOIN Likes AS l ON f.id = l.film_id GROUP BY f.id) as LC ON FILM.ID = LC.ID " +
                    "WHERE FILM.NAME ilike ? " +
                    "GROUP BY film.id order by likes_COUNT desc";

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
        this.addFilmMpa(film.getId(), film.getMpa().getId());
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        jdbcTemplate.update(SQL_DELETE, id);
        this.deleteFilmMpa(id);
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
        this.updateFilmMpa(film.getId(), film.getMpa().getId());
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
    public Collection<Film> search(String query) {
            return jdbcTemplate.query(SQL_SEARCH_TITLE, this::mapRowToFilm, "%" +  query + "%");
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

    private void addFilmMpa(Integer filmId, Integer mpaId) {
        jdbcTemplate.update(SQL_INSERT_MPA,
                filmId,
                mpaId);
    }

    private void updateFilmMpa(Integer filmId, Integer mpaId) {
        jdbcTemplate.update(SQL_UPDATE_MPA,
                mpaId,
                filmId);
    }

    private void deleteFilmMpa(Integer filmId) {
        jdbcTemplate.update(SQL_DELETE_MPA,
                filmId);
    }
}
