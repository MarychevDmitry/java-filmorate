package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmStorage = inMemoryFilmStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        isFilmExists(film.getId());

        return filmStorage.update(film);
    }

    public Map<Integer, Film> getFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Integer id) {
        isFilmExists(id);

        return filmStorage.getById(id);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return filmStorage
                .getAll()
                .values()
                .stream()
                .sorted((Film film1, Film film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(Integer filmId, Integer userId) {
        isFilmExists(filmId);

        filmStorage.getById(filmId).getLikes().add(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        isFilmExists(filmId);
        isUserIdValid(userId);

        filmStorage.getById(filmId).getLikes().remove(userId);
    }

    private void isFilmExists(Integer id) {
        if (filmStorage.getById(id) == null) {
            throw new NoSuchElementException(String.format("No Film with id %s.", id));
        }
    }

    private void isUserIdValid(Integer userId) {
        if (userId <= 0) {
            throw new NoSuchElementException("User id can not be negativ or zero");
        }
    }
}