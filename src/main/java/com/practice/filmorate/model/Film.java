package com.practice.filmorate.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Film {
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @Past
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
