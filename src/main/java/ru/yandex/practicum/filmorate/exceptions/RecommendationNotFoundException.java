package ru.yandex.practicum.filmorate.exceptions;

public class RecommendationNotFoundException extends RuntimeException {
    public  RecommendationNotFoundException(String s) {
        super(s);
    }
}
