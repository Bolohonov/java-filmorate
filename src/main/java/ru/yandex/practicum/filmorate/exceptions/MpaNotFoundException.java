package ru.yandex.practicum.filmorate.exceptions;

public class MpaNotFoundException extends RuntimeException {
    public MpaNotFoundException(String s) {
        super(s);
    }
}
