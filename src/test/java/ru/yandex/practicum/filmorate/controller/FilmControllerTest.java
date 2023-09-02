package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest

public class FilmControllerTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final Film testFilm = Film.builder()
            .name("Test Film")
            .description("Test Description")
            .releaseDate(LocalDate.parse("2010-01-01"))
            .duration(115)
            .build();

    @Test
    public void create_shouldCreateFilmIfFieldsIsValid() {
        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);

        assertAll("Test: create | Должен проверить валидность полей объекта и присвоить ему id.",
                () -> assertTrue(violations.isEmpty())
        );
    }

    @Test
    void create_shouldNotCreateFilmWithNullOrEmptyName() {
        String[] names = {null, "", " ", "  "};

        Arrays.stream(names).forEach(name -> {
            Film filmWithIncorrectName = testFilm
                    .toBuilder()
                    .name(name)
                    .build();

            Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectName);

            assertAll("Test: create | Фильм с пустым название или названием равным null не должен пройти валидацию.",
                    () -> assertFalse(violations.isEmpty())
            );
        });
    }

    @Test
    void create_shouldNotCreateFilmWithDescriptionLongerThen200Symbols() {
        Film filmWithIncorrectDescription = testFilm
                .toBuilder()
                .description("Слишком длинное описание!".repeat(20))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectDescription);

        assertAll("Test: create | Фильм с описание длиннее 200 символов не должен пройти вылидацию.",
                () -> assertFalse(violations.isEmpty())
        );
    }

    @Test
    void create_shouldCreateFilmWithReleaseDateFirstFilmDate() {
        Film filmWithFirstFilmReleaseDate = testFilm
                .toBuilder()
                .releaseDate(LocalDate.parse("1895-12-28"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithFirstFilmReleaseDate);

        assertAll("Test: create | Фильм с датой выхда 28 декабря 1895 года должен пройти валиддацию.",
                () -> assertTrue(violations.isEmpty())
        );
    }

    @Test
    void create_shouldNotCreateFilmWithReleaseDateBeforeFirstFilmDate() {
        Film filmWithIncorrectReleaseDate = testFilm
                .toBuilder()
                .releaseDate(LocalDate.parse("1801-01-01"))
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithIncorrectReleaseDate);

        assertAll("Test: create | Фильм с датой выхда раньше 28 декабря 1895 года не должен пройти валиддацию.",
                () -> assertFalse(violations.isEmpty()),
                () -> assertEquals(1, violations.size())
        );
    }

    @Test
    void create_shouldNotCreateFilmWithNegativeDuration() {
        Film filmWithNegativeDuration = testFilm
                .toBuilder()
                .duration(-1)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(filmWithNegativeDuration);

        assertAll("Test: create | Фильм с отрицательной продолжительностью не должне пройти валидацию",
                () -> assertFalse(violations.isEmpty()),
                () -> assertEquals(1, violations.size())
        );
    }
}