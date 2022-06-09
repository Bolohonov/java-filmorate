package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;

@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_INSERT =
            "insert into director (name) values (?)";
    private static final String SQL_SELECT =
            "select id, name from director";
    private static final String SQL_DELETE =
            "delete from director where id = ?";
    private static final String SQL_UPDATE =
            "update director set name = ? where id = ?";
    private static final String SQL_SELECT_WITH_ID =
            "select id, name from director where id = ?";


    @Override
    public Director addDirector(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT, new String[]{"id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(keyHolder.getKey().intValue());

        return director;
    }

    @Override
    public List<Director> findAllDirectors() {
        SqlRowSet filmAsRowSet = jdbcTemplate.queryForRowSet(SQL_SELECT);
        List<Director> directors = new ArrayList<>();
        while (filmAsRowSet.next()) {
            Director director = buildDirector(filmAsRowSet);
            directors.add(director);
        }
        return directors;
    }

    @Override
    public Optional<Director> findDirectorById(Integer id) {
        return of(jdbcTemplate.queryForRowSet(SQL_SELECT_WITH_ID, id))
                .filter(SqlRowSet::next)
                .map(this::buildDirector);
    }

    @Override
    public Optional<Director> updateDirector(Director director) {
        jdbcTemplate.update(
                SQL_UPDATE,
                director.getName(),
                director.getId()
        );
        return of(director);
    }

    @Override
    public boolean deleteDirector(Integer id) {
        return jdbcTemplate.update(SQL_DELETE, id) > 0;
    }

    private Director buildDirector(SqlRowSet filmAsRowSet) {
        return Director.builder()
                .id(filmAsRowSet.getInt("id"))
                .name(filmAsRowSet.getString("name"))
                .build();
    }
}