package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Integer id = 1;

    protected final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Map<Integer, Film> getAll() {
        return new HashMap<>(films);
    }

    @Override
    public Film getById(Integer id) {
        return films.get(id);
    }

    private Integer generateId() {
        return id++;
    }
}