package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    private final UserStorage userDbStorage;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate, UserStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public boolean addToFriends(Integer firstUserId, Integer secondUserId) {
        Boolean result = false;
        String flag = "FirstIsFirst";
        if (firstUserId > secondUserId) {
            Integer idLarge = firstUserId;
            firstUserId = secondUserId;
            secondUserId = idLarge;
            flag = "FirstIsSecond";
        }
        String sqlInsert = "insert into friends (first_user_id, second_user_id, accept_first, accept_second) " +
                "values (?, ?, ?, ?)";
        String sqlUpdate = "update friends set first_user_id = ?, second_user_id = ?, " +
                "accept_first = ?, accept_second = ?";
        switch (flag) {
            case ("FirstIsFirst"):
                if (!isRequestToFriendExist(firstUserId, secondUserId)) {
                    jdbcTemplate.update(sqlInsert,
                            firstUserId,
                            secondUserId,
                            Boolean.TRUE,
                            Boolean.FALSE);
                    return false;
                } else {
                    if (isAcceptTrueSecondUser(firstUserId, secondUserId)) {
                        jdbcTemplate.update(sqlUpdate,
                                firstUserId,
                                secondUserId,
                                Boolean.TRUE,
                                Boolean.TRUE);
                        result = true;
                    }
                }
                break;
            case ("FirstIsSecond"):
                if (!isRequestToFriendExist(firstUserId, secondUserId)) {
                    jdbcTemplate.update(sqlInsert,
                            firstUserId,
                            secondUserId,
                            Boolean.FALSE,
                            Boolean.TRUE);
                    return false;
                } else {
                    if (isAcceptTrueFirstUser(firstUserId, secondUserId)) {
                        jdbcTemplate.update(sqlUpdate,
                                firstUserId,
                                secondUserId,
                                Boolean.TRUE,
                                Boolean.TRUE);
                        result = true;
                    }
                }
                break;
        }
        return result;
    }

    ;

    public void removeFriend(Integer firstUserId, Integer secondUserId) {
        if (firstUserId > secondUserId) {
            Integer idLarge = firstUserId;
            firstUserId = secondUserId;
            secondUserId = idLarge;
        }
        String sqlQuery = "delete from friends where first_user_id = ? and second_user_id =?";
        jdbcTemplate.update(sqlQuery,
                firstUserId,
                secondUserId);
    }

    ;

    public Collection<User> getUserFriends(Integer userId) {
        log.warn("User with ID {} get friends", userId);
        Collection<User> userFriendsFirst;
        Collection<User> userFriendsSecond;
        String sqlFirst = "select u.*" +
                "from (select second_user_id " +
                "from friends as fr " +
                "where fr.first_user_id = ? and fr.accept_first = true) " +
                "left join user_filmorate as u on u.id = second_user_id";
        userFriendsFirst = jdbcTemplate.query(sqlFirst, this::mapRowToUser, userId);
        String sqlSecond = "select u.*" +
                "from (select first_user_id " +
                "from friends as fr " +
                "where fr.second_user_id = ? and fr.accept_second = true) " +
                "left join user_filmorate as u on u.id = first_user_id";
        userFriendsSecond = jdbcTemplate.query(sqlSecond, this::mapRowToUser, userId);
        userFriendsFirst.addAll(userFriendsSecond);
        return userFriendsFirst;
    }

    public Collection<User> getMatchingFriends(Integer id, Integer otherId) {
        log.warn("User with ID {} get matching friends with user {}", id, otherId);
        Collection<User> firstUserFriends = this.getUserFriends(id);
        Collection<User> secondUserFriends = this.getUserFriends(otherId);
        return firstUserFriends
                .stream()
                .distinct()
                .filter(secondUserFriends::contains)
                .collect(Collectors.toSet());
    }

    private boolean isAcceptTrueFirstUser(Integer firstUserId, Integer secondUserId) {
        String sqlQuery = "select accept_first from friends where first_user_id = ? and second_user_id = ?";
        boolean result = false;
        try {
            result = jdbcTemplate.queryForObject(sqlQuery, Boolean.class, firstUserId, secondUserId);
        } catch (Exception exp) {
            log.warn(exp.getMessage());
        }
        return result;
    }

    private boolean isAcceptTrueSecondUser(Integer firstUserId, Integer secondUserId) {
        String sqlQuery = "select accept_second from friends where first_user_id = ? and second_user_id = ?";
        boolean result = false;
        try {
            result = jdbcTemplate.queryForObject(sqlQuery, Boolean.class, firstUserId, secondUserId);
        } catch (Exception exp) {
            log.warn(exp.getMessage());
        }
        return result;
    }

    private boolean isRequestToFriendExist(Integer firstUserId, Integer secondUserId) {
        String sqlQuery = "select first_user_id from friends where first_user_id = ? and second_user_id = ?";
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, firstUserId, secondUserId);
        } catch (Exception exp) {
            log.warn(exp.getMessage());
        }
        return count > 0;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
