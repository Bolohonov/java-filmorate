package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
@JsonDeserialize(using = MpaDeSerializer.class)
public class Mpa {
    private int id;
    public Mpa(int id) {
        this.id = id;
    }
}
