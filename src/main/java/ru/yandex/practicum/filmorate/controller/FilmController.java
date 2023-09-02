package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}. \n Список всех фильмов: {}", films.size(), films);

        return new ArrayList<Film>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(getId());
        films.put(film.getId(), film);
        log.debug("Добавлен новый фильм: {}.", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Запрос на обновления фильма с id: {}.", film.getId());
        if (!films.containsKey(film.getId())) {
            throw new UpdateException("Фильм с id: " + film.getId() + " не найден.");
        }
        films.put(film.getId(), film);
        log.debug("Обновлен фильм с id: {}. Фильм: {}", film.getId(), film);
        return film;
    }

    public int getId() {
        return id++;
    }
}
