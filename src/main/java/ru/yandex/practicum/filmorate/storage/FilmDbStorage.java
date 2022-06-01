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
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public Collection<Film> getFilms() {
        String sql = "select id, rate, name, description, release_date, duration, mpa from film";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into film (rate, name, description, release_date, duration, mpa) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setInt(1, film.getRate());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, Integer.valueOf((int) film.getDuration().toSeconds()));
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        this.addFilmMpa(film.getId(), film.getMpa().getId());
        return film;
    }

    @Override
    public void deleteFilm(Integer id) {
        String sqlQuery = "delete from film where id = ?";
        jdbcTemplate.update(sqlQuery, id);
        this.deleteFilmMpa(id);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update film set rate = ?, name = ?, description = ?, release_date = ? " +
                ", duration = ?, mpa = ? where id = ?";
        jdbcTemplate.update(sqlQuery,
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
        String sqlQuery = "select id, rate, name, description, release_date, duration, mpa " +
                "from film where id = ?";
        Optional<Film> film;
        try {
            film = Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id));
        } catch (Exception exp) {
            log.warn("Фильм с id {} не найден", id);
            throw new FilmNotFoundException(exp.getMessage());
        }
        return film;
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
        String sqlQuery = "insert into film_mpa (film_id, mpa_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                filmId,
                mpaId);
    }

    private void updateFilmMpa(Integer filmId, Integer mpaId) {
        String sqlQuery = "update film_mpa set mpa_id = ? where film_id = ?";
        jdbcTemplate.update(sqlQuery,
                mpaId,
                filmId);
    }

    private void deleteFilmMpa(Integer filmId) {
        String sqlQuery = "delete from film_mpa where film_id = ? ";
        jdbcTemplate.update(sqlQuery,
                filmId);
    }
}
