package com.practice.filmorate.storage;

import com.practice.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> findAll();

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    List<User> findFriend(long userId);

    List<User> findCommonFriend(long userId, long friendId);

    Optional<User> findById(long userId);

}
