package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaStorage;
    private final FilmDbStorage filmStorage;

    @Test
    void getNewMpaObject() {
        Mpa mpa = filmStorage.getFilmById(30).get().getMpa();
        Mpa newMpa = mpaStorage.getNewMpaObject(4);
        assertTrue(mpa.getId() == newMpa.getId());
    }
}