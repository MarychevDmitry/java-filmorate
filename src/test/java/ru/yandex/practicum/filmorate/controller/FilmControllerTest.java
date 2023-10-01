package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.validator.FilmValidator.isFilmValid;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

@SpringBootTest
public class FilmControllerTest {


    @Test
    public void create_shouldCreateFilmIfFieldsIsValid() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .mpa(Mpa.builder().id((long) 1)
                        .name("G")
                        .description("У фильма нет возрастных ограничений")
                        .build())
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
                .mpa(Mpa.builder().id((long) 1)
                        .name("G")
                        .description("У фильма нет возрастных ограничений")
                        .build())
                .build();

        String[] names = {null, "", " ", "  "};

        Arrays.stream(names).forEach(name -> {
            Film filmWithIncorrectName = film
                    .toBuilder()
                    .name(name)
                    .build();

            FilmValidationException exception = Assertions.assertThrows(
                    FilmValidationException.class, () -> isFilmValid(filmWithIncorrectName));

            assertEquals(String.format("Name can't be blank or null. %s.", filmWithIncorrectName), exception.getMessage());
        });
    }

    @Test
    void create_shouldNotCreateFilmWithDescriptionLongerThen200Symbols() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .mpa(Mpa.builder().id((long) 1)
                        .name("G")
                        .description("У фильма нет возрастных ограничений")
                        .build())
                .build();

        Film filmWithIncorrectDescription = film
                .toBuilder()
                .description("Слишком длинное описание!".repeat(20))
                .build();

        FilmValidationException exception = Assertions.assertThrows(
                FilmValidationException.class, () -> isFilmValid(filmWithIncorrectDescription));

        assertEquals(String.format("Description can not be blank and it's length must be below 200. %s.", filmWithIncorrectDescription), exception.getMessage());
    }

    @Test
    void create_shouldCreateFilmWithReleaseDateFirstFilmDate() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .mpa(Mpa.builder().id((long) 1)
                        .name("G")
                        .description("У фильма нет возрастных ограничений")
                        .build())
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
                .mpa(Mpa.builder().id((long) 1)
                        .name("G")
                        .description("У фильма нет возрастных ограничений")
                        .build())
                .build();

        Film filmWithIncorrectReleaseDate = film
                .toBuilder()
                .releaseDate(LocalDate.parse("1801-01-01"))
                .build();

        FilmValidationException exception = Assertions.assertThrows(
                FilmValidationException.class, () -> isFilmValid(filmWithIncorrectReleaseDate));

        assertEquals(String.format("Film release date can't be before 1895-12-28. %s.", filmWithIncorrectReleaseDate), exception.getMessage());
    }

    @Test
    void create_shouldNotCreateFilmWithNegativeDuration() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test Description")
                .releaseDate(LocalDate.parse("2010-01-01"))
                .duration(115)
                .mpa(Mpa.builder().id((long) 1)
                        .name("G")
                        .description("У фильма нет возрастных ограничений")
                        .build())
                .build();

        Film filmWithNegativeDuration = film
                .toBuilder()
                .duration(-1)
                .build();

        FilmValidationException exception = Assertions.assertThrows(
                FilmValidationException.class, () -> isFilmValid(filmWithNegativeDuration));

        assertEquals(String.format("Duration should be positive. %s.", filmWithNegativeDuration), exception.getMessage());
    }
}