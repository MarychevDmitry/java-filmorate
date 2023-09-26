package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final InMemoryFilmStorage filmStorage;

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        isFilmExists(film.getId());

        return filmStorage.update(film);
    }

    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.getAll().values());
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
            throw new FilmNotFoundException(String.format("Film with id %s not found.", id));
        }
    }

    private void isUserIdValid(Integer userId) {
        if (userId <= 0) {
            throw new UserNotFoundException("User id can not be negative or zero.");
        }
    }
}