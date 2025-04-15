package com.practice.filmorate.controller;

import com.practice.filmorate.exception.UserNotFoundException;
import com.practice.filmorate.exception.ValidationException;
import com.practice.filmorate.model.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUser(user);
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Пользователь успешно добавлен");
        return user;
    }
    @PutMapping
    public User update(@Valid @RequestBody User user){
        validateUser(user);
        if (!users.containsKey(user.getId())){
            throw new UserNotFoundException("Пользователь не найден");
        }
        log.info("Пользователь успешно обновлен");
        users.put(user.getId(), user);
        return user;
    }

    private void validateUser(User user){
        if (user.getEmail() == null || !user.getEmail().contains("@") ||
                user.getLogin() == null || user.getLogin().isBlank() || user.getBirthday() == null
        ) {
            log.warn("Произошла ошибка при добавлении пользователя");
            throw new ValidationException("Данные пользователя введены неверно");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя не указано. Установлено значение логина: " + user.getLogin());
        }
    }
}
