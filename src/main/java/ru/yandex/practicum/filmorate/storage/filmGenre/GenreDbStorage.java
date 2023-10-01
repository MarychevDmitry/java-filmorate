package ru.yandex.practicum.filmorate.storage.filmGenre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import static ru.yandex.practicum.filmorate.validator.GenreValidator.isGenreValid;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("genreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(long id) {
        String sql = "SELECT g.id ," +
                            "g.name " +
                     "FROM genre g " +
                     "WHERE g.id = ?;";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
    }

    @Override
    public Film setGenre(Film film) {
        List<Film> films = setGenres(List.of(film));
        return films.get(0);
    }

    public List<Film> setGenres(List<Film> films) {
        List<Long> filmIds = new ArrayList<>();
        films.forEach(film -> filmIds.add(film.getId()));
        String sql = "SELECT fg.film_id, g.id, g.name " +
                     "FROM genre g " +
                     "JOIN film_genre fg ON fg.genre_id = g.id " +
                     "WHERE fg.film_id IN (" + StringUtils.join(filmIds, ',') + ")" +
                     "ORDER BY g.id asc;";

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql);
        while (genreRows.next()) {
            for (Film film : films) {
                if (film.getId() == genreRows.getInt("film_id")) {
                    film.getGenres().add(Genre.builder()
                            .id(genreRows.getLong("id"))
                            .name(genreRows.getString("name")).build());
                }
            }
        }
        return films;
    }

    @Override
    public List<Genre> getAllGenre() {
        String sqlQuery = "SELECT * " +
                          "FROM genre " +
                          "GROUP BY id ORDER BY id ASC;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public void createFilmGenre(Film film) {
        String sqlQuery = "INSERT into film_genre (film_id, genre_id) values(?, ?);";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public void deleteFilmGenre(Film film) {
        String sql = "DELETE FROM film_genre " +
                     "WHERE film_id = ?;";
        jdbcTemplate.update(sql, film.getId());
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = Genre.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
        isGenreValid(genre);
        return genre;
    }
}
