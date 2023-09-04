package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.Film;
import static ru.yandex.practicum.filmorate.util.Constants.FIRST_FILM_BIRTHDAY;

public class FilmValidator {

    public static boolean isFilmValid(Film film) {
        return (film.getId() == null || film.getId() > 0) &&
                film.getName() != null &&
                !film.getName().isBlank() &&
                film.getName() != null &&
                !film.getDescription().isBlank() &&
                film.getDescription().length() <= 200 &&
                (film.getReleaseDate().equals(FIRST_FILM_BIRTHDAY) ||
                film.getReleaseDate().isAfter(FIRST_FILM_BIRTHDAY)) &&
                film.getDuration() > 0;
    }
}
