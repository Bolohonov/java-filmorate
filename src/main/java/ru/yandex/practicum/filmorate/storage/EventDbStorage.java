package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Component
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEvent(int userId, int entityId, String eventType, String operation) {
        String sql = "insert into event(event_time, user_id, event_type, operation, entity_id ) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, Timestamp.valueOf(LocalDateTime.now()), userId, eventType, operation, entityId);
        log.warn("Событие {} для пользователя {} записано", eventType, userId);
    }

    @Override
    public Collection<Event> getEventsForUser(int id) {
        String sql = "select id, event_time, user_id, event_type, operation, entity_id " +
                "from event where user_id = ? order by event_time desc";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeEvent(rs), id);
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        return Event.builder().
                id(rs.getInt("id"))
                .timestamp(rs.getTimestamp("event_time").toLocalDateTime())
                .userId(rs.getInt("user_id"))
                .eventType(rs.getString("event_type"))
                .entityId(rs.getInt("entity_id"))
                .operation(rs.getString("operation"))
                .build();
    }
}
