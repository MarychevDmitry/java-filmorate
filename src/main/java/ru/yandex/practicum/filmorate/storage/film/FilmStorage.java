package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    public Film create(Film film);

    public Film update(Film film);

    public Map<Integer, Film> getAll();

    public Film getById(Integer id);
}
