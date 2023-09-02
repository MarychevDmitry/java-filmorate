package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BeforeDateValidator implements ConstraintValidator<BeforeDate, LocalDate> {
    private LocalDate date;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(BeforeDate constraintAnnotation) {
        this.date = LocalDate.parse(constraintAnnotation.value(), formatter);
    }

    @Override
    public boolean isValid(LocalDate target, ConstraintValidatorContext context) {
        return !target.isBefore(date);
    }
}