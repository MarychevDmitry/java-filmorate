package ru.yandex.practicum.filmorate.exception;

public class MpaNotFoundException extends RuntimeException {
    public MpaNotFoundException(String message) {
        super(message);
    }

    public MpaNotFoundException(long id) {
        super(String.format("Mpa with id %s. not found.", id));
    }
}
