package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;

import static ru.yandex.practicum.filmorate.validator.UserValidator.isUserValid;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @Test
    void create_shouldCreateUserIfFieldsIsValid() {
        User user = User
                .builder()
                .login("TestLogin")
                .name("TestName")
                .email("email@email.com")
                .birthday(LocalDate.parse("1996-03-04"))
                .build();

        assertTrue(isUserValid(user));
    }

    @Test
    void create_shouldNotCreateUserWithWrongLogin() {
        User user = User
                .builder()
                .login("TestLogin")
                .name("TestName")
                .email("email@email.com")
                .birthday(LocalDate.parse("1996-03-04"))
                .build();

        String[] logins = {null, "wrong name", "", " "};

        Arrays.stream(logins).forEach(login -> {
            User userWithIncorrectLogin = user
                    .toBuilder()
                    .login(login)
                    .build();

            assertFalse(isUserValid(userWithIncorrectLogin));
        });
    }

    @Test
    void create_shouldNotCreateUserWithWrongEmail() {
        User user = User
                .builder()
                .login("TestLogin")
                .name("TestName")
                .email("email@email.com")
                .birthday(LocalDate.parse("1996-03-04"))
                .build();

        String[] emails = {null, "", " ", "test.@email.ru", ".test.@email.ru", "testemail.ru"};

        Arrays.stream(emails).forEach(email -> {
            User userWithIncorrectEmail = user
                    .toBuilder()
                    .email(email)
                    .build();

            assertFalse(isUserValid(userWithIncorrectEmail));
        });
    }

    @Test
    void create_shouldNotCreateUserWithBirthdayInFuture() {
        User user = User
                .builder()
                .login("TestLogin")
                .name("TestName")
                .email("email@email.com")
                .birthday(LocalDate.parse("1996-03-04"))
                .build();

        User userWithBirthdayInFuture = user
                .toBuilder()
                .birthday(LocalDate.parse("3000-01-01"))
                .build();

        assertFalse(isUserValid(userWithBirthdayInFuture));
    }
}