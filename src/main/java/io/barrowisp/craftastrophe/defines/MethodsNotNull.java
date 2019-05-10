package io.barrowisp.craftastrophe.defines;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import javax.annotation.meta.TypeQualifierNickname;
import java.lang.annotation.*;

/**
 * This annotation can be applied to a class to indicate that the method
 * parameters and return values are nonnull by default unless there is
 * an explicit nullness annotation on the element that overrides it
 */
@Documented
@TypeQualifierDefault({ ElementType.METHOD, ElementType.PARAMETER })
@TypeQualifierNickname @Nonnull
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface MethodsNotNull { }
