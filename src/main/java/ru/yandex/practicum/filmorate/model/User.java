package ru.yandex.practicum.filmorate.model;

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

    public void addFriend(int id) {
        friends.add(id);
        log.warn("Friend with ID {} has been added", id);
    }

    public Set<Integer> getFriends() {
        log.warn("Get all friends of {}", this.login);
        return friends;
    }

    public void removeFriend(int id) {
        friends.remove(id);
        log.warn("Friend with ID {} has been removed", id);
    }
}
