package com.practice.filmorate.storage.impl;

import com.practice.filmorate.exception.FilmNotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private long nextId = 1;
    private static final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    @Override
    public List<Film> findAll() {
        log.info("Выполнен запрос GET /films");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film create(Film film) {
        log.info("Выполнен запрос Post /films");
        validateFilm(film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен");
        return film;

    }

    @Override
    public Film update(Film film) {
        log.info("Выполнен запрос Put /films с ID: " + film.getId());
        if (!films.containsKey(film.getId())) {
            log.warn("Попытка обновить несуществующий фильм с ID: " + film.getId());
            throw new FilmNotFoundException("Фильм с ID " + film.getId() + " не найден.");

        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен");
        return film;
    }

    @Override
    public void addLike(long filmId, long userId) {
         if (films.get(filmId) == null){
             throw new FilmNotFoundException("Фильм не найден");
         }
        validateFilm(films.get(filmId));
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        if (films.get(filmId) == null){
            throw new FilmNotFoundException("Фильм не найден");
        }
        validateFilm(films.get(filmId));
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> findPopular(int count){
        return films.values().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());


    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank() ||
                film.getDescription() != null && film.getDescription().length() > 200 ||
                film.getReleaseDate() == null || film.getReleaseDate().isBefore(earliestReleaseDate) ||
                film.getDuration() <= 0) {
            log.warn("Произошла ошибка при добавлении фильма");
            throw new ValidationException("Некорректные данные фильма.");
        }
    }
}
