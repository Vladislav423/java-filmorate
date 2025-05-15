package com.practice.filmorate.storage;

import com.practice.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> findPopular(int count);
}
