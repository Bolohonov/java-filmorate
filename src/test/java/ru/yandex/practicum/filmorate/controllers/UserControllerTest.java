package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private static final UserController controller = new UserController(new UserStorage());

    @Test
    void createStandardBehavior() {
        User user = new User();
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthday(LocalDate.of(1999, 12, 15));
        user.setEmail("ivan111@yandex.ru");
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void createDuplicatedEmail() {
        User user = new User();
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthday(LocalDate.of(1999, 12, 15));
        user.setEmail("ivan333@yandex.ru");
        controller.create(user);
        User user2 = new User();
        user2.setLogin("IvanTest2");
        user2.setName("IvanIvan");
        user2.setBirthday(LocalDate.of(1999, 12, 15));
        user2.setEmail("ivan333@yandex.ru");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user2)
        );
        assertEquals("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.",
                exception.getMessage());
    }

    @Test
    void createWhitespacesInLogin() {
        User user = new User();
        user.setLogin("Ivan Test");
        user.setName("Ivan");
        user.setBirthday(LocalDate.of(1999, 12, 15));
        user.setEmail("ivan@yandex.ru");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        assertEquals("Логин не может содержать пробелы.",
                exception.getMessage());
        user.setLogin(" IvanTest");
        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        assertEquals("Логин не может содержать пробелы.",
                exception2.getMessage());
        user.setLogin("IvanTest ");
        final ValidationException exception3 = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        assertEquals("Логин не может содержать пробелы.",
                exception3.getMessage());
    }

    @Test
    void createNameIsNull() {
        controller.findAll().clear();
        User user = new User();
        user.setLogin("IvanTest");
        user.setBirthday(LocalDate.of(1999, 12, 15));
        user.setEmail("ivan@yandex.ru");
        controller.create(user);
        String actualName = "";
        for (User u : controller.findAll()) {
            if (u.getLogin().equals("IvanTest")) {
                actualName = u.getName();
            }
        }
        assertEquals("IvanTest", actualName);
    }

    @Test
    void createIncorrectBirthDate() {
        User user = new User();
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthday(LocalDate.of(2022, 12, 15));
        user.setEmail("ivan222@yandex.ru");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        assertEquals("Указана неверная дата рождения.",
                exception.getMessage());
    }

    @Test
    void putStandardBehavior() {
        User user = new User();
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthday(LocalDate.of(1999, 12, 15));
        user.setEmail("ivan555@yandex.ru");
        controller.create(user);
        user.setLogin("New");
        controller.put(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void putWrongId() {
        User user = new User();
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthday(LocalDate.of(1999, 12, 15));
        user.setEmail("ivan555@yandex.ru");
        controller.create(user);
        user.setId(-5);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.put(user)
        );
        assertEquals("ID должен быть положительным.",
                exception.getMessage());
    }
}