package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Friends {

    private int firstUserId;
    private int secondUserId;
    private boolean accept;

}
