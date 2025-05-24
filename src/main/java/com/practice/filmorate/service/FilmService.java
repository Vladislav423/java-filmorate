package com.practice.filmorate.service;

import com.practice.filmorate.exception.FilmNotFoundException;
import com.practice.filmorate.exception.UserNotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import com.practice.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    public Film create(Film film){
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film){
        if (filmStorage.findById(film.getId()).isEmpty()) {
            log.warn("Попытка обновить несуществующий фильм с ID: " + film.getId());
            throw new FilmNotFoundException("Фильм с ID " + film.getId() + " не найден.");

        }
        validateFilm(film);
        return filmStorage.update(film);
    }

    public List<Film> findAll(){
        return filmStorage.findAll();
    }

    public void addLike(long filmId, long userId){
        if (filmStorage.findById(filmId).isEmpty()){
            throw new FilmNotFoundException("Фильм не найден");
        }
        if (userStorage.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        filmStorage.addLike(filmId,userId);
    }

    public void removeLike(long filmId, long userId){
        if (filmStorage.findById(filmId).isEmpty()){
            throw new FilmNotFoundException("Фильм не найден");
        }
        if (userStorage.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> findPopular(int count){
        return filmStorage.findPopular(count);
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
