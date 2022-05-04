package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class Film {
    private Set<Integer> likes = new HashSet<>();
    private int id;

    @NotBlank(message = "name may not be null")
    private String name;

    private String description;
    private LocalDate releaseDate;
    private Duration duration;

    public void addLike(Integer userId) {
        likes.add(userId);
        log.info("User with ID " + userId + " has been add like to film with ID " + this.id);
    }

    public void removeLike(Integer userId) {
        likes.remove(userId);
        log.info("User with ID " + userId + " has been remove like from film with ID " + this.id);
    }
}
