package com.practice.filmorate.controller;

import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.Film;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    private static final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getAll() {
        log.info("Выполнен запрос GET /films");
        return new ArrayList<>(films.values());
    }
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Выполнен запрос Post /films");
        validateFilm(film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен");
        return film;

    }
    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        log.info("Выполнен запрос Put /films с ID: " + film.getId());
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Попытка обновить несуществующий фильм с ID: " + film.getId());
            throw new ValidationException("Фильм с ID " + film.getId() + " не найден.");

        }
        film.setId(film.getId());
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен");
        return film;

    }
    private void validateFilm(Film film){
        if (film.getName() == null || film.getName().isBlank() ||
                film.getDescription() != null && film.getDescription().length() > 200 ||
                film.getReleaseDate() == null || film.getReleaseDate().isBefore(earliestReleaseDate) ||
                film.getDuration() <= 0) {
            log.warn("Произошла ошибка при добавлении фильма");
            throw new ValidationException("Некорректные данные фильма для обновления.");
        }
    }

}
