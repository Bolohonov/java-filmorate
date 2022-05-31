package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    private int id;

    public Mpa(int id) {
        this.id = id;
    }
}
