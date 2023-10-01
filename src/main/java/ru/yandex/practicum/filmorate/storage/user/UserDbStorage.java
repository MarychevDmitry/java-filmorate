package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> mapUser = new HashMap<>();
        mapUser.put("email", user.getEmail());
        mapUser.put("login", user.getLogin());
        mapUser.put("name", user.getName());
        mapUser.put("birthday", user.getBirthday());
        user.setId(simpleJdbcInsert.executeAndReturnKey(mapUser).longValue());
        log.info("New User added to DB. id - {}", user.getId());
        return getUserById(user.getId());
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users " +
                          "SET email = ?, login = ?, name = ?, birthday = ? " +
                          "WHERE id = ?;";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("User updated in DB. id - {}", user.getId());
        return user;
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * " +
                          "FROM users;";
        return jdbcTemplate.query(sqlQuery, this::mapUser);
    }

    @Override
    public User getUserById(long id) {
        String sqlQuery = "SELECT id, email, login, name, birthday " +
                          "FROM users WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapUser, id);
    }

    @Override
    public List<User> getFriendsList(long id) {
        String sqlQuery = "SELECT u.id , u.email, u.login, u.name, u.birthday, " +
                          "FROM users u " +
                          "JOIN friends f ON f.user_id  = ? " +
                          "WHERE u.id  = f.FRIEND_ID ;";
        return jdbcTemplate.query(sqlQuery, this::mapUser, id);
    }

    @Override
    public List<Integer> getFriendsId(User user) {
        String sqlQuery = "SELECT friend_id " +
                          "FROM friends " +
                          "WHERE user_id = ?;";
        return jdbcTemplate.query(sqlQuery, this::mapFriedId, user.getId());
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "SELECT * " +
                          "FROM friends " +
                          "WHERE user_id = ? AND friend_id = ?;";
        if (!jdbcTemplate.queryForList(sqlQuery, friendId, userId).isEmpty()) {
            sqlQuery = "UPDATE friends " +
                       "SET status = ? " +
                       "WHERE user_id = ? AND friend_id = ?;";
            jdbcTemplate.update(sqlQuery, true, friendId, userId);
            sqlQuery = "INSERT into friends (user_id, friend_id, status) values(?, ?, ?);";
            jdbcTemplate.update(sqlQuery, userId, friendId, true);
        } else {
            sqlQuery = "INSERT into friends (user_id, friend_id) values(?, ?);";
            jdbcTemplate.update(sqlQuery, userId, friendId);
        }
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM friends " +
                     "WHERE user_id = ? AND friend_id = ?;";
        jdbcTemplate.update(sql, userId, friendId);
        jdbcTemplate.update(sql, friendId, userId);
    }

    @Override
    public boolean isUserExistInBd(long id) {
        String sqlQuery = "SELECT id, email, login, name, birthday " +
                "FROM users WHERE id = ?;";
        return !jdbcTemplate.query(sqlQuery, this::mapUser, id).isEmpty();
    }

    private Integer mapFriedId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("friend_id");
    }

    private User mapUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        user.setFriends(new HashSet<>(getFriendsId(user)));
        return user;
    }
}
