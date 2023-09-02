package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final User user = User
            .builder()
            .login("TestLogin")
            .name("TestName")
            .email("testemail@email.com")
            .birthday(LocalDate.parse("1996-03-04"))
            .build();

    @Test
    void create_shouldCreateUserIfFieldsIsValid() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertAll("Test: create | Должен проверить валидность полей объекта и присвоить ему id.",
                () -> assertTrue(violations.isEmpty())
        );
    }

    @Test
    void create_shouldNotCreateUserWithWrongLogin() {
        String[] logins = {null, "wrong name", "", " "};

        Arrays.stream(logins).forEach(login -> {
            User userWithIncorrectLogin = user
                    .toBuilder()
                    .login(login)
                    .build();

            Set<ConstraintViolation<User>> violations = validator.validate(userWithIncorrectLogin);

            assertAll("Test: create | Пользователь с пустым или равным null логином не должен пройти валидацию.",
                    () -> assertFalse(violations.isEmpty())
            );
        });
    }

    @Test
    void create_shouldNotCreateUserWithWrongEmail() {
        String[] emails = {null, "", " ", "test.@email.ru", ".test.@email.ru", "testemail.ru"};

        Arrays.stream(emails).forEach(email -> {
            User userWithIncorrectEmail = user
                    .toBuilder()
                    .email(email)
                    .build();

            Set<ConstraintViolation<User>> violations = validator.validate(userWithIncorrectEmail);

            assertAll("Test: create | Пользователь с неверным email не должен пройти валидацию.",
                    () -> assertFalse(violations.isEmpty())
            );
        });
    }

    @Test
    void create_shouldNotCreateUserWithBirthdayInFuture() {
        User userWithBirthdayInFuture = user
                .toBuilder()
                .birthday(LocalDate.parse("3000-01-01"))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(userWithBirthdayInFuture);

        assertAll("Test: create | Пользователь с датой рождения в будщем не должен пройти валидацию.",
                () -> assertFalse(violations.isEmpty()),
                () -> assertEquals(1, violations.size())
        );
    }
}