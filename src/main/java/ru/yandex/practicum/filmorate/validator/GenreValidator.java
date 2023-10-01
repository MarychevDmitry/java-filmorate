package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.GenreValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreValidator {

    public static boolean isGenreValid(Genre genre) {
        if (genre.getName().isBlank()) {
            throw new GenreValidationException(String.format("Genre name can not be blank. %s", genre));
        }
        return true;
    }
}
