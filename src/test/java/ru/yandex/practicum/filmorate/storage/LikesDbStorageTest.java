package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikesDbStorageTest {
    private final LikesDbStorage likesDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    void addLike() {
        likesDbStorage.addLike(30, 20);
        likesDbStorage.addLike(30, 40);
        likesDbStorage.addLike(30, 50);
        Collection<Film> films = likesDbStorage.getFilmsByLikes(1);
        Film[] forTest = films.toArray(new Film[films.size()]);
        assertThat(forTest[0].equals(filmDbStorage.getFilmById(30)));
    }

    @Test
    void removeLike() {
        likesDbStorage.removeLike(10, 40);
        Collection<Film> films = likesDbStorage.getFilmsByLikes(1);
        assertThat(films.contains(filmDbStorage.getFilmById(40)));
    }

    @Test
    void getFilmsByLikes() {
        Collection<Film> films = likesDbStorage.getFilmsByLikes(1);
        int length = films.toArray().length;
        assertTrue(length == 1);
        assertThat(films.contains(filmDbStorage.getFilmById(10)));
    }
}