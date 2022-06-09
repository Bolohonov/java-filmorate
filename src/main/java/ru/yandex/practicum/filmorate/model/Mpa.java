package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonDeserialize(using = MpaDeSerializer.class)
public class Mpa {
    private int id;
    private String name;
}
