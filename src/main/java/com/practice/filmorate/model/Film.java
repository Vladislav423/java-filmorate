package com.practice.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Film {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @Past
    private LocalDate releaseDate;
    @Positive
    private int duration;

    private final Set<Long> likes = new HashSet<>();
}
