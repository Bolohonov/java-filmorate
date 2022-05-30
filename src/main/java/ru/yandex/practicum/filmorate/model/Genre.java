package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Slf4j
@Data
public class Genre {

    private int id;

    @NotBlank(message = "name may not be null")
    private String name;
}