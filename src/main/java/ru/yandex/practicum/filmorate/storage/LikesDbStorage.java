package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.RecommendationNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

import java.util.*;
import java.util.stream.Collectors;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Slf4j
@Component
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaDbStorage;
    private final DirectorDbStorage directorDbStorage;
    private final UserDbStorage userDbStorage;

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


    private static final String SQL_SELECT_FILMS_BY_LIKES_BY_GENRE_AND_YEAR =
            "select FILM.ID, FILM.NAME, FILM.DESCRIPTION, FILM.RELEASE_DATE, FILM.DURATION, FILM.RATE, FILM.MPA " +
                    "from FILM " +
                    "LEFT JOIN " +
                    "    (SELECT f.id, count(l.USER_ID) as likes_COUNT " +
                    "           FROM film AS f " +
                    "           LEFT JOIN Likes AS l ON f.id = l.film_id GROUP BY f.id) as LC ON FILM.ID = LC.ID " +
                    "LEFT JOIN FILM_GENRE FG ON FILM.ID = FG.FILM_ID " +
                    "LEFT JOIN GENRE G ON FG.GENRE_ID = G.ID " +
                    "WHERE g.ID = ? AND year(FILM.RELEASE_DATE) = ? " +
                    "GROUP BY film.id order by likes_COUNT desc limit ?";
    private static final String SQL_SELECT_FILMS_BY_LIKES_BY_GENRE =
            "select FILM.ID, FILM.NAME, FILM.DESCRIPTION, FILM.RELEASE_DATE, FILM.DURATION, FILM.RATE, FILM.MPA " +
                    "from FILM " +
                    "LEFT JOIN " +
                    "    (SELECT f.id, count(l.USER_ID) as likes_COUNT " +
                    "           FROM film AS f " +
                    "           LEFT JOIN Likes AS l ON f.id = l.film_id GROUP BY f.id) as LC ON FILM.ID = LC.ID " +
                    "LEFT JOIN FILM_GENRE FG ON FILM.ID = FG.FILM_ID " +
                    "LEFT JOIN GENRE G ON FG.GENRE_ID = G.ID " +
                    "WHERE g.ID = ? " +
                    "GROUP BY film.id order by likes_COUNT desc limit ?";

    private static final String SQL_SELECT_FILMS_BY_LIKES_BY_YEAR =
            "select FILM.ID, FILM.NAME, FILM.DESCRIPTION, FILM.RELEASE_DATE, FILM.DURATION, FILM.RATE, FILM.MPA " +
                    "from FILM " +
                    "LEFT JOIN " +
                    "    (SELECT f.id, count(l.USER_ID) as likes_COUNT " +
                    "           FROM film AS f " +
                    "           LEFT JOIN Likes AS l ON f.id = l.film_id GROUP BY f.id) as LC ON FILM.ID = LC.ID " +
                    "WHERE year(FILM.RELEASE_DATE) = ? " +
                    "GROUP BY film.id order by likes_COUNT desc limit ?";

    private static final String SQL_SELECT_FILMS_THAT_USER_LIKES =
            "select * from film RIGHT JOIN " +
                    "(SELECT l.film_id FROM likes AS l where l.user_id = ?) as LC ON FILM.ID = LC.film_id " +
                    "GROUP BY film.id";

    private static final String SQL_SELECT =
            "select user_id from likes where film_id = ? and user_id = ?";
    private static final String SQL_FIND_ALL_LIKES =
           "SELECT user_id " +
           "FROM likes " +
           "WHERE film_id = ?;";

    public LikesDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaDbStorage, DirectorDbStorage directorDbStorage, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.directorDbStorage = directorDbStorage;
        this.userDbStorage = userDbStorage;
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
    public Collection<Film> getFilmsByLikes(Integer count, Integer genreId, Integer year) {
        if (genreId != 0 && year != 0) {
            return jdbcTemplate.query(SQL_SELECT_FILMS_BY_LIKES_BY_GENRE_AND_YEAR, this::mapRowToFilm, genreId, year, count);
        }
        if (genreId != 0) {
            return jdbcTemplate.query(SQL_SELECT_FILMS_BY_LIKES_BY_GENRE, this::mapRowToFilm, genreId, count);
        }
        if (year != 0) {
            return jdbcTemplate.query(SQL_SELECT_FILMS_BY_LIKES_BY_YEAR, this::mapRowToFilm, year, count);
        }
        return jdbcTemplate.query(SQL_SELECT_FILMS_BY_LIKES, this::mapRowToFilm, count);

    }

    @Override
    public Collection<Film> getRecommendations(Integer userId) {
        Map<User, Collection<Film>> mapMatchingLikes = this.getUsersMatchingLikes(userId);
        int count = mapMatchingLikes.size();
        Collection<Film> filmsToRecommend = new ArrayList<>();
        for (Map.Entry e : mapMatchingLikes.entrySet()) {
            User user = (User) e.getKey();
            Collection<Film> filmsUserLikes = this.getFilmsThatUserLikes(user.getId());
            Collection<Film> filmsMatchingLikes = (Collection<Film>) e.getValue();
            filmsToRecommend.addAll(filmsUserLikes.stream()
                    .filter(film -> !filmsMatchingLikes.contains(film)).collect(Collectors.toSet()));
            --count;
            if (!filmsToRecommend.isEmpty()) {
                return filmsToRecommend;
            }
            if (count == 0 && filmsToRecommend.isEmpty()) {
                throw new RecommendationNotFoundException("Пока мы не можем вам ничего рекомендовать");
            }
        }
        return filmsToRecommend;
    }

    private Collection<Film> getFilmsThatUserLikes(Integer userId) {
        return jdbcTemplate.query(SQL_SELECT_FILMS_THAT_USER_LIKES, this::mapRowToFilm, userId);
    }

    private Map<User, Collection<Film>> getUsersMatchingLikes(Integer userId) {
        Collection<Film> firstUserLikes = this.getFilmsThatUserLikes(userId);
        Collection<User> users = this.userDbStorage.getUsers();
        users.remove(userDbStorage.findUserById(userId).get());
        Map<User, Collection<Film>> usersMatchingLikes = new HashMap<>();
        for (User u : users) {
            Collection<Film> likesMatch;
            likesMatch = firstUserLikes
                    .stream()
                    .filter(this.getFilmsThatUserLikes(u.getId())::contains)
                    .collect(Collectors.toSet());
            if (likesMatch.size() != 0) {
                usersMatchingLikes.put(u, likesMatch);
            }
        }
        if (usersMatchingLikes.isEmpty()) {
            throw new RecommendationNotFoundException("Пользователи с похожими интересами не найдены");
        }
        return usersMatchingLikes
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> o2.size() - o1.size()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
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
