package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikesDbStorageTest {
    private final LikesDbStorage likesDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    void addLike() {
        likesDbStorage.addLike(1, 4);
        likesDbStorage.addLike(2, 4);
        likesDbStorage.addLike(3, 4);
        likesDbStorage.addLike(4, 4);
        Collection<Film> films = likesDbStorage.getFilmsByLikes(1);
        assertThat(films.contains(filmDbStorage.getFilmById(4)));
    }

    @Test
    void removeLike() {
    }

    @Test
    void getFilmsByLikes() {
        Collection<Film> films = likesDbStorage.getFilmsByLikes(1);
        assertThat(films.contains(filmDbStorage.getFilmById(1)));
    }
}