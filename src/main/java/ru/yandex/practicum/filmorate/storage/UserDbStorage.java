package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getUsers() {
        String sql = "select id, name, login, email, birthday from user_filmorate";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into user_filmorate (name, login, email, birthday) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());
        return user;
    }

    @Override
    public void deleteUser(Integer id) {
        String sqlQuery = "delete from user_filmorate where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update user_filmorate set name = ?, login = ?, email = ?, birthday = ? where id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());
        return user;
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        String sqlQuery = "select id, name, login, email, birthday " +
                "from user_filmorate where id = ?";
        return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id));
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
