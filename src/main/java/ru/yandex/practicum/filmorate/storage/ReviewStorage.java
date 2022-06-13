package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.ReviewAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ReviewNotFoundException;

import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Map;

public interface ReviewStorage {
    /**
     * Добавить
     *
     * @param review обьект Review без назначенного id
     * @return добавленный обьект Review c назначенным для него id
     * @throws ReviewAlreadyExistException если отзыв к от пользователя к фильму добавлен ранее
     * @throws UserNotFoundException       если пользователь не найден или значение поля id равен null
     * @throws FilmNotFoundException       если фильм не был найден
     */
    Review add(Review review);

    /**
     * Обновить
     *
     * @param review объект Review обязательно должен иметь не нулевое поле id
     * @return обновленный обект Review
     * @throws ReviewNotFoundException если отзыв не найден
     */
    Review update(Review review);

    /**
     * Удалить отзыв по id
     *
     * @param id
     * @return
     */
    Map<String, String> deleteReviewById(int id);

    /**
     * Найти по id
     *
     * @param id
     * @return
     * @throws UserNotFoundException если пользователь не найден
     */
    Review findById(int id);

    /**
     * Получить все отзывы по id фильма
     *
     * @param filmId
     * @return
     * @throws FilmNotFoundException если фильм не найден
     */
    List<Review> getAllReviewsByFilmId(int filmId, int count);

    /**
     * Добавить лайк/дизлайк
     *
     * @param reviewId
     * @param userId
     * @param value    значение 1 - лайк, -1 - дизлайк
     * @return
     * @throws ReviewNotFoundException если отзыв не найден
     * @throws UserNotFoundException   если пользователь не найден
     */
    Review addReviewUseful(int reviewId, int userId, int value);

    /**
     * Удалить лайк/дизлайк
     *
     * @param reviewId
     * @param userId
     * @param value
     * @return
     * @throws ReviewNotFoundException если отзыв не найден
     * @throws UserNotFoundException   если пользователь не найден
     */
    Review deleteReviewUseful(int reviewId, int userId, int value);
}
