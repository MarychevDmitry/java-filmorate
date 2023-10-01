package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.GenreStorage;
import static ru.yandex.practicum.filmorate.validator.FilmValidator.isFilmValid;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;

    public Film createFilm(Film film) {
        isFilmValid(film);

        return genreStorage.setGenre(filmStorage.createFilm(film));
    }

    public Film updateFilm(Film film) {
        isFilmValid(film);
        isFilmExists(film.getId());

        genreStorage.deleteFilmGenre(film);
        return genreStorage.setGenre(filmStorage.updateFilm(film));
    }

    public List<Film> getFilms() {
        return genreStorage.setGenres(filmStorage.getFilms());
    }

    public Film getFilmById(long filmId) {
        isFilmExists(filmId);

        return genreStorage.setGenre(filmStorage.getFilmById(filmId));
    }

    public List<Film> getFamousFilms(Long count) {
        if (count != null) {
            return getFilms().stream()
                    .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public Film addLike(long filmId, long userId) {
        isFilmExists(filmId);

        filmStorage.addLike(userId, filmId);
        log.info("Like added from User от - {} to Film - {}", userId, filmId);
        return genreStorage.setGenre(filmStorage.getFilmById(filmId));
    }

    public Film deleteLike(long filmId, long userId) {
        isFilmExists(filmId);
        isLikeFromUserExist(userId);

        filmStorage.deleteLike(userId, filmId);
        log.info("Like deleted from User - {} from Film - {}", userId, filmId);
        return genreStorage.setGenre(filmStorage.getFilmById(filmId));
    }

    private void isFilmExists(long id) {
        if (!filmStorage.checkFilmExistInBd(id)) {
            throw new FilmNotFoundException(id);
        }
    }

    private void isLikeFromUserExist(long id) {
        if (!filmStorage.checkFilmExistInBd(id)) {
            throw new LikeNotFoundException(id);
        }
    }
}
