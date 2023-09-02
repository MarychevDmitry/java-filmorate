package ru.yandex.practicum.filmorate.model;



import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
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
