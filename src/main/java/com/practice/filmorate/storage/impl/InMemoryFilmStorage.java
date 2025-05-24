package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @Override
    public List<Film> findAll() {
        log.info("Выполнен запрос GET /films");
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(long filmId){
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Film create(Film film) {
        log.info("Выполнен запрос Post /films");
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен");
        return film;

    }

    @Override
    public Film update(Film film) {
        log.info("Выполнен запрос Put /films с ID: " + film.getId());
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен");
        return film;
    }

    @Override
    public void addLike(long filmId, long userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> findPopular(int count){
        return films.values().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());


    }
}
