package ru.yandex.practicum.filmorate.storage.filmMpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("mpaDbStorage")
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public Mpa getMpaById(int id) {
        String sql = "SELECT * " +
                     "FROM mpa " +
                     "WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, this::mapMpa, id);
    }

    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * " +
                          "FROM mpa;";
        return jdbcTemplate.query(sqlQuery, this::mapMpa);
    }

    private Mpa mapMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
