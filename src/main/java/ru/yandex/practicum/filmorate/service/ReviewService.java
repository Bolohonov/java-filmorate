package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private static final int USEFUL_CHANGE_STEP = 1;

    @Autowired
    public ReviewService(
            @Qualifier("reviewDBStorage") ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public Review addReview(Review review) {
        log.info("Add {}", review);

        return reviewStorage.add(review);
    }

    public Review updateReview(Review review) {
        log.info("Update review with id:{} on {}", review.getId(), review);

        return reviewStorage.update(review);
    }

    public Map<String, String> deleteReviewById(int id) {
        log.info("Delete review with id:{}", id);

        return reviewStorage.deleteReviewById(id);
    }

    public Review findById(int id) {
        log.info("Find review by id:{}", id);

        return reviewStorage.findById(id);
    }

    public List<Review> getAllReviewsByFilmId(int id, int count) {
        log.info("Get all reviews by film id:{}", id);

        return reviewStorage.getAllReviewsByFilmId(id, count);
    }

    public Review addLikeReview(int reviewId, int userId) {
        log.info("User with id:{} like the review with id:{}", userId, reviewId);

        return reviewStorage.addReviewUseful(reviewId, userId, USEFUL_CHANGE_STEP);
    }

    public Review addDislikeReview(int reviewId, int userId) {
        log.info("User with id:{} dislike the review with id:{}", userId, reviewId);

        return reviewStorage.addReviewUseful(reviewId, userId, -USEFUL_CHANGE_STEP);
    }

    public Review deleteLikeReview(int reviewId, int userId) {
        log.info("User with id:{} delete like the review with id:{}", userId, reviewId);

        return reviewStorage.deleteReviewUseful(reviewId, userId, USEFUL_CHANGE_STEP);
    }

    public Review deleteDislikeReview(int reviewId, int userId) {
        log.info("User with id:{} delete dislike the review with id:{}", userId, reviewId);

        return reviewStorage.deleteReviewUseful(reviewId, userId, -USEFUL_CHANGE_STEP);
    }
}
