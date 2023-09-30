package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmMpa.MpaDbStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDBStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void getAllMpa_shouldReturnAllMpa() {
        Assertions.assertEquals(5, mpaDbStorage.getAllMpa().size());
    }

    @Test
    public void getMpaById_shouldReturnMpaWithId() {
        Mpa mpa = Mpa.builder()
                .id(2)
                .name("PG")
                .description("Детям рекомендуется смотреть фильм с родителями")
                .build();
        Assertions.assertEquals(mpa, mpaDbStorage.getMpaById(2));
    }

    @Test
    public void getMpaById_shouldThrowExceptionIfMpaNotFound() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> mpaDbStorage.getMpaById(99));
    }
}
