package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.User;
import static ru.yandex.practicum.filmorate.validator.PatternValidator.isPatternMatches;
import static ru.yandex.practicum.filmorate.util.Constants.EMAIL_REGEX_PATTERN;
import static ru.yandex.practicum.filmorate.util.Constants.LOGIN_REGEX_PATTERN;

import java.time.LocalDate;

public class UserValidator {
    public static boolean isUserValid(User user) {
        return (user.getId() == null || user.getId() > 0) &&
                isPatternMatches(user.getEmail(), EMAIL_REGEX_PATTERN) &&
                isPatternMatches(user.getLogin(), LOGIN_REGEX_PATTERN) &&
                user.getBirthday().isBefore(LocalDate.now());
    }

    public static boolean isUserNameValid(String userName) {
        return !userName.isBlank();
    }
}
