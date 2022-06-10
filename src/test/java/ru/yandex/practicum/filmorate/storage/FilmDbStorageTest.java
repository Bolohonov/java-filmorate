package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

import static java.time.Duration.ofSeconds;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmStorage filmDbStorage;
    private final FilmService filmService;
    private final DirectorService directorService;
    private final MpaDbStorage mpaDbStorage;

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
    void testGetFilms() {
        List<Film> filmList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            filmList.add(filmService.addFilm(Film.builder()
                    .name("Film" + i)
                    .description("Descr" + i)
                    .releaseDate(of(2021, 12, 2))
                    .duration(ofSeconds(1500))
                    .mpa(new Mpa(1, "G"))
                    .likes(new HashSet<>())
                    .build()));
        }
        Collection<Film> films = filmDbStorage.getFilms();
        assertThat(films).contains(filmList.get(0));
        assertThat(films).contains(filmList.get(1));
        assertThat(films).contains(filmList.get(2));
        assertThat(films).contains(filmList.get(3));
        assertThat(films).contains(filmList.get(4));
    }

    @Test
    void testAddFilm() {
        Film newFilm = Film.builder()
                .name("Film")
                .description("Descr")
                .releaseDate(of(2021, 12, 2))
                .duration(ofSeconds(1100))
                .mpa(new Mpa(1, "G"))
                .likes(new HashSet<>())
                .build();
        filmDbStorage.addFilm(newFilm);
        assertThat(filmDbStorage.getFilms().contains(newFilm));
    }

    @Test
    void testDeleteFilm() {
        filmDbStorage.deleteFilm(1008);
        assertThrows(
                FilmNotFoundException.class,
                () -> filmDbStorage.getFilmById(1008)
        );
    }

    @Test
    void testUpdateFilm() {
        Film newFilm = Film.builder()
                .name("FilmForTest")
                .description("DescrForTest")
                .releaseDate(of(2020, 12, 2))
                .duration(ofSeconds(1200))
                .mpa(new Mpa(1, "G"))
                .likes(new HashSet<>())
                .build();
        filmDbStorage.addFilm(newFilm);
        newFilm.setName("NewTestFilm");
        filmDbStorage.updateFilm(newFilm);
        assertThat(filmDbStorage.getFilms().contains(newFilm));
    }

    @Test
    void testGetFilmById() {
        Optional<Film> filmOptional = filmDbStorage.getFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void testSearchSort() {
        ArrayList<Film> list = (ArrayList<Film>) filmDbStorage.search("Film", "title");
        System.out.println(list.get(0));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");


        assertThat(list.get(0)).hasFieldOrPropertyWithValue("name","Film5Name");
    }

    @Test
    void testSearchStandard() {
        Director director = Director.builder().name("DirForTest").build();
        directorService.createDirector(director);
        List<Film> filmList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            filmList.add(filmService.addFilm(Film.builder()
                    .name("FilmforSearchTest" + i)
                    .description("Descr" + i)
                    .releaseDate(of(2021, 12, 2))
                    .duration(ofSeconds(1500))
                    .mpa(new Mpa(1, "G"))
                    .director(director)
                    .likes(new HashSet<>())
                    .build()));
        }

        ArrayList<Film> list = (ArrayList<Film>) filmDbStorage.search("FilmforSearch", "title");

        assertThat(list.get(0)).hasFieldOrPropertyWithValue("name","FilmforSearchTest0");
        assertThat(list.get(1)).hasFieldOrPropertyWithValue("name","FilmforSearchTest1");
        assertThat(list.get(2)).hasFieldOrPropertyWithValue("name","FilmforSearchTest2");
        assertThat(list.get(3)).hasFieldOrPropertyWithValue("name","FilmforSearchTest3");
        assertThat(list.get(4)).hasFieldOrPropertyWithValue("name","FilmforSearchTest4");
    }

    @Test
    void testSearchEmpty() {
        Assertions.assertEquals(0, filmDbStorage.search("not a movie", "title").size());
    }

    @Test
    void testSearchTitleDirector() {
        Director director = Director.builder().name("filIvanov").build();
        directorService.createDirector(director);
        Film newFilm = Film.builder()
                .name("FilmForSearchTestDirector")
                .description("DescrForTest")
                .releaseDate(of(2020, 12, 2))
                .duration(ofSeconds(1200))
                .mpa(new Mpa(1, "G"))
                .director(director)
                .likes(new HashSet<>())
                .build();
        filmService.addFilm(newFilm);
        ArrayList<Film> list = (ArrayList<Film>) filmDbStorage.search("fil", "title,director");
        assertThat(list.get(0)).hasFieldOrPropertyWithValue("name","FilmForSearchTestDirector");
    }

    @Test
    void testSearchDirector() {
        Director director = Director.builder().name("ДИванов").build();
        directorService.createDirector(director);
        List<Film> filmList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            filmList.add(filmService.addFilm(Film.builder()
                    .name("FilmforSearchTest" + i)
                    .description("Descr" + i)
                    .releaseDate(of(2021, 12, 2))
                    .duration(ofSeconds(1500))
                    .mpa(new Mpa(1, "G"))
                    .director(director)
                    .likes(new HashSet<>())
                    .build()));
        }
        ArrayList<Film> list = (ArrayList<Film>) filmDbStorage.search("ДИван", "director");
        Assertions.assertEquals(5, list.size());
        assertThat(list.get(0)).hasFieldOrPropertyWithValue("name","FilmforSearchTest0");
    }
}