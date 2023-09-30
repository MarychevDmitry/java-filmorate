package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.filmGenre.GenreDbStorage;


import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public Genre getGenreById(int id) {
        try {
            return genreDbStorage.getGenreById(id);
        } catch (Exception e) {
            throw new GenreNotFoundException(String.format("Genre with id %s. not found.", id));
        }
    }

    public List<Genre> getAllGenre() {
        try {
            return genreDbStorage.getAllGenre();
        } catch (Exception e) {
            throw new GenreNotFoundException("Genres not found.");
        }
    }
}
