package com.theodo.albeniz.validation;

import com.theodo.albeniz.dto.Tune;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotChildrenSongValidation implements ConstraintValidator<NotAChildrenSong, Tune> {

    @Override
    public boolean isValid(Tune tune, ConstraintValidatorContext context) {
        return !tune.getAuthor().contains("Chantal G.");
    }
}
