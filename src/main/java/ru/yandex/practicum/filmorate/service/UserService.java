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

    public User getUserById(long id) {
        isUserExist(id);

        return storage.getUserById(id);
    }

    public List<User> getUserFriends(long id) {
        isUserExist(id);

        return storage.getFriendsList(id);
    }

    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        isUserExist(firstUserId);
        isUserExist(secondUserId);

        Set<User> friendsFirstUser = new HashSet<>(storage.getFriendsList(firstUserId));
        friendsFirstUser.retainAll(storage.getFriendsList(secondUserId));
        List<User> mutualFriends = new ArrayList<>(friendsFirstUser);

        return mutualFriends;
    }

    public User addFriend(long userId, long friendId) {
        isUserExist(userId);
        isUserExist(friendId);

        storage.addFriend(userId, friendId);
        return storage.getUserById(userId);
    }

    public User deleteFriend(long userId, long friendId) {
        isUserExist(userId);
        isUserExist(friendId);

        storage.deleteFriend(userId, friendId);
        return storage.getUserById(userId);
    }

    private void isUserExist(long id) {
        if (!storage.isUserExistInBd(id)) {
            throw new UserNotFoundException(id);
        }
    }
}
