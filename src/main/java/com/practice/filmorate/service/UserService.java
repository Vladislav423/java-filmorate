package com.practice.filmorate.service;

import com.practice.filmorate.exception.UserNotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        if (userStorage.findById(user.getId()).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        validateUser(user);
        return userStorage.update(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(Integer userId, long friendId) {
        if (userStorage.findById(userId).isEmpty() || userStorage.findById(friendId).isEmpty()) {
            throw new UserNotFoundException("Пользователь или друг не найден");
        }
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Integer userId, long friendId) {
        if (userStorage.findById(userId).isEmpty() || userStorage.findById(friendId).isEmpty()) {
            throw new UserNotFoundException("Пользователь или друг не найден");
        }
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> findFriend(long userId) {
        if (userStorage.findById(userId).isEmpty()) throw new UserNotFoundException("Друг не найден");
        return userStorage.findFriend(userId);
    }

    public List<User> findCommonFriend(long userId, long friendId) {
        if (userStorage.findById(userId).isEmpty() || userStorage.findById(friendId).isEmpty()){
            throw new UserNotFoundException("Один из пользователей не найден");
        }
        return userStorage.findCommonFriend(userId, friendId);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null
                || user.getEmail().isBlank()
                || !user.getEmail().contains("@")
                || user.getLogin() == null
                || user.getLogin().isBlank()
                || user.getLogin().contains(" ")
                || user.getBirthday() == null
                || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Произошла ошибка при добавлении пользователя");
            throw new ValidationException("Данные пользователя введены неверно");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя не указано — подставлен login");
        }
    }
}
