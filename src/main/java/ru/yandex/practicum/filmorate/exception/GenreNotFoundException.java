package ru.yandex.practicum.filmorate.exception;


public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException(String message) {
        super(message);
    }

    public GenreNotFoundException(long id) {
        super(String.format("Genre with id %s. not found.", id));
    }
}
