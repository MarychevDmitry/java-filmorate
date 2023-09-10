package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import static ru.yandex.practicum.filmorate.validator.FilmValidator.isFilmValid;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmControllerTest {

    @Test
    public void create_shouldCreateFilmIfFieldsIsValid() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .build();

        assertTrue(isFilmValid(film));
    }

    @Test
    void create_shouldNotCreateFilmWithNullOrEmptyName() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .build();

        String[] names = {null, "", " ", "  "};

        Arrays.stream(names).forEach(name -> {
            Film filmWithIncorrectName = film
                    .toBuilder()
                    .name(name)
                    .build();

            assertFalse(isFilmValid(filmWithIncorrectName));
        });
    }

    @Test
    void create_shouldNotCreateFilmWithDescriptionLongerThen200Symbols() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .build();

        Film filmWithIncorrectDescription = film
                .toBuilder()
                .description("Слишком длинное описание!".repeat(20))
                .build();

        assertFalse(isFilmValid(filmWithIncorrectDescription));
    }

    @Test
    void create_shouldCreateFilmWithReleaseDateFirstFilmDate() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .build();

        Film filmWithFirstFilmReleaseDate = film
                .toBuilder()
                .releaseDate(LocalDate.parse("1895-12-28"))
                .build();

       assertTrue(isFilmValid(filmWithFirstFilmReleaseDate));
    }

    @Test
    void create_shouldNotCreateFilmWithReleaseDateBeforeFirstFilmDate() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .build();

        Film filmWithIncorrectReleaseDate = film
                .toBuilder()
                .releaseDate(LocalDate.parse("1801-01-01"))
                .build();

        assertFalse(isFilmValid(filmWithIncorrectReleaseDate));
    }

    @Test
    void create_shouldNotCreateFilmWithNegativeDuration() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .build();

        Film filmWithNegativeDuration = film
                .toBuilder()
                .duration(-1)
                .build();

        assertFalse(isFilmValid(filmWithNegativeDuration));
    }
}