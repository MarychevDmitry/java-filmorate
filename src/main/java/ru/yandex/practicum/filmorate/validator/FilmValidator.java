package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import static ru.yandex.practicum.filmorate.util.Constants.FIRST_FILM_BIRTHDAY;

public class FilmValidator {
    public static boolean isFilmValid(Film film) {
        if (film.getId() != null && film.getId() <= 0) {
            throw new FilmValidationException(String.format("Id should be positive or null. %s.", film));
        } else if (film.getName().isBlank()) {
            throw new FilmValidationException(String.format("Name can't be blank. %s.", film));
        } else if (film.getDescription().isBlank() || film.getDescription().length() >= 200) {
            throw new FilmValidationException(String.format("Description can not be blank and it's length must be below 200. %s.", film));
        } else if (film.getReleaseDate() == null) {
            throw new FilmValidationException("FIlm release date can not be NULL.");
        } else if (film.getReleaseDate().isBefore(FIRST_FILM_BIRTHDAY)) {
            throw new FilmValidationException(String.format("Film release date can't be before %s. %s.", FIRST_FILM_BIRTHDAY, film));
        } else if (film.getDuration() < 1) {
            throw new FilmValidationException(String.format("Duration should be positive. %s.", film));
        } else if (film.getMpa() == null) {
            throw new FilmValidationException("Film MPA can not be NULL.");
        } else {
            return true;
        }
    }
}
