package ru.yandex.practicum.filmorate.util;

import java.time.LocalDate;

public class Constants {
    public static final String LOGIN_REGEX_PATTERN = "^[a-zA-Z0-9._-]{1,}$";
    public static final String EMAIL_REGEX_PATTERN = "[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})";
    public static final LocalDate FIRST_FILM_BIRTHDAY = LocalDate.parse("1895-12-28");
}
