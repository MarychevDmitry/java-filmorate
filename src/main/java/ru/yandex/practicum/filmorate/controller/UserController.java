package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info(String.format("Creating new %s.", user));
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info(String.format("Updating User with id: %s. %s.", user.getId(), user));
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info(String.format("Get all Users. Total amount of Users: %s", userService.getUsers().size()));
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info(String.format("Get User with id: %s. %s.", id, userService.getUserById(id)));
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info(String.format("Get Friends of User with id: %s.", id));
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info(String.format("Get common friends of Users with id: %s and id: %s.", id, otherId));
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(String.format("User with id: %s added to friends User with id: %s.", id, friendId));
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    private User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(String.format("User with id: %s removed from friends User with id: %s.", id, friendId));
        return userService.deleteFriend(id, friendId);
    }
}
