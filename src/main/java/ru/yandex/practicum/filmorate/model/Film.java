package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
@Builder
public class Film {
    private int id;
    private int rate;

    @NotBlank(message = "name may not be null")
    private String name;

    @NotBlank(message = "name may not be null")
    private String description;
    private LocalDate releaseDate;
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;

    public void increaseRate() {
        ++rate;
    }

    public void decreaseRate() {
        --rate;
    }
}
