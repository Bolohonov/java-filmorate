package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
        Collection<Film> films = likesDbStorage.getFilmsByLikes(1, 0, 0);
        Film[] forTest = films.toArray(new Film[films.size()]);
        assertThat(forTest[0].equals(filmDbStorage.getFilmById(30)));
    }

    @Test
    void removeLike() {
        likesDbStorage.removeLike(10, 40);
        Collection<Film> films = likesDbStorage.getFilmsByLikes(1, 0, 0);
        assertThat(films.contains(filmDbStorage.getFilmById(40)));
    }

    @Test
    void getFilmsByLikes() {
        Collection<Film> films = likesDbStorage.getFilmsByLikes(1,0, 0);
        int length = films.toArray().length;
        assertEquals(1, length);
        assertThat(films.contains(filmDbStorage.getFilmById(10)));
    }

    @Test
    void getFilmsByLikesGenreAndYear() {
        List<Film> list = (List<Film>) likesDbStorage.getFilmsByLikes(5,1, 2001);
        assertEquals(10, list.get(0).getId());
    }

    @Test
    void getFilmsByLikesGenre() {
        List<Film> list = (List<Film>) likesDbStorage.getFilmsByLikes(5,2, 0);
        assertEquals(20, list.get(0).getId());
    }

    @Test
    void getFilmsByLikesYear() {
        List<Film> list = (List<Film>) likesDbStorage.getFilmsByLikes(5,0, 2004);
        assertEquals(30, list.get(0).getId());
        assertEquals(40, list.get(1).getId());
        assertEquals(50, list.get(2).getId());
    }

    @Test
    void getFilmsByLikesYearEmpty() {
        assertEquals(0, likesDbStorage.getFilmsByLikes(5,3, 3).size());

    }
}