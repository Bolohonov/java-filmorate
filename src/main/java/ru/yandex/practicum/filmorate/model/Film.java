package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private int rate;

    @NotBlank(message = "name may not be null")
    private String name;

    @NotBlank(message = "name may not be null")
    private String description;
    private LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;

    public void addLike(Integer userId) {
        likes.add(userId);
        log.warn("User with ID {} has been add like to film with ID {}", userId, this.id);
    }

    public void removeLike(Integer userId) {
        likes.remove(userId);
        log.warn("User with ID {} has been remove like from film with ID {}", userId, this.id);
    }

    public int getRate() {
        return likes.size();
    }
}
