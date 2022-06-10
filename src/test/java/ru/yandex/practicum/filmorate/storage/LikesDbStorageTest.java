package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;

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
    private final DirectorService directorService;

    @Test
    void addLike() {
        likesDbStorage.addLike(1003, 1002);
        likesDbStorage.addLike(1003, 1004);
        likesDbStorage.addLike(1003, 1005);
        Collection<Film> films = likesDbStorage.getFilmsByLikes(2, 0, 0);
        Film[] forTest = films.toArray(new Film[films.size()]);
        assertThat(forTest[1].equals(filmDbStorage.getFilmById(1003)));
    }

    @Test
    void removeLike() {
        likesDbStorage.removeLike(1001, 1001);
        Collection<Film> films = likesDbStorage.getFilmsByLikes(3, 0, 0);
        Film[] forTest = films.toArray(new Film[films.size()]);
        assertThat(forTest[2].equals(filmDbStorage.getFilmById(1001)));
    }

    @Test
    void getFilmsByLikes() {
        Collection<Film> films = likesDbStorage.getFilmsByLikes(1,0, 0);
        int length = films.toArray().length;
        assertEquals(1, length);
        assertThat(films.contains(filmDbStorage.getFilmById(1005)));
    }

    @Test
    void getFilmsByLikesGenreAndYear() {
        List<Film> list = (List<Film>) likesDbStorage.getFilmsByLikes(3,1, 2001);
        assertEquals(1001, list.get(0).getId());
    }

    @Test
    void getFilmsByLikesGenre() {
        List<Film> list = (List<Film>) likesDbStorage.getFilmsByLikes(5,2, 0);
        assertEquals(1007, list.get(0).getId());
    }

    @Test
    void getFilmsByLikesYear() {
        List<Film> list = (List<Film>) likesDbStorage.getFilmsByLikes(5,0, 2004);
        assertEquals(1003, list.get(0).getId());
        assertEquals(1005, list.get(1).getId());
    }

    @Test
    void getFilmsByLikesYearEmpty() {
        assertEquals(0, likesDbStorage.getFilmsByLikes(5,3, 3).size());

    }
  
    @Test
    void getRecommendations() {
        assertThat(likesDbStorage.getRecommendations(1001).contains(filmDbStorage.getFilmById(1005)));
    };

}