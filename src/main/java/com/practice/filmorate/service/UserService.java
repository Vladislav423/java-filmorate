package com.practice.filmorate.service;

import com.practice.filmorate.exception.UserNotFoundException;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(Integer userId, long friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Integer userId, long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> findFriend(long userId) {
        return userStorage.findFriend(userId);
    }

    public List<User> findCommonFriend(long userId, long friendId) {
        return userStorage.findCommonFriend(userId, friendId);
    }
}
