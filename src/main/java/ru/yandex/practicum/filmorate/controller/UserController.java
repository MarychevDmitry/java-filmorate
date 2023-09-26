package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import static ru.yandex.practicum.filmorate.validator.UserValidator.isUserValid;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        isUserValid(user);
        log.info(String.format("Created new %s.", user));
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        isUserValid(user);
        log.info(String.format("Updated User with id: %s. %s.", user.getId(), user));
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info(String.format("Get all Users. Total amount of Users: %s", userService.getUsers().size()));
        return new ArrayList<>(userService.getUsers().values());
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        log.info(String.format("Get User with id: %s. %s.", id, userService.getUserById(id)));
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Integer id) {
        log.info(String.format("Get Friends of User with id: %s.", id));
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        log.info(String.format("Get common friends of Users with id: %s and id: %s.", id, otherId));
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.addFriend(id, friendId);
        log.info(String.format("User with id: %s added to friends User with id: %s.", id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.deleteFriend(id, friendId);
        log.info(String.format("User with id: %s removed from friends User with id: %s.", id, friendId));
    }
}

