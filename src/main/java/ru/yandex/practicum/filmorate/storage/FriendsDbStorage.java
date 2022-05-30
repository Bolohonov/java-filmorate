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
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addToFriends(Integer firstUserId, Integer secondUserId) {
        boolean result = false;
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
                result = true;
            }
        }
        return result;
    };

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
    };

    public Collection<User> getUserFriends(Integer userId) {
        log.warn("User with ID {} get friends", userId);
        List<User> friends = new ArrayList<>();
        String sql = "select u.*" +
                "from (select first_user_id " +
                "from (select * " +
                "from friends as fr " +
                "where (fr.first_user_id = ? or fr.second_user_id = ?) and fr.accept = true) as tab " +
                "where second_user_id = ? " +
                "union " +
                "select second_user_id " +
                "from (select * " +
                "from friends " +
                "where (first_user_id = ? or second_user_id = ?) and accept = true) as tab " +
                "where first_user_id = ?) as user_friends " +
                "left join user_filmorate as u on u.id = user_friends.first_user_id";
        return jdbcTemplate.query(sql, this::mapRowToUser, userId);
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

    private boolean isAcceptTrue(Integer firstUserId, Integer secondUserId) {
        String sqlQuery = "select accept from friends where first_user_id = ? and second_user_id = ?";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, firstUserId, secondUserId));
    }

    private boolean isRequestToFriendExist(Integer firstUserId, Integer secondUserId) {
        String sqlQuery = "select first_user_id from friends where first_user_id = ? and second_user_id = ?";
        return firstUserId.equals(jdbcTemplate.queryForObject(sqlQuery, Integer.class, firstUserId, secondUserId));
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
