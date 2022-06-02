package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

@Data
@JsonDeserialize(using = MpaDeSerializer.class)
public class Mpa {
    private int id;
    private String title;

    public Mpa(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
