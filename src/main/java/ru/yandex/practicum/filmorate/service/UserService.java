package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.UserValidator.isUserNameValid;

@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage userStorage;

    public User createUser(User user) {
        setUserName(user);

        return userStorage.create(user);
    }

    public User updateUser(User user) {
        checkUserIsNotNull(user.getId());

        setUserName(user);

        return userStorage.update(user);
    }

    public Map<Integer, User> getUsers() {
        return userStorage.getAll();
    }

    public User getUserById(Integer id) {
        checkUserIsNotNull(id);

        return userStorage.getById(id);
    }

    public List<User> getUserFriends(Integer id) {
        checkUserIsNotNull(id);

        return userStorage.getById(id)
                .getFriends()
                .stream()
                .map(getUsers()::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer friendId) {
        checkUserIsNotNull(id);
        checkUserIsNotNull(friendId);

        User userOne = userStorage.getById(id);
        User userTwo = userStorage.getById(friendId);

        Map<Integer, User> users = getUsers();
        Set<Integer> user1Friends = userOne.getFriends();
        Set<Integer> user2Friends = userTwo.getFriends();

        return user1Friends
                .stream()
                .filter(user2Friends::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }

    public void addFriend(Integer id, Integer friendId) {
        checkUserIsNotNull(id);
        checkUserIsNotNull(friendId);

        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void deleteFriend(Integer id, Integer friendId) {
        checkUserIsNotNull(id);
        checkUserIsNotNull(friendId);

        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    private void checkUserIsNotNull(Integer id) {
        if (userStorage.getById(id) == null) {
            throw new UserNotFoundException(String.format("User with id %s. not found", id));
        }
    }

    private void setUserName(User user) {
        if (!isUserNameValid(user.getName())) {
            user.setName(user.getLogin());
        }
    }
}