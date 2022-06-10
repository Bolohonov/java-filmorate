package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {

    private final FilmDbStorage filmStorage;
    private final MpaStorage mpaStorage;

    @Test
    void getAllMpa() {
        Collection<Mpa> list = mpaStorage.getAllMpa();
        assertThat(list.size() == 5);
    }

    @Test
    void getMpaById() {
        Mpa mpa = filmStorage.getFilmById(1003).get().getMpa();
        Mpa newMpa = mpaStorage.getMpaById(4).get();
        assertTrue(mpa.getId() == newMpa.getId());
    }
}