package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.BeforeDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
