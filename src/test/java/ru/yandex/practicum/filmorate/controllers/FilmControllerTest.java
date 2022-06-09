package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.net.URI.create;
import static java.time.Duration.ofSeconds;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.model.Film.builder;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {

    private final MockMvc mockMvc;
    private final FilmService filmService;
    private final UserService userService;
    private final URI filmUrl = create("http://localhost:8080/films");

    @Test
    void test0getCommonFilmsCheckThatEndpointIsWorking() throws Exception {
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
        User user1 = User.builder().birthday(LocalDate.of(2000, 1, 1)).name("user1").login("login1").email("email1@ya.ru").build();
        User user2 = User.builder().birthday(LocalDate.of(2000, 1, 1)).name("user2").login("login2").email("email2@ya.ru").build();

        user1 = userService.addUser(user1);
        user2 = userService.addUser(user2);

        filmService.addLike(filmList.get(0).getId(), 1);
        filmService.addLike(filmList.get(0).getId(), 2);
        filmService.addLike(filmList.get(1).getId(), 1);
        filmService.addLike(filmList.get(1).getId(), 2);
        filmService.addLike(filmList.get(2).getId(), 1);
        filmService.addLike(filmList.get(3).getId(), 2);

        //common?userId={userId}&friendId={friendId}
        mockMvc.perform(get(create(filmUrl + "/common?" + "userId=" + user1.getId() + "&" + "friendId=" + user2.getId()))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}