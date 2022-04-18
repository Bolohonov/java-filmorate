package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class User {
    @NotNull
    @Positive
    private int id;

    @Email
    @NotNull
    private String email;

    @NotBlank(message = "Login may not bee null")
    private String login;

    private String name;
    private LocalDate birthDate;
}
