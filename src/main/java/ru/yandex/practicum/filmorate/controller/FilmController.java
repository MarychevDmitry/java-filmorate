package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info(String.format("Created new %s", film));
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info(String.format("Updated Film with id: %s %s", film.getId(), film));
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info(String.format("Get all Films. Total amount of Films: %s", filmService.getFilms().size()));
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable long filmId) {
        log.info(String.format("Get Film with id: %s %s", filmId, filmService.getFilmById(filmId)));
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilm(@RequestParam(name = "count", defaultValue = "10") long count) {
        log.info(String.format("Get top %s most popular Films", count));
        return filmService.getFamousFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        log.info(String.format("User with id: %s added Like to Film id: %s", userId, id));
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info(String.format("User with id: %s deleted Like from Film id: %s", userId, id));
        return filmService.deleteLike(id, userId);
    }
}
