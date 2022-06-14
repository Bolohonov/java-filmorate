package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("reviewDBStorage")
@Slf4j
public class ReviewDBStorage implements ReviewStorage {
    private static final String SQL_FOR_UPDATE_REVIEW = "UPDATE reviews SET content = ?, is_positive = ?" +
            "WHERE review_id = ?";
    private static final String SQL_FOR_FIND_BY_ID = "SELECT * FROM reviews WHERE review_id = ?";
    private static final String SQL_FOR_GET_REVIEW_USEFUL_BY_ID = "SELECT SUM(useful) AS useful_sum " +
            "FROM review_usefulS WHERE review_id = ?";
    private static final String SQL_FOR_ADD_REVIEW_USEFUL = "INSERT INTO review_usefuls (review_id, user_id, useful) " +
            "VALUES (?, ?, ?);";
    private static final String SQL_FOR_GET_ALL_REVIEWS_BY_FILM_ID = "SELECT * FROM reviews WHERE film_id = ? LIMIT ?";
    private static final String SQL_FOR_DELETE_REVIEW_USEFUL = "DELETE FROM review_usefuls WHERE review_id = ? " +
            "AND user_id = ? AND useful = ?";
    private static final String SQL_FOR_DELETE_REVIEW_BY_ID = "DELETE FROM reviews WHERE review_id = ?";


    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public ReviewDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review add(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("review_id");

        Map<String, Object> reviewValues = new HashMap<>();
        reviewValues.put("user_id", review.getUserId());
        reviewValues.put("film_id", review.getFilmId());
        reviewValues.put("content", review.getContent());
        reviewValues.put("is_positive", review.isPositive());

        try {
            int reviewId = simpleJdbcInsert.executeAndReturnKey(reviewValues).intValue();
            review.setId(reviewId);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMessage();
            if (message.contains("FK_REVIEWS_USER_IDENTIFIER")) {
                throw new UserNotFoundException("User with id:" + review.getUserId() + " not found");
            } else if (message.contains("FK_REVIEWS_FILM_IDENTIFIER")) {
                throw new FilmNotFoundException("Film with id:" + review.getFilmId() + " not found");
            } else if (message.contains("UQ_REVIEWS_COMBINATION_USER_IDENTIFIER_AND_FILM_IDENTIFIER")) {
                throw new ReviewAlreadyExistException("Review from user with id:" + review.getUserId() +
                        " for film with id:" + review.getFilmId() + " already exists");
            }
        }

        log.info("Review from user with id:{} added", review.getUserId());

        return review;
    }

    @Override
    public Review update(Review review) {
        int queryResult = jdbcTemplate.update(
                SQL_FOR_UPDATE_REVIEW,
                review.getContent(),
                review.isPositive(),
                review.getId()
        );
        if (queryResult == 0) {
            throw new ReviewNotFoundException("Review with id:" + review.getId() + " not found");
        }
        log.info("Review with id:{} updated", review.getId());

        return review;
    }

    @Override
    public Map<String, String> deleteReviewById(int id) {
        int deleteResult = jdbcTemplate.update(SQL_FOR_DELETE_REVIEW_BY_ID, id);

        if (deleteResult > 0) {
            log.info("Review with id:{} deleted", id);

            return Map.of("message", "review with id:" + id + " deleted");
        } else {
            throw new ReviewNotFoundException("Review with id:" + id + " not found");
        }
    }


    @Override
    public Review findById(int id) {
        try {
            return jdbcTemplate.queryForObject(SQL_FOR_FIND_BY_ID, this::mapRowToReview, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ReviewNotFoundException("Review with id:" + id + " not found");
        }
    }

    private Review mapRowToReview(ResultSet rs, int rowNum) throws SQLException {
        int reviewId = rs.getInt("review_id");

        return Review.builder()
                .id(reviewId)
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .useful(getReviewUsefulById(reviewId))
                .build();
    }

    private Integer getReviewUsefulById(int id) {
        return jdbcTemplate.queryForObject(SQL_FOR_GET_REVIEW_USEFUL_BY_ID,
                (ResultSet rs, int rowNum) -> rs.getInt("useful_sum"),
                id);
    }

    @Override
    public List<Review> getAllReviewsByFilmId(int filmId, int count) {
        return jdbcTemplate.query(SQL_FOR_GET_ALL_REVIEWS_BY_FILM_ID, this::mapRowToReview, filmId, count);
    }

    @Override
    public Review addReviewUseful(int reviewId, int userId, int value) {
        try {
            jdbcTemplate.update(SQL_FOR_ADD_REVIEW_USEFUL, reviewId, userId, value);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMessage();

            if (message.contains("PRIMARY_KEY")) {
                throw new UsefulAlreadyExistsException("User's reaction to the review already exists");
            } else if (message.contains("FK_REVIEW_USEFULS_USER_IDENTIFIER")) {
                throw new UserNotFoundException("User with id:" + userId + " not found");
            } else if (message.contains("FK_REVIEW_USEFULS_REVIEW_IDENTIFIER")){
                throw new ReviewNotFoundException("Review with id:" + reviewId + " not found");
            }
        }

        return findById(reviewId);
    }

    @Override
    public Review deleteReviewUseful(int reviewId, int userId, int value) {
        int deleteResult = jdbcTemplate.update(SQL_FOR_DELETE_REVIEW_USEFUL, reviewId, userId, value);

        if (deleteResult == 0) {
            throw new UsefulNotFoundException("User's reaction to the review not found");
        }

        return findById(reviewId);
    }
}
