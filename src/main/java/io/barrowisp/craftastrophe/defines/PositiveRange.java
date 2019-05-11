package io.barrowisp.craftastrophe.defines;

import io.barrowisp.craftastrophe.validator.PositiveRangeValidator;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierNickname;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * The annotated element must at all times be a number with a positive value
 * that does not exceed the maximum defined value {@code max()}.
 */
@CustomModAnnotation
@TypeQualifierNickname @Nonnull
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@Constraint(validatedBy = { PositiveRangeValidator.class })
@SuppressWarnings("unused")
public @interface PositiveRange {

    /* Declare this as a number type double so it can work with
     * integers, floats and other numbers without losing precision
     */
    double max();

    String level() default "ERROR";

    String message() default "Found invalid number value $value out of valid positive range (0-$max)";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
