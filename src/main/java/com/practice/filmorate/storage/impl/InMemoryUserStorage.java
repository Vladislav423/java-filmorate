package com.practice.filmorate.storage.impl;

import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User create(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен");
        return user;
    }

    @Override
    public User update(User user) {
        log.info("Пользователь успешно обновлен");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> findFriend(long userId) {
        List<User> result = new ArrayList<>();
        for (Long friendId : users.get(userId).getFriends()) {
            result.add(users.get(friendId));
        }
        return result;
    }

    @Override
    public List<User> findCommonFriend(long userId, long friendId) {
        Set<Long> commonFriends = new HashSet<>(users.get(userId).getFriends());
        commonFriends.retainAll(users.get(friendId).getFriends());
        List<User> result = new ArrayList<>();
        for (Long id : commonFriends) {
            User friend = users.get(id);
            if (friend != null) {
                result.add(friend);
            }
        }
        return result;
    }
}
