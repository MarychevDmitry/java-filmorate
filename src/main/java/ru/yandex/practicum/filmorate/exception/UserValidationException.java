package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class UserValidationException extends ValidationException {

    public UserValidationException(String message) {
        super(message);
    }
}
