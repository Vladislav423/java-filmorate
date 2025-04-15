package com.practice.filmorate.model;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String login;
    @NotNull
    private String name;
    @Past
    private LocalDate birthday;
}
