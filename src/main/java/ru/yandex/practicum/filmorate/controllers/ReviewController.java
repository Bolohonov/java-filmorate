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

    @DeleteMapping("{id}")
    public Map<String, String> deleteReviewById(@PathVariable(required = false) Integer id) {
        if (id == null) {
            throw new IncorrectParameterException("Review id incorrect");
        }

        return reviewService.deleteReviewById(id);
    }

    @GetMapping("{id}")
    public Review findById(@PathVariable(required = false) Integer id) {
        if (id == null) {
            throw new IncorrectParameterException("Review id incorrect");
        }

        return reviewService.findById(id);
    }

    @GetMapping
    public List<Review> getAllReviewsByFilmId(
            @RequestParam(required = false) Integer film,
            @RequestParam(defaultValue = "10", required = false) Integer count
    ) {
        if (film == null) {
            throw new IncorrectParameterException("Query param <film> incorrect");
        }

        return reviewService.getAllReviewsByFilmId(film, count);
    }

    @PutMapping("{id}/like/{userId}")
    public Review putLikeReview(
            @PathVariable(required = false) Integer id,
            @PathVariable(required = false) Integer userId
    ) {
        if (id == null) {
            throw new IncorrectParameterException("Review id incorrect");
        }

        if (userId == null) {
            throw new IncorrectParameterException("User id incorrect");
        }

        return reviewService.addLikeReview(id, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public Review putDislikeReview(
            @PathVariable(required = false) Integer id,
            @PathVariable(required = false) Integer userId
    ) {
        if (id == null) {
            throw new IncorrectParameterException("Review id incorrect");
        }

        if (userId == null) {
            throw new IncorrectParameterException("User id incorrect");
        }

        return reviewService.addDislikeReview(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Review deleteLikeReview(
            @PathVariable(required = false) Integer id,
            @PathVariable(required = false) Integer userId
    ) {
        if (id == null) {
            throw new IncorrectParameterException("Review id incorrect");
        }

        if (userId == null) {
            throw new IncorrectParameterException("User id incorrect");
        }

        return reviewService.deleteLikeReview(id, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public Review deleteDislikeReview(
            @PathVariable(required = false) Integer id,
            @PathVariable(required = false) Integer userId
    ) {
        if (id == null) {
            throw new IncorrectParameterException("Review id incorrect");
        }

        if (userId == null) {
            throw new IncorrectParameterException("User id incorrect");
        }

        return reviewService.deleteDislikeReview(id, userId);
    }

}
