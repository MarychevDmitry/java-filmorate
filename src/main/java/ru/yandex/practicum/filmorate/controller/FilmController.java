package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;

import static ru.yandex.practicum.filmorate.validator.FilmValidator.isFilmValid;

import javax.validation.Valid;
import javax.validation.ValidationException;
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
        log.info("Request: List of all Films.\nCurrent number of Films: {}.\nList of all Films: {}", films.size(), films);

        return new ArrayList<Film>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Request: Adding  new Film.");
        if (!isFilmValid(film)) {
            throw new ValidationException("Film validation Error.");
        }
        film.setId(getId());
        films.put(film.getId(), film);
        log.info("New Film added: {}.", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Request: Update Film with id: {}.", film.getId());
        if (!isFilmValid(film)) {
            throw new ValidationException("Film validation Error.");
        }
        if (!films.containsKey(film.getId())) {
            throw new UpdateException("Film with id: " + film.getId() + " not found.");
        }
        films.put(film.getId(), film);
        log.info("Updated Film with id: {}. Film: {}", film.getId(), film);
        return film;
    }

    private int getId() {
        return id++;
    }
}
