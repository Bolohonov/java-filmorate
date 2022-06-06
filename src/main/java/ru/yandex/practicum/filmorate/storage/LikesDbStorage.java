package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MatchingLikesNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.RecommendationNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaDbStorage;
    private final UserDbStorage userDbStorage;
    private static final int USERS_MATCHING_LIKES_MAX = 2;
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

    private static final String SQL_SELECT_FILMS_THAT_USER_LIKES =
            "select * from film " +
                    "LEFT JOIN likes AS l ON l.user_id = ?";
    private static final String SQL_SELECT =
            "select user_id from likes where film_id = ? and user_id = ?";


    public LikesDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaDbStorage, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
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
    public Collection<Film> getFilmsByLikes(Integer count) {
        return jdbcTemplate.query(SQL_SELECT_FILMS_BY_LIKES, this::mapRowToFilm, count);
    }

    @Override
    public Collection<Film> getFilmsThatUserLikes(Integer userId) {
        return jdbcTemplate.query(SQL_SELECT_FILMS_THAT_USER_LIKES, this::mapRowToFilm, userId);
    }

    @Override
    public Collection<Film> getRecommendations(Integer userId) {
        HashMap<User, Collection<Film>> mapMatchingLikes = this.getUsersMatchingLikes(userId);
        Collection<Film> filmsToRecommend = null;
        for (Map.Entry e : mapMatchingLikes.entrySet()) {
            User user = (User) e.getKey();
            Collection<Film> filmsUserLikes = this.getFilmsThatUserLikes(user.getId());
            Collection<Film> filmsMatchingLikes = (Collection<Film>) e.getValue();
            filmsToRecommend.addAll(filmsUserLikes.stream()
                    .filter(film -> !filmsMatchingLikes.contains(film)).collect(Collectors.toSet()));
        }
        if (filmsToRecommend.isEmpty()) {
            throw new RecommendationNotFoundException("Пока мы не можем вам ничего рекомендовать");
        }
        return filmsToRecommend;
    }

    private HashMap<User, Collection<Film>> getUsersMatchingLikes(Integer userId) {
        Collection<Film> firstUserLikes = this.getFilmsThatUserLikes(userId);
        Collection<User> users = this.userDbStorage.getUsers();
        HashMap<User, Collection<Film>> usersMatchingLikes = null;
        users.remove(userDbStorage.findUserById(userId));
        for (User u : users) {
            Collection<Film> likes;
            likes = firstUserLikes
                        .stream()
                        .distinct()
                        .filter(this.getFilmsThatUserLikes(u.getId())::contains)
                        .collect(Collectors.toSet());
            usersMatchingLikes.put(u, likes);
        }
        if (usersMatchingLikes.isEmpty()) {
            throw new MatchingLikesNotFoundException("Пользователи с похожими интересами не найдены");
        }
        return usersMatchingLikes.entrySet()
                        .stream()
                        .sorted(Comparator.comparingInt(o -> o.getValue().size()))
                        .limit(USERS_MATCHING_LIKES_MAX)
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
