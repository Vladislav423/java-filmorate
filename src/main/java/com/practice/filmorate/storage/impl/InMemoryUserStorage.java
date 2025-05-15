package com.practice.filmorate.storage.impl;

import com.practice.filmorate.exception.UserNotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.*;


@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(long userId){
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User create(@RequestBody User user) {
        validateUser(user);
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен");
        return user;
    }

    @Override
    public User update(@RequestBody User user) {
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        log.info("Пользователь успешно обновлен");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        if (user == null || friend == null) {
            throw new UserNotFoundException("Пользователь или друг не найден");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        if (user == null || friend == null) {
            throw new UserNotFoundException("Пользователь или друг не найден");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> findFriend(long userId) {
        if (users.get(userId) == null ) throw new UserNotFoundException("Друг не найден");
        List<User> result = new ArrayList<>();
        for (Long friendId : users.get(userId).getFriends()) {
            result.add(users.get(friendId));
        }
        return result;
    }

    @Override
    public List<User> findCommonFriend(long userId, long friendId) {
        if (users.get(userId) == null || users.get(friendId) == null){
            throw new UserNotFoundException("Один из пользователей не найден");
        }

        Set<Long> commonFriends = new HashSet<>(users.get(userId).getFriends());
        commonFriends.retainAll(users.get(friendId).getFriends());

        List<User> result = new ArrayList<>();
        for (Long id : commonFriends){
            User friend = users.get(id);
            if (friend != null){
                result.add(friend);
            }
        }
            return result;


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
