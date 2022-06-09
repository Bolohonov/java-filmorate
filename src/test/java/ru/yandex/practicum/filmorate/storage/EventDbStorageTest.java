package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventDbStorageTest {

    private final EventDbStorage eventDbStorage;

    @Test
    void getEventsForUserTest() {
        Collection<Event> events = eventDbStorage.getEventsForUser(1);
        Assertions.assertEquals(3, events.size());
    }

    @Test
    void addEventTest() {
        eventDbStorage. addEvent(2, 3, "LIKE", "ADD");
        Assertions.assertEquals(1, eventDbStorage.getEventsForUser(2).size());
    }

}
