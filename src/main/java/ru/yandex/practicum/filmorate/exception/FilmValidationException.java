package ru.yandex.practicum.filmorate.exception;

import javax.validation.ValidationException;

public class FilmValidationException extends ValidationException {

    public FilmValidationException(String message) {
        super(message);
    }
}
