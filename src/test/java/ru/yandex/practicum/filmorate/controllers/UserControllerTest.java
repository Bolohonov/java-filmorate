package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController controller = new UserController();

    @Test
    void createStandardBehavior() {
        User user = new User();
        user.setId(1);
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthDate(LocalDate.of(1999,12,15));
        user.setEmail("ivan111@yandex.ru");
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void createDuplicatedEmail() {
        User user = new User();
        user.setId(1);
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthDate(LocalDate.of(1999,12,15));
        user.setEmail("ivan333@yandex.ru");
        controller.create(user);
        User user2 = new User();
        user2.setId(2);
        user2.setLogin("IvanTest2");
        user2.setName("IvanIvan");
        user2.setBirthDate(LocalDate.of(1999,12,15));
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
        user.setId(1);
        user.setLogin("Ivan Test");
        user.setName("Ivan");
        user.setBirthDate(LocalDate.of(1999,12,15));
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
        user.setId(1);
        user.setLogin("IvanTest");
        user.setBirthDate(LocalDate.of(1999,12,15));
        user.setEmail("ivan@yandex.ru");
        controller.create(user);
        User user2 = new User();
        user2.setId(1);
        user2.setLogin("IvanTest");
        user2.setName("IvanTest");
        user2.setBirthDate(LocalDate.of(1999,12,15));
        user2.setEmail("ivan@yandex.ru");
        assertTrue(controller.findAll().contains(user2));
    }

    @Test
    void createIncorrectBirthDate() {
        User user = new User();
        user.setId(1);
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthDate(LocalDate.of(2022,12,15));
        user.setEmail("ivan222@yandex.ru");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );
        assertEquals("Указана неверная дата рождения.",
                exception.getMessage());
    }

//    @Test
//    void createIncorrectEmail() {
//        controller.findAll().clear();
//        User user = new User();
//        user.setId(10);
//        user.setLogin("IvanTest");
//        user.setName("Ivan");
//        user.setBirthDate(LocalDate.of(2000,12,15));
//        user.setEmail("ivan222yandex.ru");
//        final MethodArgumentNotValidException exception = assertThrows(
//                MethodArgumentNotValidException.class,
//                () -> controller.create(user)
//        );
//        assertEquals("Указана неверная дата рождения.",
//                exception.getMessage());
//    }

//    @Test
//    void createNullEmail() {
//        controller.findAll().clear();
//        User user = new User();
//        user.setId(11);
//        user.setLogin("IvanTest");
//        user.setName("Ivan");
//        user.setBirthDate(LocalDate.of(2000,12,15));
//        controller.create(user);
//        controller.findAll().forEach(System.out::println);
////        final NullPointerException exception = assertThrows(
////                NullPointerException.class,
////                () -> controller.create(user)
////        );
////        assertEquals("Cannot invoke \"String.contains(java.lang.CharSequence)\" " +
////                        "because the return value of \"ru.yandex.practicum.filmorate.model" +
////                        ".User.getLogin()\" is null",
////                exception.getMessage());
//    }

//    @Test
//    void createNullLogin() {
//        controller.findAll().clear();
//        User user = new User();
//        user.setId(11);
//        user.setName("Ivan");
//        user.setBirthDate(LocalDate.of(2000,12,15));
//        user.setEmail("ivan222@yandex.ru");
//        final NullPointerException exception = assertThrows(
//                NullPointerException.class,
//                () -> controller.create(user)
//        );
//        assertEquals("Login may not bee null",
//                exception.getMessage());
//    }

//    @Test
//    void createNullId() {
//        controller.findAll().clear();
//        User user = new User();
//        user.setName("Ivan");
//        user.setLogin("IvanTest");
//        user.setBirthDate(LocalDate.of(2000,12,15));
//        user.setEmail("ivan222@yandex.ru");
//        final NullPointerException exception = assertThrows(
//                NullPointerException.class,
//                () -> controller.create(user)
//        );
//        assertEquals("Cannot invoke \"String.contains(java.lang.CharSequence)\" " +
//                        "because the return value of \"ru.yandex.practicum.filmorate.model" +
//                        ".User.getLogin()\" is null",
//                exception.getMessage());
//    }

    @Test
    void createDuplicatedId() {
        controller.findAll().clear();
        User user = new User();
        user.setId(1);
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthDate(LocalDate.of(1999,12,15));
        user.setEmail("ivan777@yandex.ru");
        controller.create(user);
        User user2 = new User();
        user2.setId(1);
        user2.setLogin("IvanTest2");
        user2.setName("IvanIvan");
        user2.setBirthDate(LocalDate.of(1999,12,15));
        user2.setEmail("ivan333@yandex.ru");
        controller.create(user2);
        User user3 = new User();
        user3.setId(2);
        user3.setLogin("IvanTest2");
        user3.setName("IvanIvan");
        user3.setBirthDate(LocalDate.of(1999,12,15));
        user3.setEmail("ivan333@yandex.ru");
        assertTrue(controller.findAll().contains(user3));

    }

    @Test
    void putStandardBehavior() {
        User user = new User();
        user.setId(1);
        user.setLogin("IvanTest");
        user.setName("Ivan");
        user.setBirthDate(LocalDate.of(1999,12,15));
        user.setEmail("ivan555@yandex.ru");
        controller.create(user);
        user.setLogin("New");
        controller.put(user);
        assertTrue(controller.findAll().contains(user));
    }
}