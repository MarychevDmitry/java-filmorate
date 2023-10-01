package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmGenre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("films").usingGeneratedKeyColumns("id");
        Map<String, Object> mapFilm = new HashMap<>();
        mapFilm.put("name", film.getName());
        mapFilm.put("description", film.getDescription());
        mapFilm.put("release_date", film.getReleaseDate());
        mapFilm.put("duration", film.getDuration());
        mapFilm.put("mpa_id", film.getMpa().getId());
        film.setId(simpleJdbcInsert.executeAndReturnKey(mapFilm).longValue());
        genreStorage.createFilmGenre(film);

        film.getGenres().clear();
        film.setMpa(getFilmById(film.getId()).getMpa());
        log.info("Film added to DB. id - {}", film.getId());
        return setLikesInFilm(List.of(film)).get(0);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                          "WHERE id = ?;";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        genreStorage.createFilmGenre(film);

        film.setMpa(getFilmById(film.getId()).getMpa());
        log.info("Film updated in DB. id {}", film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, " +
                                 "m.id as mpa_id, m.name as mpa_name " +
                          "FROM films f " +
                          "JOIN mpa m ON f.mpa_id = m.id";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapFilm);
        return setLikesInFilm(films);
    }

    @Override
    public boolean checkFilmExistInBd(long id) {
        String sqlQuery = "SELECT id " +
                          "FROM films " +
                          "WHERE id = ?;";
        return !jdbcTemplate.query(sqlQuery, this::mapFilmId, id).isEmpty();
    }

    @Override
    public List<Film> setLikesInFilm(List<Film> films) {
        List<Long> filmIds = new ArrayList<>();
        films.forEach(film -> filmIds.add(film.getId()));
        String sql = "SELECT f.id , l.user_id " +
                     "FROM films f " +
                     "JOIN film_likes l ON f.id  = l.film_id " +
                     "WHERE f.id  IN (" + StringUtils.join(filmIds, ',') + ")" +
                     "ORDER by user_id asc;";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql);
        while (genreRows.next()) {
            for (Film film : films) {
                if (film.getId() == genreRows.getInt("id")) {
                    film.getLikes().add(genreRows.getInt("user_id"));
                }
            }
        }
        return films;
    }

    @Override
    public Film getFilmById(long filmId) {
        String sqlQuery = "SELECT f.id , f.name, f.description, f.release_date, f.duration, " +
                                 "m.id as mpa_id ,m.name as mpa_name " +
                          "FROM films f " +
                          "JOIN mpa m ON f.mpa_id  = m.id " +
                          "WHERE f.id = ?;";
        return setLikesInFilm(List.of(jdbcTemplate.query(sqlQuery, this::mapFilm, filmId).get(0))).get(0);
    }

    @Override
    public void addLike(long userId, long filmId) {
        String sqlQuery = "INSERT into film_likes (film_id, user_id) values(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        String sql = "DELETE FROM film_likes " +
                     "WHERE user_id = ? and film_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public Set<Integer> getLikesByFilmId(long filmId) {
        String sql = "SELECT user_id " +
                     "FROM film_likes " +
                     "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, this::mapLike, filmId));
    }

    private Integer mapFilmId(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("id");
    }

    private Integer mapLike(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("user_id");
    }

    private Film mapFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(Mpa.builder()
                        .id(resultSet.getLong("mpa_id"))
                        .name(resultSet.getString("mpa_name"))
                        .build())
                .build();
        return film;
    }
}
