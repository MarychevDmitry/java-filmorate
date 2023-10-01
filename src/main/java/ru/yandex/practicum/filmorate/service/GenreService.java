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
            throw new GenreNotFoundException(id);
        }
    }

    public List<Genre> getAllGenre() {
        List<Genre> genres = genreDbStorage.getAllGenre();
        if (genres.isEmpty()) {
            throw new GenreNotFoundException("Genres list is empty.");
        }
        return genres;
    }
}
