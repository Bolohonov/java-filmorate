package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
@Builder
@AllArgsConstructor
public class User {
    private final Set<Integer> friends = new HashSet<>();
    private int id;

    @Email
    @NotNull
    private String email;

    @NotBlank(message = "Login may not bee null")
    private String login;

    private String name;
    private LocalDate birthday;
}
