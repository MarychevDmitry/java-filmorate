package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbTest {
    private final UserDbStorage userDbStorage;
    private User user;
    private User friend;

    @Test
    public void create_shouldAddUserToDB() {
        user = User.builder()
                .email("mail@mail.mail")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990, 10, 15))
                .build();

        userDbStorage.createUser(user);

        Assertions.assertEquals(1, userDbStorage.getUserById(1).getId());
    }

    @Test
    public void update_shouldUpdateUserInDB() {
        user = User.builder()
                .email("mail@mail.mail")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990, 10, 15))
                .build();

        userDbStorage.createUser(user);
        user.setName("updatedName");
        user.setId((long) 1);
        userDbStorage.updateUser(user);
        user.setName("name");

        Assertions.assertNotEquals(user, userDbStorage.getUserById(user.getId()));
    }

    @Test
    void addFriend_ShouldAddUserToFriends() {
        user = User.builder()
                .email("mail@mail.mail")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990, 10, 15))
                .build();
        friend = User.builder()
                .email("friend@mail.mail")
                .login("friend")
                .name("name")
                .birthday(LocalDate.of(1980, 5, 10))
                .build();

        userDbStorage.createUser(user);
        userDbStorage.createUser(friend);
        userDbStorage.addFriend(user.getId(), friend.getId());

        Assertions.assertEquals(1, userDbStorage.getFriendsList(user.getId()).size());
        Assertions.assertEquals(0, userDbStorage.getFriendsList(friend.getId()).size());
    }

    @Test
    void deleteFriend_ShouldAddUserToFriends() {
        user = User.builder()
                .email("mail@mail.mail")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990, 10, 15))
                .build();
        friend = User.builder()
                .email("friend@mail.mail")
                .login("friend")
                .name("name")
                .birthday(LocalDate.of(1980, 5, 10))
                .build();

        userDbStorage.createUser(user);
        userDbStorage.createUser(friend);
        userDbStorage.addFriend(user.getId(), friend.getId());
        userDbStorage.deleteFriend(user.getId(), friend.getId());

        Assertions.assertEquals(0, userDbStorage.getFriendsList(user.getId()).size());
        Assertions.assertEquals(0, userDbStorage.getFriendsList(friend.getId()).size());
    }
}
