package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}. \n Список всех пользователей: {}", users.size(), users);

        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        user.setId(getId());
        user.updateName();
        users.put(user.getId(), user);
        log.debug("Добавлен новый Пользователь: {}.", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Запрос на обновления пользователь с id: {}.", user.getId());
        if (!users.containsKey(user.getId())) {
            throw new UpdateException("Пользователь с id: " + user.getId() + " не найден.");
        }
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь с id: {}. Пользователь: {}", user.getId(), user);
        return user;
    }

    public int getId() {
        return id++;
    }
}
