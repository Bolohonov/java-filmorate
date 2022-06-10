package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.OperationType;
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

    private static final String SQL_SELECT_EVENTS_FROM_FRIENDS =
            "SELECT id, event_time, user_id, event_type, operation, entity_id " +
                    "FROM event WHERE user_id IN (SELECT second_user_id " +
                    "FROM friends as fr " +
                    "WHERE fr.first_user_id = ? AND fr.accept_first = true " +
                    "UNION " +
                    "SELECT first_user_id " +
                    "FROM friends as fr " +
                    "WHERE fr.second_user_id = ? AND fr.accept_second = true) " +
                    "ORDER BY event_time DESC";
    private static final String SQL_INSERT_EVENT =
            "INSERT INTO event(event_time, user_id, event_type, operation, entity_id ) " +
                    "VALUES (?, ?, ?, ?, ?)";
    private  final String SQL_SELECT = "SELECT id FROM event " +
            "WHERE user_id = ? AND event_type = ? AND operation = ? AND entity_id = ?";

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEvent(int userId, int entityId, String eventType, String operation) {
        if (!isEventExist(userId, entityId, eventType, operation)) {
            jdbcTemplate.update(SQL_INSERT_EVENT, Timestamp.valueOf(LocalDateTime.now()), userId, eventType, operation, entityId);
            log.warn("Событие {} для пользователя {} записано", eventType, userId);
        } else {
            log.warn("Событие уже записано");
        }
    }

    @Override
    public Collection<Event> getFeedForUser(int id) {
        return jdbcTemplate.query(SQL_SELECT_EVENTS_FROM_FRIENDS, (rs, rowNum) -> makeEvent(rs), id, id);
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        return Event.builder().
                id(rs.getInt("id"))
                .timestamp(rs.getTimestamp("event_time").toLocalDateTime())
                .userId(rs.getInt("user_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(OperationType.valueOf(rs.getString("operation")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }

    private boolean isEventExist(int userId, int entityId, String eventType, String operation) {
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(SQL_SELECT, Integer.class, userId, eventType, operation, entityId);
        } catch (Exception exp) {
            log.warn(exp.getMessage());
        }
        return count > 0;
    }
}
