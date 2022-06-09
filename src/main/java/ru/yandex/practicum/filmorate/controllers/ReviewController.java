package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteReviewById(@PathVariable Integer id) {
        return reviewService.deleteReviewById(id);
    }

    @GetMapping("/{id}")
    public Review findById(@PathVariable Integer id) {
        return reviewService.findById(id);
    }

    @GetMapping
    public List<Review> getAllReviewsByFilmId(
            @RequestParam(required = false) Integer filmId,
            @RequestParam(defaultValue = "10", required = false) Integer count
    ) {
        if (filmId == null) {
            throw new IncorrectParameterException("Query param <film> incorrect");
        }

        return reviewService.getAllReviewsByFilmId(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Review putLikeReview(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        return reviewService.addLikeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Review putDislikeReview(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        return reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Review deleteLikeReview(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        return reviewService.deleteLikeReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Review deleteDislikeReview(
            @PathVariable Integer id,
            @PathVariable Integer userId
    ) {
        return reviewService.deleteDislikeReview(id, userId);
    }

}
