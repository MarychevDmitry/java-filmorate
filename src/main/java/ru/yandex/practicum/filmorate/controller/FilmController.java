package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import static ru.yandex.practicum.filmorate.validator.FilmValidator.isFilmValid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        isFilmValid(film);
        log.info(String.format("Created new %s.", film));
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        isFilmValid(film);
        filmService.updateFilm(film);
        log.info(String.format("Updated Film with id: %s. %s.", film.getId(), film));
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info(String.format("Get all Films. Total amount of Films: %s", filmService.getFilms().size()));
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) {
        log.info(String.format("Get Film with id: %s. %s.", id, filmService.getFilmById(id)));
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(name = "count", defaultValue = "10", required = false) Integer count) {
        log.info(String.format("Get top %s most popular Films.", count));
        return filmService.getMostPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.addLike(id, userId);
        log.info(String.format("User with id: %s added Like to Film id: %s", userId, id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.deleteLike(id, userId);
        log.info(String.format("User with id: %s deleted Like from Film id: %s", userId, id));
    }
}