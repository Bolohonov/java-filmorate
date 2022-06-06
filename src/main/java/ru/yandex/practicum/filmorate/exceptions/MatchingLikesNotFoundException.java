package ru.yandex.practicum.filmorate.exceptions;

public class MatchingLikesNotFoundException extends RuntimeException {
    public MatchingLikesNotFoundException(String s) {
        super(s);
    }
}
