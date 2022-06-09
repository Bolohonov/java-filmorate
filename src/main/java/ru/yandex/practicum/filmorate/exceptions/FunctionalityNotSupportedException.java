package ru.yandex.practicum.filmorate.exceptions;

public class FunctionalityNotSupportedException extends RuntimeException{
    public FunctionalityNotSupportedException(String s) {
        super(s);
    }
}