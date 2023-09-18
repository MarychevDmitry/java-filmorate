package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    public User create(User user);

    public User update(User user);

    public Map<Integer, User> getAll();

    public User getById(Integer id);
}
