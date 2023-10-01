package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(String message) {
        super(message);
    }

    public FilmNotFoundException(long id) {
        super(String.format("Film with id %s not found.", id));
    }
}
