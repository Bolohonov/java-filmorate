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
import java.time.LocalDate;
import java.util.List;

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

    private static Film film1;
    private static Film film2;
    private static Film film3;
    private static User user1;
    private static User user2;
    private static User user3;
    private static User user4;


    @BeforeAll
    public static void beforeAllReviewDBStorageTests() {
        film1 = Film.builder()
                .name("Film_1")
                .description("Description for Film_1")
                .duration(Duration.ofMinutes(90))
                .rate(4)
                .releaseDate(LocalDate.parse("2021-12-16"))
                .mpa(new Mpa(1, "G"))
                .build();

        film2 = Film.builder()
                .name("Film_2")
                .description("Description for Film_2")
                .duration(Duration.ofMinutes(90))
                .rate(4)
                .releaseDate(LocalDate.parse("2021-12-16"))
                .mpa(new Mpa(1, "G"))
                .build();

        film3 = Film.builder()
                .name("Film_3")
                .description("Description for Film_3")
                .duration(Duration.ofMinutes(90))
                .rate(4)
                .releaseDate(LocalDate.parse("2021-12-16"))
                .mpa(new Mpa(1, "G"))
                .build();

        user1 = User.builder()
                .name("User_1")
                .login("User1login")
                .email("user1@mail.com")
                .birthday(LocalDate.parse("1992-12-16"))
                .build();

        user2 = User.builder()
                .name("User_2")
                .login("User2login")
                .email("user2@mail.com")
                .birthday(LocalDate.parse("1992-12-16"))
                .build();

        user3 = User.builder()
                .name("User_3")
                .login("User3login")
                .email("user3@mail.com")
                .birthday(LocalDate.parse("1992-12-16"))
                .build();

        user4 = User.builder()
                .name("User_4")
                .login("User4login")
                .email("user4@mail.com")
                .birthday(LocalDate.parse("1992-12-16"))
                .build();
    }

    @Test
    public void reviewCreate() {
        filmService.addFilm(film1);
        userService.addUser(user1);

        assertDoesNotThrow(() -> {
            reviewService.addReview(Review.builder()
                    .content("Review 1")
                    .isPositive(true)
                    .filmId(film1.getId())
                    .userId(user1.getId())
                    .build());
        });
    }

    @Test
    public void reviewAlreadyExists() {
        assertThrows(ReviewAlreadyExistException.class,
                () -> {
                    reviewService.addReview(Review.builder()
                            .content("Review 1")
                            .isPositive(true)
                            .filmId(film1.getId())
                            .userId(user1.getId())
                            .build());
                });
    }

    @Test
    public void reviewCreateFailFilmId() {
        assertThrows(FilmNotFoundException.class, () -> {
            reviewService.addReview(Review.builder()
                    .content("Review 1")
                    .isPositive(true)
                    .filmId(Integer.MAX_VALUE)
                    .userId(user1.getId())
                    .build());
        });
    }

    @Test
    public void reviewCreateFailUserId() {
        assertThrows(UserNotFoundException.class, () -> {
            reviewService.addReview(Review.builder()
                    .content("Review 1")
                    .isPositive(true)
                    .filmId(film1.getId())
                    .userId(Integer.MAX_VALUE)
                    .build());
        });
    }

    @Test
    public void reviewUpdate() {
        userService.addUser(user2);

        Review review = reviewService.addReview(Review.builder()
                .content("Review ")
                .isPositive(true)
                .filmId(film1.getId())
                .userId(user2.getId())
                .build());

        String updatedContent = "Updated content";

        assertAll(
                () -> {
                    assertDoesNotThrow(() -> {
                        review.setContent(updatedContent);
                        reviewService.updateReview(review);
                    });
                },
                () -> {
                    assertEquals(reviewService.findById(review.getId()).getContent(), updatedContent);
                },
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
        filmService.addFilm(film2);
        Review review = reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film2.getId())
                .userId(user1.getId())
                .build());

        assertAll(
                () -> {
                    assertEquals(review, reviewService.findById(review.getId()));
                },
                () -> {
                    assertThrows(ReviewNotFoundException.class,
                            () -> {
                                reviewService.findById(Integer.MAX_VALUE);
                            });
                }
        );
    }

    @Test
    public void deleteReviewById() {
        filmService.addFilm(film2);
        Review review = reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film2.getId())
                .userId(user2.getId())
                .build());

        reviewService.deleteReviewById(review.getId());

        assertThrows(ReviewNotFoundException.class,
                () -> {
                    reviewService.findById(review.getId());
                }
        );
    }

    @Test
    public void getAllReviewsByFilmId() {
        filmService.addFilm(film3);

        Review review1 = reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film3.getId())
                .userId(user2.getId())
                .build());

        Review review2 = reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film3.getId())
                .userId(user1.getId())
                .build());

        List<Review> checkList = List.of(review1, review2);

        assertEquals(checkList, reviewService.getAllReviewsByFilmId(film3.getId(), 10));
    }

    @Test
    public void addLikeOrDeleteLikeReview() {
        userService.addUser(user3);
        filmService.addFilm(film3);

        Review review = reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film3.getId())
                .userId(user3.getId())
                .build());

        assertAll(
                () -> {
                    reviewService.addLikeReview(review.getId(), user3.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), 1);
                },
                () -> {
                    assertThrows(UsefulAlreadyExistsException.class,
                            () -> {
                                reviewService.addLikeReview(review.getId(), user3.getId());
                            }
                    );
                },
                () -> {
                    reviewService.deleteLikeReview(review.getId(), user3.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), 0);
                }
        );
    }

    @Test
    public void addDislikeOrDeleteDislikeReview() {
        userService.addUser(user4);
        filmService.addFilm(film3);

        Review review = reviewService.addReview(Review.builder()
                .content("getReviewById test ")
                .isPositive(true)
                .filmId(film3.getId())
                .userId(user4.getId())
                .build());

        assertAll(
                () -> {
                    reviewService.addDislikeReview(review.getId(), user4.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), -1);
                },
                () -> {
                    assertThrows(UsefulAlreadyExistsException.class,
                            () -> {
                                reviewService.addDislikeReview(review.getId(), user4.getId());
                            }
                    );
                },
                () -> {
                    reviewService.deleteDislikeReview(review.getId(), user4.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), 0);
                }
        );
    }
}