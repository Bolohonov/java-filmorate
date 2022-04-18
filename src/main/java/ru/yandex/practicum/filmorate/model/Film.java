package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    @NotNull
    private int id;

    @NotBlank(message = "name may not be null")
    private String name;

    private String description;
    private LocalDate releaseDate;
    private Duration duration;
}
