package io.barrowisp.craftastrophe.validator;

import io.barrowisp.craftastrophe.defines.PositiveRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositiveRangeValidator implements ConstraintValidator<PositiveRange, Number> {

    private PositiveRange constraint;

    @Override
    public void initialize(PositiveRange constraint) {
        this.constraint = constraint;
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext ctx) {

        double number = value != null ? value.doubleValue() : 0;
        return value != null && number > 0 && number < constraint.max();
    }
}
