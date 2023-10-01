package ru.yandex.practicum.filmorate.storage.filmMpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    Mpa getMpaById(long id);

    List<Mpa> getAllMpa();
}
