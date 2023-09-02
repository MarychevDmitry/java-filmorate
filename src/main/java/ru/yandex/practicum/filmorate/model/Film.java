package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.BeforeDate;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    @Min(1)
    private Integer id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @BeforeDate("1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
