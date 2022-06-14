package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FunctionalityNotSupportedException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.*;
import java.sql.Date;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaDbStorage;
    private final DirectorDbStorage directorDbStorage;
    private static final String SQL_SELECT =
            "select id, rate, name, description, release_date, duration, mpa, director_id from film";
    private static final String SQL_INSERT =
            "insert into film (rate, name, description, release_date, duration, mpa, director_id) " +
                    "values (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE =
            "delete from film where id = ?";
    private static final String SQL_UPDATE =
            "update film set rate = ?, name = ?, description = ?, release_date = ? " +
                    ", duration = ?, mpa = ?, director_id = ? where id = ?";
    private static final String SQL_SELECT_WITH_ID =
            "select id, rate, name, description, release_date, duration, mpa, director_id " +
                    "from film where id = ?";
    private static final String SQL_SEARCH_DIRECTOR_TITLE =
            "select * " +
                    "from FILM " +
                    "LEFT JOIN " +
                    "    (SELECT f.id, count(l.USER_ID) as likes_COUNT " +
                    "           FROM film AS f " +
                    "           LEFT JOIN Likes AS l ON f.id = l.film_id GROUP BY f.id) as LC ON FILM.ID = LC.ID " +
                    "LEFT JOIN DIRECTOR D on FILM.DIRECTOR_ID = D.ID " +
                    "WHERE D.NAME ilike ? AND FILM.NAME ilike ? " +
                    "GROUP BY film.id order by likes_COUNT desc";
    private static final String SQL_SEARCH_DIRECTOR =
            "select * " +
                    "from FILM " +
                    "LEFT JOIN " +
                    "    (SELECT f.id, count(l.USER_ID) as likes_COUNT " +
                    "           FROM film AS f " +
                    "           LEFT JOIN Likes AS l ON f.id = l.film_id GROUP BY f.id) as LC ON FILM.ID = LC.ID " +
                    "LEFT JOIN DIRECTOR D on FILM.DIRECTOR_ID = D.ID " +
                    "WHERE D.NAME ilike ? " +
                    "GROUP BY film.id order by likes_COUNT desc";
    private static final String SQL_SEARCH_TITLE =
            "select * " +
                    "from FILM " +
                    "LEFT JOIN " +
                    "    (SELECT f.id, count(l.USER_ID) as likes_COUNT " +
                    "           FROM film AS f " +
                    "           LEFT JOIN Likes AS l ON f.id = l.film_id GROUP BY f.id) as LC ON FILM.ID = LC.ID " +
                    "WHERE FILM.NAME ilike ? " +
                    "GROUP BY film.id order by likes_COUNT desc";

    private static final String SQL_FIND_ALL_LIKES =

            "SELECT user_id " +
            "FROM likes " +
            "WHERE film_id = ?;";
    public static final String SQL_FIND_ALL_FILMS_BY_DIRECTOR_ID =
            "SELECT id " +
            "FROM film " +
            "WHERE director_id = ?;";
    public static final String SQL_SELECT_COMMON_FILMS_IDS_BETWEEN_TWO_USERS =
            "SELECT film_id\n" +
                    "FROM likes\n" +
                    "WHERE user_id = ?\n" +
                    "INTERSECT\n" +
                    "SELECT film_id\n" +
                    "FROM likes\n" +
                    "WHERE user_id = ?;";

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaDbStorage, DirectorDbStorage directorDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.directorDbStorage = directorDbStorage;
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
            if (film.getDirector() != null) {
                stmt.setInt(7, film.getDirector().getId());
            } else {
                stmt.setNull(7, Types.NULL);
            }
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
                film.getDirector(),
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
    public Collection<Film> search(String query, String by) {
        List<String> list = Arrays.stream(by.split(","))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        if (list.contains("director") && list.contains("title") && list.size() == 2) {
            log.info("Получение списка фильмов по режиссеру и наименованию. Запрос - {}", query);
            return jdbcTemplate.query(SQL_SEARCH_DIRECTOR_TITLE, this::mapRowToFilm, "%" + query + "%", "%" + query + "%");
        }
        if (list.contains("director") && list.size() == 1) {
            log.info("Получение списка фильмов по режиссеру. Запрос - {}", query);
            return jdbcTemplate.query(SQL_SEARCH_DIRECTOR, this::mapRowToFilm, "%" + query + "%");
        }
        if (list.contains("title") && list.size() == 1) {
            log.info("Получение списка фильмов по наименованию. Запрос - {}", query);
            return jdbcTemplate.query(SQL_SEARCH_TITLE, this::mapRowToFilm, "%" + query + "%");
        }
        log.warn("Попытка получения списка фильмов по {}", by);
        throw new FunctionalityNotSupportedException("Функциональность не поддерживается");
    }


    public Collection<Film> findFilmsByDirectorId(Integer directorId) {
        var filmAsRowSet = jdbcTemplate.queryForRowSet(SQL_FIND_ALL_FILMS_BY_DIRECTOR_ID, directorId);
        List<Film> films = new ArrayList<>();
        while (filmAsRowSet.next()) {
            films.add(getFilmById(filmAsRowSet.getInt("id")).orElseThrow());
        }
        return films;
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
                .mpa(mpaDbStorage.getMpaById(resultSet.getInt("mpa")).get())
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
