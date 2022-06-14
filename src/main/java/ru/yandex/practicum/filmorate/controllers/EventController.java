package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.EventService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}/feed")
    public Collection<Event> getFeedForUser(@PathVariable("id") Integer id) {
        log.info("Get feed for user with ID {}", id);
        return eventService.getFeedForUser(id);
    }
}
