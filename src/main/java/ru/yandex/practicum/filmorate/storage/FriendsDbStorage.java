package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addToFriends(Integer firstUserId, Integer secondUserId) {
        if (firstUserId > secondUserId) {
            Integer idLarge = firstUserId;
            firstUserId = secondUserId;
            secondUserId = idLarge;
        }
        String sqlQuery = "insert into friends (first_user_id, second_user_id, accept) " +
                "values (?, ?, ?)";
        if (!isRequestToFriendExist(firstUserId, secondUserId)) {
            jdbcTemplate.update(sqlQuery,
                    firstUserId,
                    secondUserId,
                    false);
        } else {
            if (!isAcceptTrue(firstUserId, secondUserId)) {
                jdbcTemplate.update(sqlQuery,
                        firstUserId,
                        secondUserId,
                        true);
            }
        }
    };

    public void removeFriend(Integer firstUserId, Integer secondUserId) {
        String sqlQuery = "delete from likes where user_id = ? AND film_id = ?";
        retjdbcTemplate.update(sqlQuery, userId, filmId);
    };

    private boolean isAcceptTrue(Integer firstUserId, Integer secondUserId) {
        String sqlQuery = "select accept from friends where first_user_id = ? AND second_user_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, firstUserId, secondUserId));
    }

    private boolean isRequestToFriendExist(Integer firstUserId, Integer secondUserId) {
        String sqlQuery = "select first_user_id from friends where first_user_id = ? AND second_user_id = ?";
        return firstUserId.equals(jdbcTemplate.queryForObject(sqlQuery, Integer.class, firstUserId, secondUserId));
    }
}
