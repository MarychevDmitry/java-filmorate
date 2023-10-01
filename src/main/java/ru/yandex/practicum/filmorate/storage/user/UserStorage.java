package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(long id);

    List<Integer> getFriendsId(User user);

    List<User> getFriendsList(long userId);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    boolean isUserExistInBd(long id);
}
