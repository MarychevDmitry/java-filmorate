package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Integer id = 1;

    protected final Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User getById(Integer id) {
        return users.get(id);
    }

    @Override
    public Map<Integer, User> getAll() {
        return new HashMap<>(users);
    }

    private Integer generateId() {
        return id++;
    }
}