package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class User {
    private int id;

    @Email
    @NotNull
    private String email;

    @NotBlank(message = "Login may not bee null")
    private String login;

    private String name;
    private LocalDate birthday;
}
