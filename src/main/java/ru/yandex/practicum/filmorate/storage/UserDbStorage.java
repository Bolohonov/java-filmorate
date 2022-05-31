package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
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
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        String sqlQuery = "select id, name, login, email, birthday " +
                "from user_filmorate where id = ?";
        Optional<User> user;
        try {
            user = Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id));
        } catch (Exception exp) {
            log.warn("Пользователь с id {} не найден", id);
            throw new UserNotFoundException("Пользователь не найден");
        }
        return user;
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
