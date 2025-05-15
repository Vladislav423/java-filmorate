package com.practice.filmorate.model;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String login;
    @NotNull
    private String name;
    @Past
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();
}
