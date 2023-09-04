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

import static ru.yandex.practicum.filmorate.validator.UserValidator.isUserValid;

import javax.validation.ValidationException;
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
        log.info("Request: List of all Users.\nCurrent number of Users: {}.\nList of all Users: {}", users.size(), users);

        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Request: Adding  new User.");
        if (!isUserValid(user)) {
            throw new ValidationException("User validation Error.");
        }
        user.setId(getId());
        user.updateName();
        users.put(user.getId(), user);
        log.info("New User added: {}.", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Request: Update User with id: {}.", user.getId());
        if (!isUserValid(user)) {
            throw new ValidationException("User validation Error.");
        }
        if (!users.containsKey(user.getId())) {
            throw new UpdateException("User with id: " + user.getId() + " not found.");
        }
        users.put(user.getId(), user);
        log.info("Updated User with id: {}. User: {}", user.getId(), user);
        return user;
    }

    private int getId() {
        return id++;
    }
}
