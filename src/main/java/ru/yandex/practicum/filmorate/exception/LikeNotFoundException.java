package ru.yandex.practicum.filmorate.exception;

public class LikeNotFoundException extends RuntimeException {

    public LikeNotFoundException(String message) {
        super(message);
    }

    public LikeNotFoundException(long id) {
        super(String.format("Like from User with id %s. not found", id));
    }
}