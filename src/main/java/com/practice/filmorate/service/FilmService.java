package com.practice.filmorate.service;

import com.practice.filmorate.exception.UserNotFoundException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import com.practice.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film){
        return filmStorage.create(film);
    }
    public Film update(Film film){
        return filmStorage.update(film);
    }
    public List<Film> findAll(){
        return filmStorage.findAll();
    }
    public void addLike(long filmId, long userId){
        if (userStorage.findById(userId).isEmpty()){
            throw new UserNotFoundException("Пользователь не найден");
        }
        filmStorage.addLike(filmId,userId);
    }
    public void removeLike(long filmId, long userId){
        if (userStorage.findById(userId).isEmpty()){
            throw new UserNotFoundException("Пользователь не найден");
        }
        filmStorage.removeLike(filmId, userId);
    }
    public List<Film> findPopular(int count){
        return filmStorage.findPopular(count);
    }
}
