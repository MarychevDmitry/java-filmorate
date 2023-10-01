package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(long id);

    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    List<Film> setLikesInFilm(List<Film> films);

    Set<Integer> getLikesByFilmId(long filmId);

    boolean checkFilmExistInBd(long id);
}
