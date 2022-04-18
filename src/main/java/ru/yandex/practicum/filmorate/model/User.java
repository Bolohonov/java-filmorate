package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class User {
    @NotNull
    private int id;

    @Email
    @NotNull
    private String email;

    @NotNull(message = "Login may not bee null")
    private String login;

    private String name;
    private LocalDate birthDate;
}
