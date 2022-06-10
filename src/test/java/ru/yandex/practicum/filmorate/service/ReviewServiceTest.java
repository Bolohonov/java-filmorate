package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewServiceTest {
    @Autowired
    private FilmService filmService;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewService reviewService;

    private Supplier<User> userSupplier = () -> {
        return User.builder()
                .name("User")
                .login("User" + System.currentTimeMillis())
                .email("user" + System.currentTimeMillis() + "@mail.com")
                .birthday(LocalDate.of(1990, 12, 2))
                .build();
    };

    private Supplier<Film> filmSupplier = () -> {
        return Film.builder()
                .name("Film" + System.currentTimeMillis())
                .description("Desc")
                .releaseDate(LocalDate.of(1950, 12, 2))
                .duration(Duration.ofSeconds(1500))
                .mpa(new Mpa(1, "G"))
                .likes(new HashSet<>())
                .build();
    };

    @Test
    public void reviewCreate() {
        User user = userService.addUser(userSupplier.get());
        Film film = filmService.addFilm(filmSupplier.get());

        assertDoesNotThrow(() -> {
            reviewService.addReview(Review.builder()
                    .content("Review 1")
                    .isPositive(true)
                    .filmId(film.getId())
                    .userId(user.getId())
                    .build());
        });
    }

    @Test
    public void reviewAlreadyExists() {
        Film film = filmService.addFilm(filmSupplier.get());
        User user = userService.addUser(userSupplier.get());
        Review review = Review.builder()
                .content("Test")
                .isPositive(true)
                .filmId(film.getId())
                .userId(user.getId())
                .build();

        reviewService.addReview(review);

        assertThrows(ReviewAlreadyExistException.class, () -> reviewService.addReview(review));
    }

    @Test
    public void reviewCreateFailFilmId() {
        User user = userService.addUser(userSupplier.get());

        assertThrows(FilmNotFoundException.class, () -> {
            reviewService.addReview(Review.builder()
                    .content("Review 1")
                    .isPositive(true)
                    .filmId(Integer.MAX_VALUE)
                    .userId(user.getId())
                    .build());
        });
    }

    @Test
    public void reviewCreateFailUserId() {
        Film film = filmService.addFilm(filmSupplier.get());

        assertThrows(UserNotFoundException.class, () -> {
            reviewService.addReview(Review.builder()
                    .content("Review 1")
                    .isPositive(true)
                    .filmId(film.getId())
                    .userId(Integer.MAX_VALUE)
                    .build());
        });
    }

    @Test
    public void reviewUpdate() {
        User user = userService.addUser(userSupplier.get());
        Film film = filmService.addFilm(filmSupplier.get());

        Review review = reviewService.addReview(Review.builder()
                .content("Review")
                .isPositive(true)
                .filmId(film.getId())
                .userId(user.getId())
                .build());

        String updatedContent = "Updated content";

        assertAll(
                () -> {
                    assertDoesNotThrow(() -> {
                        review.setContent(updatedContent);
                        reviewService.updateReview(review);
                    });
                },
                () -> assertEquals(reviewService.findById(review.getId()).getContent(), updatedContent),
                () -> {
                    assertThrows(ReviewNotFoundException.class, () -> {
                        review.setId(Integer.MAX_VALUE);
                        reviewService.updateReview(review);
                    });
                }
        );
    }

    @Test
    public void getReviewById() {
        User user = userService.addUser(userSupplier.get());
        Film film = filmService.addFilm(filmSupplier.get());

        Review review = reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film.getId())
                .userId(user.getId())
                .build());

        assertAll(
                () -> assertEquals(review, reviewService.findById(review.getId())),
                () -> assertThrows(ReviewNotFoundException.class, () -> reviewService.findById(Integer.MAX_VALUE))
        );
    }

    @Test
    public void deleteReviewById() {
        Film film = filmService.addFilm(filmSupplier.get());
        User user = userService.addUser(userSupplier.get());

        Review review = reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film.getId())
                .userId(user.getId())
                .build());

        reviewService.deleteReviewById(review.getId());

        assertThrows(ReviewNotFoundException.class, () -> reviewService.findById(review.getId()));
    }

    @Test
    public void getAllReviewsByFilmId() {
        Film film = filmService.addFilm(filmSupplier.get());
        User user1 = userService.addUser(userSupplier.get());
        User user2 = userService.addUser(userSupplier.get());
        List<Review> reviewCheckList = new ArrayList<>();

        reviewCheckList.add(reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film.getId())
                .userId(user1.getId())
                .build()));

        reviewCheckList.add(reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film.getId())
                .userId(user2.getId())
                .build()));

        assertEquals(reviewCheckList, reviewService.getAllReviewsByFilmId(film.getId(), 10));
    }

    @Test
    public void addLikeOrDeleteLikeReview() {
        Film film = filmService.addFilm(filmSupplier.get());
        User user = userService.addUser(userSupplier.get());

        Review review = reviewService.addReview(Review.builder()
                .content("test")
                .isPositive(true)
                .filmId(film.getId())
                .userId(user.getId())
                .build());

        assertAll(
                () -> {
                    reviewService.addLikeReview(review.getId(), user.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), 1);
                },
                () -> {
                    assertThrows(UsefulAlreadyExistsException.class,
                            () -> reviewService.addLikeReview(review.getId(), user.getId())
                    );
                },
                () -> {
                    reviewService.deleteLikeReview(review.getId(), user.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), 0);
                }
        );
    }

    @Test
    public void addDislikeOrDeleteDislikeReview() {
        User user = userService.addUser(userSupplier.get());
        Film film = filmService.addFilm(filmSupplier.get());

        Review review = reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film.getId())
                .userId(user.getId())
                .build());

        assertAll(
                () -> {
                    reviewService.addDislikeReview(review.getId(), user.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), -1);
                },
                () -> {
                    assertThrows(UsefulAlreadyExistsException.class,
                            () -> {
                                reviewService.addDislikeReview(review.getId(), user.getId());
                            }
                    );
                },
                () -> {
                    reviewService.deleteDislikeReview(review.getId(), user.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), 0);
                }
        );
    }
}