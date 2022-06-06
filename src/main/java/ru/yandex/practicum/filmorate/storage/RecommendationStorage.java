package ru.yandex.practicum.filmorate.storage;

public interface RecommendationStorage {
    Recommendation addRecommendation(Integer firstUserId, Integer secondUserId, Integer filmId);

    void removeRecommendation(Integer firstUserId, Integer secondUserId, Integer filmId);
}
