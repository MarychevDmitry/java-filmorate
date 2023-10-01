package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmMpa.MpaDbStorage;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public Mpa getMpaById(long id) {
        try {
            return mpaDbStorage.getMpaById(id);
        } catch (Exception e) {
            throw new MpaNotFoundException(id);
        }
    }

    public List<Mpa> getAllMpa() {
        List<Mpa> mpas = mpaDbStorage.getAllMpa()
                .stream()
                .sorted(Comparator.comparingLong(Mpa::getId))
                .collect(Collectors.toList());
        if (mpas.isEmpty()) {
            throw new MpaNotFoundException("Mpa list is empty.");
        }
        return mpas;
    }
}
