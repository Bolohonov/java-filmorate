package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.Duration.ofSeconds;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.model.Film.builder;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmServiceTest {

    private final FilmService filmService;
    private final UserService userService;

    @Test
    void test0getCommonFilmsIf2CommonFilmsExistingShouldReturn2FilmsFromList() {
        List<Film> filmList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            filmList.add(filmService.addFilm(builder()
                    .id(i)
                    .name("Film" + i)
                    .description("Descr" + i)
                    .releaseDate(of(2021, 12, 2))
                    .duration(ofSeconds(1500))
                    .mpa(new Mpa(1, "G"))
                    .build()));
        }
        User user1 = User.builder().birthday(LocalDate.of(2000,1,1)).name("user1").login("login1").email("email1@ya.ru").build();
        User user2 = User.builder().birthday(LocalDate.of(2000,1,1)).name("user2").login("login2").email("email2@ya.ru").build();
        User user3 = User.builder().birthday(LocalDate.of(2000,1,1)).name("user3").login("login3").email("email3@ya.ru").build();

        user1 = userService.addUser(user1);
        user2 = userService.addUser(user2);
        user3 = userService.addUser(user3);

        filmService.addLike(filmList.get(0).getId(),1);
        filmService.addLike(filmList.get(0).getId(),2);
        filmService.addLike(filmList.get(0).getId(),3);
        filmService.addLike(filmList.get(1).getId(),1);
        filmService.addLike(filmList.get(1).getId(),2);
        filmService.addLike(filmList.get(2).getId(),1);
        filmService.addLike(filmList.get(3).getId(),2);

        assertThat(filmService.getCommonFilms(user1.getId(), user2.getId())).hasSize(2);
    }

    @Test
    void test1getCommonFilmsIfCommonFilmsNotExistingShouldReturn0FilmsFromList() {
        List<Film> filmList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            filmList.add(filmService.addFilm(builder()
                    .id(i)
                    .name("Film" + i)
                    .description("Descr" + i)
                    .releaseDate(of(2021, 12, 2))
                    .duration(ofSeconds(1500))
                    .mpa(new Mpa(1, "G"))
                    .build()));
        }
        User user1 = User.builder().birthday(LocalDate.of(2000,1,1)).name("user1").login("login1").email("email1@ya.ru").build();
        User user2 = User.builder().birthday(LocalDate.of(2000,1,1)).name("user2").login("login2").email("email2@ya.ru").build();
        User user3 = User.builder().birthday(LocalDate.of(2000,1,1)).name("user3").login("login3").email("email3@ya.ru").build();

        user1 = userService.addUser(user1);
        user2 = userService.addUser(user2);
        user3 = userService.addUser(user3);

        filmService.addLike(filmList.get(0).getId(),1);
        filmService.addLike(filmList.get(0).getId(),2);
        filmService.addLike(filmList.get(1).getId(),1);
        filmService.addLike(filmList.get(1).getId(),2);

        assertThat(filmService.getCommonFilms(user2.getId(), user3.getId())).hasSize(0);
    }
}