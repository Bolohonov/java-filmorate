package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Director;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DirectorServiceTest {

    private final DirectorService directorService;

    @Test
    void test0createDirector() {
        Director director = Director.builder().name("Dir1").build();
        directorService.createDirector(director);
        assertThat(directorService.findAllDirectors()).hasSize(1);
    }

    @Test
    void test10findDirectorById() {
        Director director = Director.builder().name("Dir1").build();
        director = directorService.createDirector(director);
        assertThat(directorService.findDirectorById(director.getId())).isNotEmpty();
    }

    @Test
    void test20updateDirector() {
        Director director = Director.builder().name("Dir1").build();
        directorService.createDirector(director);
        director.setName("NewName");

        assertThat(directorService.updateDirector(director).get().getName()).isEqualTo(director.getName());
    }

    @Test
    void test30findAllDirectors() {
        Director director1 = Director.builder().name("Dir1").build();
        directorService.createDirector(director1);
        Director director2 = Director.builder().name("Dir2").build();
        directorService.createDirector(director2);
        Director director3 = Director.builder().name("Dir3").build();
        directorService.createDirector(director3);

        assertThat(directorService.findAllDirectors()).hasSize(3);
    }

    @Test
    void test40deleteDirector() {
        Director director1 = Director.builder().name("Dir1").build();
        director1 = directorService.createDirector(director1);
        Director director2 = Director.builder().name("Dir2").build();
        director2 = directorService.createDirector(director2);

        assertThat(directorService.findAllDirectors()).hasSize(2);

        assertThat(directorService.deleteDirector(director1.getId())).isTrue();

        assertThat(directorService.findAllDirectors()).hasSize(1);
    }
}