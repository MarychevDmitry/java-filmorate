package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import static ru.yandex.practicum.filmorate.validator.UserValidator.isUserValid;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User createUser(User user) {
        isUserValid(user);

        return storage.createUser(user);
    }

    public User updateUser(User user) {
        isUserValid(user);
        isUserExist(user.getId());

        return storage.updateUser(user);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User getUserById(int id) {
        isUserExist(id);

        return storage.getUserById(id);
    }

    public List<User> getUserFriends(int id) {
        isUserExist(id);

        return storage.getFriendsList(id);
    }

    public List<User> getCommonFriends(int firstUserId, int secondUserId) {
        isUserExist(firstUserId);
        isUserExist(secondUserId);

        Set<User> friendsFirstUser = new HashSet<>(storage.getFriendsList(firstUserId));
        friendsFirstUser.retainAll(storage.getFriendsList(secondUserId));
        List<User> mutualFriends = new ArrayList<>(friendsFirstUser);

        return mutualFriends;
    }

    public User addFriend(Integer userId, Integer friendId) {
        isUserExist(userId);
        isUserExist(friendId);

        storage.addFriend(userId, friendId);
        return storage.getUserById(userId);
    }

    public User deleteFriend(int userId, int friendId) {
        isUserExist(userId);
        isUserExist(friendId);

        storage.deleteFriend(userId, friendId);
        return storage.getUserById(userId);
    }

    private void isUserExist(Integer id) {
        if (!storage.isUserExistInBd(id)) {
            throw new UserNotFoundException(String.format("User with id %s. not found", id));
        }
    }
}
