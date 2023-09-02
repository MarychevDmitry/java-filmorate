package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
    @Min(1)
    private Integer id;
    @Email
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "\\S*$")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    public void updateName() {
        if (name == null || name.isEmpty()) {
            name = login;
        }
    }
}
